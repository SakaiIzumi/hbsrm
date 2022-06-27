package net.bncloud.uaa.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

//@Configuration
//@EnableResourceServer
public class UaaResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Resource
    private AccessDeniedHandler accessDeniedHandler;
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    private final TokenStore tokenStore;

    private final CorsFilter corsFilter;

    public UaaResourceServerConfigurer(TokenStore tokenStore, CorsFilter corsFilter) {
        this.tokenStore = tokenStore;
        this.corsFilter = corsFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new ThirdOauth2RedirectFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/third/**").permitAll()
                .antMatchers("/api/test").permitAll()
                .antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("uaa").tokenStore(tokenStore)
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);;
    }
}
