package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.ZcContractServiceFeignClient;
import net.bncloud.information.feign.ZcOrderServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ZcOrderClientFallbackFactory implements FallbackFactory<ZcOrderServiceFeignClient> {
    @Override
    public ZcOrderServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        return new ZcOrderServiceFeignClient() {

            @Override
            public R getMsgCount() {
                R r = new R();r.setSuccess(false);
                return r;
            }


        };
    }
}
