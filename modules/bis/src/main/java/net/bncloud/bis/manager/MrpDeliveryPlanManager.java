package net.bncloud.bis.manager;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.model.entity.ExperimentSupplier;
import net.bncloud.bis.model.vo.PlnPlanOrderVo;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.msk3cloud.constant.enums.DocumentStatus;
import net.bncloud.msk3cloud.constant.enums.PlanOrderLaunchBillTypeId;
import net.bncloud.msk3cloud.constant.formid.ManufacturingConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import net.bncloud.service.api.delivery.feign.PlanOrderFeignClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * desc: mrp送货计划
 *
 * @author Rao
 * @Date 2022/05/23
 **/
@Slf4j
@Component
public class MrpDeliveryPlanManager extends AbstractSyncManager {

    @Resource
    private PlanOrderFeignClient planOrderFeignClient;



    /**
     * 获取需要同步的 billNo
     * @param experimentSupplierCodeList 试点供应商编码集合
     * @param computerNo
     * @return
     */
    private List<String> getAllBillNoListWithSupplierCode(List<String> experimentSupplierCodeList, String computerNo) {
        Set<String> allBillNoSet = new HashSet<>();
        QueryCondition queryCondition = QueryCondition.build( ManufacturingConstants.PLN_PLAN_ORDER)
                .select("FBillNo")
                // 单据状态 已审核
                .eq("FDocumentStatus", DocumentStatus.C.name() )
                // 关闭状态 未关闭 需要其他状态枚举找产品
//                .eq("FCloseStatus", "A")
                // 单据类型  标准采购申请
                .eq("FReleaseBillType.fnumber", PlanOrderLaunchBillTypeId.CGSQD01_SYS.name() )
                // 限制供应商
                .in( "F_ABC_TEST.fnumber",experimentSupplierCodeList )
                // 同步这个运算编号的数据
                .eq("FComputerNo",computerNo)
                ;

        segmentedQueryManager.whileHandle( queryCondition,QueryCondition.MAX_PAGE_LIMIT, PlnPlanOrderVo.class,plnPlanOrderVoList -> {
            Set<String> billNoSet = plnPlanOrderVoList.stream().map(PlnPlanOrderVo::getFBillNo).collect(Collectors.toSet());
            allBillNoSet.addAll( billNoSet );
        });

        return new ArrayList<>(allBillNoSet);
    }

    /**
     * 同步计划订单
     * @param experimentSupplierList
     * @param computerNo
     * @return
     */
    public R<Object> syncPlanOrder(List<ExperimentSupplier> experimentSupplierList, String computerNo) {

        Asserts.isTrue( CollectionUtil.isNotEmpty( experimentSupplierList),"无试点供应商！" );

        List<String> experimentSupplierCodeList = experimentSupplierList.stream().map(ExperimentSupplier::getSupplierCode).collect(Collectors.toList());

        List<String> allBillNoList = this.getAllBillNoListWithSupplierCode( experimentSupplierCodeList,computerNo );

        Class<PlnPlanOrderVo> plnPlanOrderVoClass = PlnPlanOrderVo.class;
        List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList( plnPlanOrderVoClass);

        // 查询到所有数据再统一往下推  由于计划订单的数据设计到统一运算
        List<List<String>> allBillNoListSplit = CollUtil.split( allBillNoList, 100);
        Optional<List<PlanOrderDto>> planOrderDtoListOpt = allBillNoListSplit.stream().map( billNoList -> {
            try {
                QueryCondition queryCondition = QueryCondition.build(ManufacturingConstants.PLN_PLAN_ORDER)
                        .select(fieldKeyList)
                        .in("FBillNo", billNoList)
                        .page(1, QueryCondition.MAX_PAGE_LIMIT);

                List<PlnPlanOrderVo> planOrderVoList = k3cloudRemoteService.documentQueryWithClass(queryCondition.queryParam(), plnPlanOrderVoClass);
                if (CollectionUtil.isEmpty(planOrderVoList)) {
                    log.info("[experimentSupplierSyncPlanOrder] billNoList:{}",billNoList);
                    return new ArrayList<PlanOrderDto>();
                }

                return BeanUtil.copy(planOrderVoList, PlanOrderDto.class);

            } catch (Exception ex) {
                log.error("[计划订单同步] billNoList:{}, error!", billNoList,ex);
                return new ArrayList<PlanOrderDto>();
            }
        }).reduce((o1, o2) -> {
            o1.addAll(o2);
            return o1;
        });

        if (! planOrderDtoListOpt.isPresent()) {
            return R.fail("同步ERP计划订单数据集合空！");
        }
        List<PlanOrderDto> planOrderDtoList = planOrderDtoListOpt.get();

        // 对接送货计划
        R<Object> planOrderDataR = planOrderFeignClient.syncPlanOrderData( planOrderDtoList );
        Asserts.isTrue( planOrderDataR.isSuccess(),planOrderDataR.getMsg() );

        return R.success();

    }
}
