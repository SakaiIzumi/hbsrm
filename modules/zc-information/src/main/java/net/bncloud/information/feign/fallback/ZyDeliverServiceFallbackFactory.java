package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.ZcDeliverServiceFeignClient;
import net.bncloud.information.feign.ZyDeliverServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ZyDeliverServiceFallbackFactory implements FallbackFactory<ZyDeliverServiceFeignClient> {



    @Override
    public ZyDeliverServiceFeignClient create(Throwable throwable) {
        return new ZyDeliverServiceFeignClient() {
            @Override
            public R getMsgCount() {
                R r = new R();r.setSuccess(false);
                return r;
            }


        };
    }
}
