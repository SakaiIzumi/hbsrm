package net.bncloud.delivery.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.enums.DeliveryCfgParam;
import net.bncloud.delivery.enums.DeliveryPlanStatus;
import net.bncloud.delivery.service.DeliveryNoteService;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.service.DeliveryPlanService;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanEntity;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Toby
 */
@Service
@Slf4j
public class DeliveryPlanManager {
    private final DeliveryPlanService deliveryPlanService;
    private final DeliveryPlanDetailService deliveryPlanDetailService;

    private final DeliveryNoteService deliveryNoteService;

    private final DeliveryPlanDetailService planDetailService;


    private final CfgParamResourceFeignClient cfgParamResourceFeignClient;
    @Resource
    private NumberFactory numberFactory;

    @Resource
    @Lazy
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    public DeliveryPlanManager(DeliveryPlanService deliveryPlanService,
                               DeliveryPlanDetailService deliveryPlanDetailService,
                               DeliveryNoteService deliveryNoteService,
                               DeliveryPlanDetailService planDetailService,
                               CfgParamResourceFeignClient cfgParamResourceFeignClient) {
        this.deliveryPlanService = deliveryPlanService;
        this.deliveryPlanDetailService = deliveryPlanDetailService;
        this.deliveryNoteService = deliveryNoteService;
        this.planDetailService = planDetailService;
        this.cfgParamResourceFeignClient = cfgParamResourceFeignClient;
    }

    /**
     * 根据code查找所有组织下的协同配置
     *
     * @param cfgParam
     * @return
     */
    private R<List<CfgParamDTO>> getParamAllByCode(DeliveryCfgParam cfgParam) {
        R<List<CfgParamDTO>> cfgParamR;
        try {
            cfgParamR = cfgParamResourceFeignClient.findListByCode(cfgParam.getCode());
        } catch (Exception e) {
            log.error("查询是否自动发送送货计划接口请求失败");
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR);
        }
        if (R.isNotSuccess(cfgParamR)) {
            log.error("查询送货协同的协同配置接口请求失败");
            throw new ApiException(cfgParamR.getCode(), cfgParamR.getMsg());
        }
        return cfgParamR;
    }


    public static Function<DeliveryPlanEntity, String> distinctByKeyFunction() {
        return (DeliveryPlanEntity deliveryPlanEntity) -> deliveryPlanEntity.getPurchaseCode() + "-" + deliveryPlanEntity.getSupplierCode();
    }



    /**
     * 同步送货计划至智采
     *
     * @param deliveryPlanDTOList 送货计划列表
     */
    public void receiveDeliveryPlans(List<DeliveryPlanDTO> deliveryPlanDTOList) {

        if (CollectionUtil.isNotEmpty(deliveryPlanDTOList)) {

            //获取所有组织的（自动发送）协同配置
            List<CfgParamDTO> cfgParamDTOList = getParamAllByCode(DeliveryCfgParam.DELIVERY_AUTO_SEND).getData();

            ArrayList<DeliveryPlanEntity> deliveryPlanEntities = new ArrayList<>();
            deliveryPlanDTOList.forEach(dto -> {
                        //计划状态：草稿
                        dto.getDeliveryPlanEntity().setPlanStatus(DeliveryPlanStatus.DRAFT.getCode());
                        //计划编号
                        dto.getDeliveryPlanEntity().setPlanNo(numberFactory.buildNumber(NumberType.plan));
                        dto.getPlanDetailEntityList().forEach(detail -> {
                            //计划明细状态：（和计划状态一样）
                            detail.setStatus(DeliveryPlanStatus.DRAFT.getCode());
                            //计划数量=项次中送货数量的和
                            long planQuantity = detail.getPlanDetailItemList().stream().mapToLong(item -> Long.parseLong(item.getDeliveryQuantity())).sum();
                            detail.setPlanQuantity(planQuantity+"");

                        });
                        deliveryPlanEntities.add(dto.getDeliveryPlanEntity());
                    }
            );


            //根据采购方和供应商编码去重
            List<DeliveryPlanEntity> supplierAndCodeList = deliveryPlanEntities.stream().filter(CollectionUtil.distinctByKey(distinctByKeyFunction())).collect(Collectors.toList());
            List<OrgIdQuery> orgIdQueryList = BeanUtil.copy(supplierAndCodeList, OrgIdQuery.class);
            //根据采购方和供应商编码批量获取组织id
            log.info("采购方编码和供应编码的集合,{}",JSON.toJSONString(orgIdQueryList));
            R<List<OrgIdDTO>> infos = purchaserFeignClient.infos(orgIdQueryList);
            log.info("获取到的采购方和供应方的信息：{}",JSON.toJSONString(infos));

            if (infos.isSuccess()) {
                Map<String, String> purchaseMap = infos.getData().stream()
                        .filter(CollectionUtil.distinctByKey(OrgIdDTO::getPurchaseCode))
                        .collect(Collectors.toMap(OrgIdDTO::getPurchaseCode, OrgIdDTO::getPurchaseName));
                Map<String, String> supplierMap = infos.getData().stream()
                        .filter(CollectionUtil.distinctByKey(OrgIdDTO::getSupplierCode))
                        .collect(Collectors.toMap(OrgIdDTO::getSupplierCode, OrgIdDTO::getSupplierame));
                deliveryPlanDTOList.forEach(deliveryPlanDTO -> {
                    DeliveryPlanEntity deliveryPlanEntity = deliveryPlanDTO.getDeliveryPlanEntity();
                    //设值：采购方名称
                    deliveryPlanEntity.setPurchaseName(purchaseMap.get(deliveryPlanEntity.getPurchaseCode()));
                    deliveryPlanEntity.setSupplierName(supplierMap.get(deliveryPlanEntity.getSupplierCode()));
                    //设值：供应商名称
                    List<DeliveryPlanDetailEntity> detailEntityList = deliveryPlanDTO.getPlanDetailEntityList();
                    if (detailEntityList!=null && detailEntityList.size()>0){
                        detailEntityList.forEach(detail->{
                            detail.setSupplierName(supplierMap.get(detail.getSupplierCode()));
                        });
                    }
                });
            }


            //供应商去重
            List<DeliveryPlanEntity> disSupplierCode = deliveryPlanEntities.stream().filter(deliveryPlanEntity -> StringUtil.isNotBlank(deliveryPlanEntity.getSupplierCode()))
                    .filter(CollectionUtil.distinctByKey(DeliveryPlanEntity::getSupplierCode))
                    .collect(Collectors.toList());

            List<SupplierDTO> OaSupplierDTOList = new ArrayList<>();
            disSupplierCode.forEach(item -> {
                SupplierDTO oaSupplierDTO = new SupplierDTO();
                oaSupplierDTO.setCode(item.getSupplierCode());
                OaSupplierDTOList.add(oaSupplierDTO);
            });

            //获取供应商的合作状态
            R<List<SupplierDTO>> supplierByCode = supplierFeignClient.findSupplierByCode(OaSupplierDTOList);
            List<SupplierDTO> supplierList = new ArrayList<>();
            if (supplierByCode.isSuccess()) {
                supplierList = supplierByCode.getData();
                if (supplierList.isEmpty()) {
                    throw new ApiException(500, "供应商不存在");
                }
            }


            if (infos.isSuccess()) {
                List<OrgIdDTO> orgIdDTOList = infos.getData();
                for (OrgIdDTO orgIdDTO : orgIdDTOList) {
                    List<SupplierDTO> finalSupplierList = supplierList;
                    deliveryPlanDTOList.forEach(deliveryPlanDTO -> {
                        DeliveryPlanEntity deliveryPlanEntity = deliveryPlanDTO.getDeliveryPlanEntity();
                        //设值：采购方名称
                        // if (deliveryPlanEntity.getPurchaseCode().equals(orgIdDTO.getPurchaseCode())) {
                        //     deliveryPlanEntity.setPurchaseName(orgIdDTO.getPurchaseName());
                        // }
                        // //设值：供应商名称
                        // if (deliveryPlanEntity.getSupplierCode().equals(orgIdDTO.getSupplierCode())) {
                        //     deliveryPlanDTO.getPlanDetailEntityList().forEach(deliveryPlanDetailEntity -> {
                        //         //计划明细表：
                        //         //供应方编码
                        //         deliveryPlanDetailEntity.setSupplierCode(orgIdDTO.getSupplierCode());
                        //         //供应商名称
                        //         deliveryPlanDetailEntity.setSupplierName(orgIdDTO.getSupplierame());
                        //     });
                        // }


                        if (deliveryPlanEntity.getSupplierCode().equals(orgIdDTO.getSupplierCode()) && deliveryPlanEntity.getPurchaseCode().equals(orgIdDTO.getPurchaseCode())) {
                            //设置OrgId
                            deliveryPlanEntity.setOrgId(orgIdDTO.getOrgId());
                            SupplierDTO supplierDTO = finalSupplierList.stream().filter(supplier -> supplier.getCode().equals(deliveryPlanEntity.getSupplierCode())).findAny().orElse(null);

                            if (supplierDTO != null) {
                                //与供应商的合作关系:  冻结（不能自动发送）
                                if (!SupplierRelevanceStatusEnum.FROZEN.getCode().equals(supplierDTO.getRelevanceStatus())) {
                                    //该组织下的协同配置（是否自动发送送货计划）
                                    CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getOrgId())).findFirst().orElse(null);
                                    if (paramEntity != null) {
                                        //协同配置：自动发送送货计划
                                        if ("true".equals(paramEntity.getValue())) {
                                            //计划状态：待确认
                                            deliveryPlanEntity.setPlanStatus(DeliveryPlanStatus.TO_BE_CONFIRM.getCode());
                                            //发布人和发布时间
                                            deliveryPlanEntity.setPublishDate(LocalDateTime.now());
                                            deliveryPlanEntity.setPublisher("系统");
                                            //修改计划明细的状态（目前相当于计划状态）
                                            deliveryPlanDTO.getPlanDetailEntityList().forEach(detail -> detail.setStatus(DeliveryPlanStatus.TO_BE_CONFIRM.getCode()));
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }


            for (DeliveryPlanDTO deliveryPlanDTO : deliveryPlanDTOList) {
                try {
                    deliveryPlanService.receiveDeliveryPlan(deliveryPlanDTO);
                } catch (Exception e) {
                    log.warn("同步送货计划至智采,接收参数：{}", JSON.toJSONString(deliveryPlanDTO));
                    log.error("DeliveryPlanManager#receiveDeliveryPlans,同步送货计划至智采发生异常", e);
                }
            }
        }
    }


    /**
     * 批量更新收货单信息（收货数量及状态）
     *
     * @param deliveryNoteUpdateDTOList 送货单列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncErpDeliveryNoteInfos(List<DeliveryDetailUpdateDTO> deliveryNoteUpdateDTOList) {
        if (CollectionUtil.isNotEmpty(deliveryNoteUpdateDTOList)) {
            for (DeliveryDetailUpdateDTO noteUpdateDTO : deliveryNoteUpdateDTOList) {
                try {
                    deliveryNoteService.syncErpDeliveryNoteDetailInfo(noteUpdateDTO);
                } catch (Exception e) {
                    log.warn("同步送货计划至智采,接收参数：{}", JSON.toJSONString(noteUpdateDTO));
                    log.error("DeliveryPlanManager#syncErpDeliveryNoteInfos,批量更新收货单信息发生异常", e);
                }
            }
        }
    }

    /**
     * 更新送货计划组织ID
     *
     * @param syncOrgIdParams
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDeliveryPlanOrgId(SyncOrgIdParams syncOrgIdParams) {
        //{"orgId":112,"purchaseCode":"100","purchaseName":"美尚（广州)化妆品股份有限公司","supplierCodeList":["OEM0009"],"supplierCodeNameMap":{"OEM0009":"科丝美诗（中国）化妆品有限公司 "}}
        log.info("updateDeliveryPlanOrgId params: {}",JSON.toJSONString( syncOrgIdParams ));
        Optional.ofNullable(syncOrgIdParams.getOrgId())
                .flatMap(orgId -> Optional.ofNullable(syncOrgIdParams.getPurchaseCode()))
                .ifPresent(purchaseCode -> {
                    if (!CollectionUtil.isEmpty(syncOrgIdParams.getSupplierCodeList())) {

                        List<DeliveryPlan> willUpdateDeliveryPlanList = deliveryPlanService.lambdaQuery()
                                .select(DeliveryPlan::getId, DeliveryPlan::getPurchaseCode, DeliveryPlan::getSupplierCode)
                                .eq(DeliveryPlan::getPurchaseCode, syncOrgIdParams.getPurchaseCode())
                                .in(DeliveryPlan::getSupplierCode, syncOrgIdParams.getSupplierCodeList())
                                .isNull(DeliveryPlan::getOrgId)
                                .list();

                        if (CollectionUtil.isNotEmpty(willUpdateDeliveryPlanList)) {

                            Set<Long> deliveryPlanIdSet = willUpdateDeliveryPlanList.stream().map(DeliveryPlan::getId).collect(Collectors.toSet());
                            List<DeliveryPlanDetail> deliveryPlanDetailList = deliveryPlanDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery().select( DeliveryPlanDetail::getId,DeliveryPlanDetail::getSupplierCode ).in(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlanIdSet));

                            willUpdateDeliveryPlanList.forEach(deliveryPlan -> {
                                deliveryPlan.setOrgId(syncOrgIdParams.getOrgId());
                                deliveryPlan.setPurchaseName( syncOrgIdParams.getPurchaseName() );
                                deliveryPlan.setSupplierName(syncOrgIdParams.getSupplierCodeNameMap().get(deliveryPlan.getSupplierCode()));
                            });

                            // 离谱
                            Map<String, String> supplierCodeNameMap = syncOrgIdParams.getSupplierCodeNameMap();
                            List<DeliveryPlanDetail> deliveryPlanDetailUpdateRecordList = deliveryPlanDetailList.stream()
                                    .filter(deliveryPlanDetail -> supplierCodeNameMap.get(deliveryPlanDetail.getSupplierCode()) != null)
                                    .map(deliveryPlanDetail -> deliveryPlanDetail.setSupplierName(supplierCodeNameMap.get(deliveryPlanDetail.getSupplierCode())))
                                    .collect(Collectors.toList());

                            if(! CollectionUtils.isEmpty(willUpdateDeliveryPlanList )){
                                deliveryPlanService.saveOrUpdateBatch(willUpdateDeliveryPlanList);
                            }

                            if(! CollectionUtils.isEmpty( deliveryPlanDetailUpdateRecordList )){
                                deliveryPlanDetailService.saveOrUpdateBatch( deliveryPlanDetailList );
                            }
                        }

                    }
                });
    }
}
