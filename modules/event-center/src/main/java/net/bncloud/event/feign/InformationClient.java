package net.bncloud.event.feign;

import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.event.feign.fallback.InformationClientFallbackFactory;
import net.bncloud.event.feign.fallback.SaasClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@AuthorizedFeignClient(name = "zc-information", contextId = "informationClient", fallbackFactory = InformationClientFallbackFactory.class, decode404 = true)
public interface InformationClient {

    @PostMapping("/zc-information-msg/save")
    R save(@RequestBody SendMsgParam sendMsgParam);
}
