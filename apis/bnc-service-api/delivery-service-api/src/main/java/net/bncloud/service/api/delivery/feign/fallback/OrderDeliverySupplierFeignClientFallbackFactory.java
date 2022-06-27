package net.bncloud.service.api.delivery.feign.fallback;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import net.bncloud.service.api.delivery.feign.OrderDeliverySupplierFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liyh
 */
@Component
@Slf4j
public class OrderDeliverySupplierFeignClientFallbackFactory implements FallbackFactory<OrderDeliverySupplierFeignClient> {
    @Override
    public OrderDeliverySupplierFeignClient create(Throwable throwable) {
        return new OrderDeliverySupplierFeignClient() {
            @Override
            public R<List<String>> selectOrderDeliverySupplierProductCode(Long supplierId) {
                return R.remoteFail();
            }
        };
    }
}
