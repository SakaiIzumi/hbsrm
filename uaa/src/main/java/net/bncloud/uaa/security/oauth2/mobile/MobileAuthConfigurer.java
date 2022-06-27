package net.bncloud.uaa.security.oauth2.mobile;

import net.bncloud.uaa.security.oauth2.BncCustomSecurityConfigurer;
import net.bncloud.uaa.service.LoginUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class MobileAuthConfigurer extends BncCustomSecurityConfigurer {

    private final LoginUserDetailsService loginUserDetailsService;

    public MobileAuthConfigurer(LoginUserDetailsService loginUserDetailsService) {
        this.loginUserDetailsService = loginUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        MobilePasswordAuthenticationProvider provider = new MobilePasswordAuthenticationProvider();
        provider.setLoginUserDetailsService(loginUserDetailsService);
        http.authenticationProvider(provider);
    }
}
