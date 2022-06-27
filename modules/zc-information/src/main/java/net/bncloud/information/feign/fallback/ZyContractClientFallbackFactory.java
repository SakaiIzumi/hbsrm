package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.ZcContractServiceFeignClient;
import net.bncloud.information.feign.ZyContractServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ZyContractClientFallbackFactory implements FallbackFactory<ZyContractServiceFeignClient> {
    @Override
    public ZyContractServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());


        return new ZyContractServiceFeignClient() {

            @Override
            public R count() {
                R r = new R();r.setSuccess(false);
                return r;
            }
        };
    }
}
