package net.bncloud.delivery.service;

import net.bncloud.base.BaseService;
import net.bncloud.delivery.entity.PlnPlanOrder;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/30
 **/
public interface PlnPlanOrderService extends BaseService<PlnPlanOrder> {
    /**
     * 保存计划订单数据
     * @param planOrderDtoList
     */
    void savePlanOrderData(List<PlanOrderDto> planOrderDtoList);

    /**
     * 查询最新一版的结果
     * @return
     * @param mrpPlanOrderComputerNo
     */
    List<PlnPlanOrder> getNewestResult(String mrpPlanOrderComputerNo);

}
