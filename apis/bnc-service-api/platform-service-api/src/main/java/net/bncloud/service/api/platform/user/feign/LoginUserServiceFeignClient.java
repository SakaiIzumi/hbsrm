package net.bncloud.service.api.platform.user.feign;

import net.bncloud.api.feign.saas.user.Mobile;
import net.bncloud.service.api.platform.user.dto.UserInfoDTO;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.user.feign.fallback.UserInfoFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@AuthorizedFeignClient(name = "platform", contextId = "loginUserServiceFeignClient", fallbackFactory = UserInfoFeignClientFallbackFactory.class, decode404 = true)
public interface LoginUserServiceFeignClient {
    @GetMapping(value = "/user-center/login/user/mobile")
    R<UserInfoDTO> getUserByMobile(@RequestParam("mobile") String mobile);

    @PostMapping(value = "/user-center/login/user/cacheLoginInfo")
    void cacheLoginInfo(@RequestBody Mobile mobile);
}
