package net.bncloud.service.api.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.information.feign.fallback.InformationSMSClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AuthorizedFeignClient(name = "zc-information", contextId = "informationSMSClientForApi", fallbackFactory = InformationSMSClientFallbackFactory.class, decode404 = true)
public interface InformationSMSClient {

    @PostMapping("/zc-information-sms/save")
    R save(@RequestBody SendMsgParam sendMsgParam);



}
