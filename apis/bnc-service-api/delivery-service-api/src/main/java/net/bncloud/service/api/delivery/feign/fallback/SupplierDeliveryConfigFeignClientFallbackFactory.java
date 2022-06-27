package net.bncloud.service.api.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.delivery.feign.SupplierDeliveryConfigFeignClient;
import net.bncloud.service.api.delivery.vo.SupplierDeliveryConfigVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Slf4j
@Component
public class SupplierDeliveryConfigFeignClientFallbackFactory implements FallbackFactory<SupplierDeliveryConfigFeignClient> {
    @Override
    public SupplierDeliveryConfigFeignClient create(Throwable throwable) {
        log.error("SupplierDeliveryConfigFeignClient feign error!",throwable);
        return new SupplierDeliveryConfigFeignClient() {

            @Override
            public R<List<SupplierDeliveryConfigVo>> getSupplierDeliveryConfigListBySupplierCodeList(List<String> supplierCodeList) {
                return R.remoteFail();
            }
        };
    }
}
