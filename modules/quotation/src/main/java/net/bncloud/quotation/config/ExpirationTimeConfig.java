package net.bncloud.quotation.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "time")
@Data
public class ExpirationTimeConfig {
    private Long expirationTime;//15*60*1000

    public Long getExpirationTime() {
        return expirationTime*60*1000;
    }
}
