package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PurchaseOrderFeignClientFallbackFactory implements FallbackFactory<PurchaseOrderFeignClient> {

    @Override
    public PurchaseOrderFeignClient create(Throwable throwable) {
        return new PurchaseOrderFeignClient() {
            @Override
            public R<Object> syncData() {
                log.error("feign error!",throwable);
                return R.fail("数据服务不可用，请稍后重试！");
            }

            @Override
            public R<Object> syncOrder() {
                log.error("feign error!",throwable);
                return R.fail("数据服务不可用，请稍后重试！");
            }

            @Override
            public R<Object> syncDeliveryPlan() {
                log.error("feign error!",throwable);
                return R.fail("数据服务不可用，请稍后重试！");
            }
        };
    }
}
