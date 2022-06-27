package net.bncloud.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "bnc")
@PropertySources({
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
})
public class BncConfigProperties {

    private final CorsConfiguration cors = new CorsConfiguration();

    public CorsConfiguration getCors() {
        return cors;
    }
}
