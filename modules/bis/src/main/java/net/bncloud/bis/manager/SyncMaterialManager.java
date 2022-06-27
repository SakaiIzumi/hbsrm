package net.bncloud.bis.manager;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.service.K3ERPEnumService;
import net.bncloud.common.api.R;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.msk3cloud.client.K3CloudApiClient;
import net.bncloud.msk3cloud.constant.formid.SupplierChainConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.kingdee.request.GroupQueryParam;
import net.bncloud.msk3cloud.kingdee.response.ResponseResult;
import net.bncloud.quotation.service.api.dto.MaterialGroupInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.enums.MaterialSyncEnums;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import net.bncloud.quotation.service.api.feign.MaterialGroupFeignClient;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.bncloud.bis.constant.BisSyncConstants.SYNC_MATERIAL_INFO_DATE_KEY;
import static net.bncloud.quotation.service.api.dto.MaterialInfoDTO.*;

/**
 * 同步金蝶ERP物料
 *
 * @author lijiaju
 * @date 2022/2/21 11:40
 */
@Slf4j
@Component
@AllArgsConstructor
public class SyncMaterialManager {

    private final SegmentedQueryManager segmentedQueryManager;
    private final MaterialFeignClient materialFeignClient;
    private final MaterialGroupFeignClient materialGroupFeignClient;
    private final K3ERPEnumService k3ERPEnumService;
    private final RedissonClient redissonClient;
    private final DistributedLock distributedLock;
    private final K3CloudApiClient k3CloudApiClient;

    /**
     * @param timeSyncCount 最大2000 每次同步的数据量
     */
    public R<Object> syncAllMaterialData(Integer timeSyncCount) {
        LockWrapper lockWrapper = new LockWrapper().setKey(BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.SYNC_MATERIAL_INFO_DATE_KEY).setWaitTime(0).setLeaseTime(15).setUnit(TimeUnit.MINUTES);
        return distributedLock.tryLock(lockWrapper, () -> {
            log.info("物料数据->开始同步");
            syncMaterialInfo(timeSyncCount);
            syncMaterialGroupData();
            log.info("物料数据->同步完成");
            return R.success("同步成功！");
        }, () -> R.fail("已经有同步任务在进行了."));
    }

    /**
     * 同步物料主数据
     * @param timeSyncCount 最大2000 每次同步的数据量
     */
    public void syncMaterialInfo(Integer timeSyncCount){
        log.info("开始同步物料主数据");
        Date startDate = new Date();
        QueryCondition queryCondition = QueryCondition.build(SupplierChainConstants.MATERIAL_SYNC)
                .select("FMATERIALID,FName,FNumber,FSpecification,FBARCODE,FMaterialGroup,FMaterialGroup.fname,F_MS_WLFL.fnumber,F_MS_WLFL,F_MS_WLFL.fdatavalue,FErpClsID,FSuite,FBaseUnitId.fname,FTaxRateId.fname,FCategoryID.fname,FTaxType.fdatavalue,FPerUnitStandHour,FIssueType,F_MS_PP.fdatavalue,FForbidStatus")
                .eq("FDocumentStatus", MaterialSyncEnums.HAS_VERIFY.getCode())
                .orderByAsc("FMATERIALID");
        segmentedQueryManager.doWhile(queryCondition, SYNC_MATERIAL_INFO_DATE_KEY, "FApproveDate", timeSyncCount, MaterialInfoDTO.class, dataList -> {
            dataList.forEach(materialInfoDTO -> {
                materialInfoDTO.setProperty(k3ERPEnumService.getEnumValue(MATERIAL_PROPERTY_ENUM_KEY, materialInfoDTO.getProperty()));
                materialInfoDTO.setIssueType(k3ERPEnumService.getEnumValue(MATERIAL_ISSUE_TYPE_ENUM_KEY, materialInfoDTO.getIssueType()));
                materialInfoDTO.setSuite(k3ERPEnumService.getEnumValue(MATERIAL_SUITE_ENUM_KEY, materialInfoDTO.getSuite()));
            });
            materialFeignClient.syncMaterialDataSaveOrUpdate(dataList);
        });
        k3ERPEnumService.evictK3ErpCache();
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap(BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        syncDataTimeMap.fastPut(SYNC_MATERIAL_INFO_DATE_KEY, startDate);
        log.info("物料主数据同步完毕");
    }

    /**
     * 同步物料分组 数据量很小 每次都全量同步
     */
    public void syncMaterialGroupData(){
        log.info("开始同步物料分组数据");
        GroupQueryParam queryParam = new GroupQueryParam();
        queryParam.setFormId(SupplierChainConstants.MATERIAL_SYNC).setGroupFieldKey("FMaterialGroup");
        try {
            ResponseResult<JSONObject> result = k3CloudApiClient.queryGroupInfo(queryParam);
            List<JSONObject> jsonObjects = result.getResult().getNeedReturnData();
            List<MaterialGroupInfoDTO> materialGroupInfoDTOS = new ArrayList<>();
            jsonObjects.forEach(jsonObject -> {
                MaterialGroupInfoDTO materialGroupInfoDTO = new MaterialGroupInfoDTO();
                materialGroupInfoDTO.setErpId(jsonObject.getLong("FID"))
                        .setErpParentId(jsonObject.getLong("FPARENTID"))
                        .setErpNumber(jsonObject.getString("FNUMBER"))
                        .setErpGroupId(jsonObject.getString("FGROUPID"))
                        .setErpNullParentId(jsonObject.getString("FFULLPARENTID"))
                        .setErpLeft(jsonObject.getString("FLEFT"))
                        .setErpRight(jsonObject.getString("FRIGHT"))
                        .setErpName(jsonObject.getString("FNAME"));
                materialGroupInfoDTOS.add(materialGroupInfoDTO);
            });
            materialGroupFeignClient.syncMaterialGroupDataSaveOrUpdate(materialGroupInfoDTOS);
            log.info("[同步物料分组] 同步完成...");
        } catch (Exception e) {
            log.error("[同步物料分组] error!",e);
        }
    }
}
