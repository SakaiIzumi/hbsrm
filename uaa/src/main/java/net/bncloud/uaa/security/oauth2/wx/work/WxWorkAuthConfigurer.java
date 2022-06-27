package net.bncloud.uaa.security.oauth2.wx.work;

import net.bncloud.uaa.security.oauth2.BncCustomSecurityConfigurer;
import net.bncloud.uaa.service.LoginUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WxWorkAuthConfigurer extends BncCustomSecurityConfigurer {

    private final LoginUserDetailsService loginUserDetailsService;

    public WxWorkAuthConfigurer(LoginUserDetailsService loginUserDetailsService) {
        this.loginUserDetailsService = loginUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        WxWorkAuthenticationProvider provider = new WxWorkAuthenticationProvider();
        provider.setLoginUserDetailsService(loginUserDetailsService);
        http.authenticationProvider(provider);
    }
}
