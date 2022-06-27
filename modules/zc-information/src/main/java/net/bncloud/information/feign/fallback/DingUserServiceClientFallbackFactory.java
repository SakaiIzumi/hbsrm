package net.bncloud.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.information.feign.DingUserServiceFeignClient;
import net.bncloud.information.feign.ZyOrderServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class DingUserServiceClientFallbackFactory implements FallbackFactory<DingUserServiceFeignClient> {

    @Override
    public DingUserServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        return new DingUserServiceFeignClient() {
            @Override
            public R findAllDingUserIds() {
                R r = new R();
                r.setSuccess(false);
                return r;
            }
        };
    }
}
