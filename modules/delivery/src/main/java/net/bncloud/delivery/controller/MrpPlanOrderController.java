package net.bncloud.delivery.controller;

import net.bncloud.common.api.R;
import net.bncloud.delivery.manager.PlanOrderManager;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import net.bncloud.service.api.delivery.feign.PlanOrderFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * desc: 计划订单
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@RestController
@RequestMapping
public class MrpPlanOrderController implements PlanOrderFeignClient {

    @Resource
    private PlanOrderManager planOrderManager;

    @Override
    public R<Object> syncPlanOrderData(@RequestBody List<PlanOrderDto> planOrderDtoList) {

        planOrderManager.handlePlanOrderData( planOrderDtoList);

        return R.success();
    }
}
