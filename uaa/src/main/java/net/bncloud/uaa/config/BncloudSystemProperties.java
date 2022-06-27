package net.bncloud.uaa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "bnc.system")
public class BncloudSystemProperties {
    private String baseUrl;

    private String thirdAuth;
}
