package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.ZcDeliverServiceFeignClient;
import net.bncloud.information.feign.ZcOrderServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ZcDeliverServiceFallbackFactory implements FallbackFactory<ZcDeliverServiceFeignClient> {



    @Override
    public ZcDeliverServiceFeignClient create(Throwable throwable) {
        return new ZcDeliverServiceFeignClient() {
            @Override
            public R getMsgCount() {
                R r = new R();r.setSuccess(false);
                return r;
            }


        };
    }
}
