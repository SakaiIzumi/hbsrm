package net.bncloud.event.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.event.feign.InformationClient;
import net.bncloud.event.feign.SaasClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

public class InformationClientFallbackFactory implements FallbackFactory<InformationClient> {
    @Override
    public InformationClient create(Throwable throwable) {
        return new InformationClient() {
            @PostMapping("/zc-information-msg/save")
            @Override
            public  R save(SendMsgParam sendMsgParam) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }
        };
    }
}
