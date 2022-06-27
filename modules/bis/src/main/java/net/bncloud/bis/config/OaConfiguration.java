package net.bncloud.bis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oa")
public class OaConfiguration {

    private String domain;
    private ProxyServer proxyServer = new ProxyServer();

    @Data
    public static class ProxyServer{
        private String ip;
        private Integer port;
    }


}
