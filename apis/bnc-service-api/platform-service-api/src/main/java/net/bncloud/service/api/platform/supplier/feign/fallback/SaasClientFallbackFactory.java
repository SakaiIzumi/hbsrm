package net.bncloud.service.api.platform.supplier.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.supplier.feign.SaasClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Component
public class SaasClientFallbackFactory implements FallbackFactory<SaasClient> {
    @Override
    public SaasClient create(Throwable throwable) {
        return new SaasClient() {
            @PostMapping("/user-center/users/getUserInfoByUid")
            @Override
            public R<Map<String,Object>> getUserInfoByUid(String uid) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }
        };
    }
}
