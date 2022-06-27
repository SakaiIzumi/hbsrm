package net.bncloud.saas.user.web;

import net.bncloud.api.feign.saas.user.Mobile;
import net.bncloud.service.api.platform.user.dto.UserInfoDTO;
import net.bncloud.common.api.R;
import net.bncloud.saas.user.service.LoginUserService;
import net.bncloud.service.api.platform.user.feign.LoginUserServiceFeignClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-center/login/user")
public class LoginUserServiceRestApi implements LoginUserServiceFeignClient {

    private final LoginUserService loginUserService;

    public LoginUserServiceRestApi(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    @GetMapping("/mobile")
    public R<UserInfoDTO> getUserByMobile(@RequestParam("mobile") String mobile) {
        return R.data(loginUserService.getUserInfoByMobile(mobile));
    }

    @Override
    @PostMapping("/cacheLoginInfo")
    public void cacheLoginInfo(@RequestBody Mobile mobile) {
        loginUserService.cacheLoginInfo(mobile);
    }
}
