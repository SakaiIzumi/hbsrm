package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;


import net.bncloud.information.feign.ZcContractServiceFeignClient;
@Component
public class ZcContractClientFallbackFactory implements FallbackFactory<ZcContractServiceFeignClient> {
    @Override
    public ZcContractServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());


        return new ZcContractServiceFeignClient() {

            @Override
            public R count() {
                R r = new R();r.setSuccess(false);
                return r;
            }
        };
    }
}
