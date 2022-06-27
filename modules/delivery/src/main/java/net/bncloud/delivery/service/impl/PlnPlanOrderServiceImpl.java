package net.bncloud.delivery.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.exception.Asserts;
import net.bncloud.delivery.entity.PlnPlanOrder;
import net.bncloud.delivery.mapper.PlnPlanOrderMapper;
import net.bncloud.delivery.service.PlnPlanOrderService;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@Slf4j
@Service
public class PlnPlanOrderServiceImpl extends BaseServiceImpl<PlnPlanOrderMapper, PlnPlanOrder> implements PlnPlanOrderService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePlanOrderData(List<PlanOrderDto> planOrderDtoList) {

        // 来源Id集合
        Set<Long> fidSet = planOrderDtoList.stream().map(PlanOrderDto::getFid).collect(Collectors.toSet());
        List<PlnPlanOrder> fidSetPlnPlanOrderList = this.list(Wrappers.<PlnPlanOrder>lambdaQuery().select(PlnPlanOrder::getId,PlnPlanOrder::getSourceId).in(PlnPlanOrder::getSourceId, fidSet));
        Map<Long, Long> sourceIdPlanOrderIdMap = fidSetPlnPlanOrderList.stream().collect(Collectors.toMap(PlnPlanOrder::getSourceId, PlnPlanOrder::getId));

        // 保存或更新
        planOrderDtoList.forEach( planOrderDto -> {

            PlnPlanOrder plnPlanOrder = new PlnPlanOrder().setSourceId(planOrderDto.getFid())
                    .setBillNo(planOrderDto.getFBillNo())
                    .setDocumentStatus(planOrderDto.getFDocumentStatus())
                    .setBillTypeId(planOrderDto.getFBillTypeId())
                    .setPurchaserCode(planOrderDto.getFSupplyOrgId())
                    .setPurchaserCodeOrg(planOrderDto.getFInStockOrgId())
                    .setSupplierCode(planOrderDto.getFAbcTest())
                    .setMaterialIdChild(planOrderDto.getFMaterialId())
                    .setSpecification(planOrderDto.getFSpecification())
                    .setUnitId(planOrderDto.getFUnitId())
                    .setPlanFinishDate(planOrderDto.getFPlanFinishDate())
                    .setOrderQty(planOrderDto.getFOrderQty())
                    .setComputerNo( planOrderDto.getFComputerNo() )
                    .setApproveDate( planOrderDto.getFApproveDate() )
                    ;

            // 设置主键ID
            if ( sourceIdPlanOrderIdMap.containsKey( planOrderDto.getFid() )) {
                plnPlanOrder.setId( sourceIdPlanOrderIdMap.get( planOrderDto.getFid() ) );
            }

            try {
                boolean flag = this.saveOrUpdate(plnPlanOrder);
                Asserts.isTrue( flag,"保存" );
            } catch (Exception ex){
                log.error("[savePlanOrderData] 处理某条数据失败！要保存或更新的数据 plnPlanOrder:[{}] ；", JSON.toJSONString( plnPlanOrder),ex);
            }

        });


    }

    @Override
    public List<PlnPlanOrder> getNewestResult(String mrpPlanOrderComputerNo) {
        return this.list( Wrappers.<PlnPlanOrder>lambdaQuery().eq(PlnPlanOrder::getComputerNo,mrpPlanOrderComputerNo ) );

    }
}
