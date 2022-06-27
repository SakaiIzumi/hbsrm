package net.bncloud.service.api.delivery.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import net.bncloud.service.api.delivery.feign.fallback.PlanOrderFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * desc: 计划订单同步
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@AuthorizedFeignClient(name = "delivery",path = "delivery",fallbackFactory = PlanOrderFeignClientFallbackFactory.class)
public interface PlanOrderFeignClient {

    /**
     * 同步计划订单数据
     * @param planOrderDtoList
     * @return
     */
    @PostMapping("syncPlanOrderData")
    R<Object> syncPlanOrderData(@RequestBody List<PlanOrderDto> planOrderDtoList);

}
