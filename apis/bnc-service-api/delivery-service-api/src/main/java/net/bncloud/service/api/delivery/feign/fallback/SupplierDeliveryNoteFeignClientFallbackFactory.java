package net.bncloud.service.api.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.delivery.dto.DeliveryNoteDTO;
import net.bncloud.service.api.delivery.feign.SupplierDeliveryNoteFeignClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SupplierDeliveryNoteFeignClientFallbackFactory implements FallbackFactory<SupplierDeliveryNoteFeignClient> {
    @Override
    public SupplierDeliveryNoteFeignClient create(Throwable throwable) {
        return new SupplierDeliveryNoteFeignClient() {

            @Override
            public R<DeliveryNoteDTO> getDeliveryNoteNo(String fNumber) {
                log.error("对账模块同步结算池获取送货单号失败",throwable);
                return R.fail("收发货服务暂时不可用，对账模块同步结算池获取送货单号失败，请稍后再试!");
            }
        };
    }
}
