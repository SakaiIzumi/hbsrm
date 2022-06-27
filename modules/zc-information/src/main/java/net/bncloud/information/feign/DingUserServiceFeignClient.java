package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.DingUserServiceClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@AuthorizedFeignClient(name = "platform", contextId = "DingUserServiceFeignClient", fallbackFactory = DingUserServiceClientFallbackFactory.class, decode404 = true)
public interface DingUserServiceFeignClient {

    @GetMapping("/ding-talk/user/app/findAllDingUserIds")
    R findAllDingUserIds();


}
