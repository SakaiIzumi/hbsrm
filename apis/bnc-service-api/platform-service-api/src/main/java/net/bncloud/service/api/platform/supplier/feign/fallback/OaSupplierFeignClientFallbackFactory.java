package net.bncloud.service.api.platform.supplier.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.service.api.platform.supplier.feign.OaSupplierFeignClient;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OaSupplierFeignClientFallbackFactory implements FallbackFactory<OaSupplierFeignClient> {

    @Override
    public OaSupplierFeignClient create(Throwable throwable) {
        return null;
    }
}
