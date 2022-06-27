package net.bncloud.service.api.platform.user.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.user.Mobile;
import net.bncloud.service.api.platform.user.dto.UserInfoDTO;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.user.feign.LoginUserServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserInfoFeignClientFallbackFactory implements FallbackFactory<LoginUserServiceFeignClient> {
    @Override
    public LoginUserServiceFeignClient create(Throwable throwable) {
        return new LoginUserServiceFeignClient() {
            @Override
            public R<UserInfoDTO> getUserByMobile(String mobile) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }

            @Override
            public void cacheLoginInfo(Mobile mobile) {

            }
        };
    }
}
