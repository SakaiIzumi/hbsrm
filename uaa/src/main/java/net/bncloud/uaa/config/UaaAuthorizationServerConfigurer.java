package net.bncloud.uaa.config;

import net.bncloud.security.config.SecurityConfiguration;
import net.bncloud.uaa.security.UaaClientDetailsService;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;


@Configuration
@AutoConfigureAfter(SecurityConfiguration.class)
@EnableAuthorizationServer
public class UaaAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final TokenStore tokenStore;

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Resource(name = "authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    public UaaAuthorizationServerConfigurer(UserDetailsService userDetailsService,
                                            DataSource dataSource,
                                            TokenStore tokenStore) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new UaaClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //pick up all  TokenEnhancers incl. those defined in the application
        //this avoids changes to this class if an application wants to add its own to the chain
        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false);             //don't reuse or we will run into session inactivity timeouts
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }


}
