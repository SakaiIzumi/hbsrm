package net.bncloud.uaa.security.oauth2.dingtalk;

import net.bncloud.uaa.security.oauth2.BncCustomSecurityConfigurer;
import net.bncloud.uaa.service.LoginUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class DingTalkQrAuthConfigurer extends BncCustomSecurityConfigurer {

    private final LoginUserDetailsService loginUserDetailsService;

    public DingTalkQrAuthConfigurer(LoginUserDetailsService loginUserDetailsService) {
        this.loginUserDetailsService = loginUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        DingTalkQrAuthenticationProvider provider = new DingTalkQrAuthenticationProvider();
        provider.setLoginUserDetailsService(loginUserDetailsService);
        http.authenticationProvider(provider);
    }
}
