package net.bncloud.service.api.platform.supplier.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.supplier.feign.fallback.SaasClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@AuthorizedFeignClient(name = "platform", contextId = "platformEventClient", fallbackFactory = SaasClientFallbackFactory.class, decode404 = true)
public interface SaasClient {

    @PostMapping("/user-center/users/getUserInfoByUid")
    R<Map<String,Object>> getUserInfoByUid(@RequestParam("uid") String uid);

}
