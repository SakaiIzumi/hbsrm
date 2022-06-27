package net.bncloud.security.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

public class DefaultSecurityExceptionHandlerConfig {

    @Bean
    public BncDefaultWebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new BncDefaultWebResponseExceptionTranslator();
    }

    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler(BncDefaultWebResponseExceptionTranslator exceptionTranslator) {
        OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler = new OAuth2AccessDeniedHandler();
        oAuth2AccessDeniedHandler.setExceptionTranslator(exceptionTranslator);
        return oAuth2AccessDeniedHandler;
    }

    @Bean
    public OAuth2AuthenticationEntryPoint authenticationEntryPoint(BncDefaultWebResponseExceptionTranslator exceptionTranslator) {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(exceptionTranslator);
        return authenticationEntryPoint;
    }
}
