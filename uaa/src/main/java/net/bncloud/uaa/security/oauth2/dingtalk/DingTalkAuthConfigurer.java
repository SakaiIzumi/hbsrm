package net.bncloud.uaa.security.oauth2.dingtalk;

import net.bncloud.uaa.security.oauth2.BncCustomSecurityConfigurer;
import net.bncloud.uaa.service.LoginUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class DingTalkAuthConfigurer extends BncCustomSecurityConfigurer {

    private final LoginUserDetailsService loginUserDetailsService;

    public DingTalkAuthConfigurer(LoginUserDetailsService loginUserDetailsService) {
        this.loginUserDetailsService = loginUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        DingTalkAuthenticationProvider provider = new DingTalkAuthenticationProvider();
        provider.setLoginUserDetailsService(loginUserDetailsService);
        http.authenticationProvider(provider);
    }
}
