package net.bncloud.service.api.information.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.service.api.information.feign.InformationSMSClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public class InformationSMSClientFallbackFactory implements FallbackFactory<InformationSMSClient> {
    @Override
    public InformationSMSClient create(Throwable throwable) {
        return new InformationSMSClient() {
            @PostMapping("/zc-information-sms/save")
            @Override
            public  R save(SendMsgParam sendMsgParam) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }
        };
    }
}
