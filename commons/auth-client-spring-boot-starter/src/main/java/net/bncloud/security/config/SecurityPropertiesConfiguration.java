package net.bncloud.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityPropertiesConfiguration {

    @Bean
    @ConfigurationProperties(prefix="bnc.security.resource")
    public BncSecurityResourceProperties bncSecurityResourceProperties(){
        return new BncSecurityResourceProperties();
    }
}
