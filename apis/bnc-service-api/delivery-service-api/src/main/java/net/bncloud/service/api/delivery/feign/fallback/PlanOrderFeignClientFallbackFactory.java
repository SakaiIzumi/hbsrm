package net.bncloud.service.api.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import net.bncloud.service.api.delivery.feign.PlanOrderFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@Slf4j
@Component
public class PlanOrderFeignClientFallbackFactory implements FallbackFactory<PlanOrderFeignClient> {
    @Override
    public PlanOrderFeignClient create(Throwable throwable) {
        log.error("[PlanOrderFeignClient] feign error!",throwable);
        return new PlanOrderFeignClient() {

            @Override
            public R<Object> syncPlanOrderData(List<PlanOrderDto> planOrderDtoList) {
                return R.remoteFail("Delivery");
            }
        };
    }
}
