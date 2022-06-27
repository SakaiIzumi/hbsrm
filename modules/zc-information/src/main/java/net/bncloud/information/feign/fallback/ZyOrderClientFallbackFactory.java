package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.ZcOrderServiceFeignClient;
import net.bncloud.information.feign.ZyOrderServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ZyOrderClientFallbackFactory implements FallbackFactory<ZyOrderServiceFeignClient> {
    @Override
    public ZyOrderServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        return new ZyOrderServiceFeignClient() {

            @Override
            public R getMsgCount() {
                R r = new R();r.setSuccess(false);
                return r;
            }


        };
    }
}
