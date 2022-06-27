package net.bncloud.uaa.config;

import net.bncloud.security.annotation.EnableBncResourceServer;
import net.bncloud.uaa.security.oauth2.BncCustomSecurityConfigurer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableBncResourceServer
public class UaaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final List<BncCustomSecurityConfigurer> customSecurityConfigurers;

    public UaaWebSecurityConfiguration(UserDetailsService userDetailsService,
                                       AuthenticationManagerBuilder authenticationManagerBuilder,
                                       PasswordEncoder passwordEncoder,
                                       List<BncCustomSecurityConfigurer> customSecurityConfigurers) {
        this.userDetailsService = userDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.customSecurityConfigurers = customSecurityConfigurers;
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .authenticationProvider(authenticationProvider());
    }*/
    @PostConstruct
    public void init() throws Exception {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//            .antMatchers(HttpMethod.OPTIONS, "/**")
//            .antMatchers("/app/**/*.{js,html}")
//            .antMatchers("/i18n/**")
//            .antMatchers("/content/**")
//            .antMatchers("/swagger-ui/index.html")
//            .antMatchers("/test/**")
//            .antMatchers("/h2-console/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new ThirdOauth2RedirectFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests()
                .antMatchers("/rsa/publicKey", "/auth/logout").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/auth/mobile").permitAll()
                .antMatchers("/auth/loginForm").permitAll()
                .antMatchers("/auth/refresh_token").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
//        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        /*ClassLoader classLoader = this.context.getClassLoader();
        List<BncCustomSecurityConfigurer> customSecurityConfigurers =
                SpringFactoriesLoader.loadFactories(BncCustomSecurityConfigurer.class, classLoader);*/
        for (BncCustomSecurityConfigurer configurer : customSecurityConfigurers) {
            http.apply(configurer);
        }
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


}
