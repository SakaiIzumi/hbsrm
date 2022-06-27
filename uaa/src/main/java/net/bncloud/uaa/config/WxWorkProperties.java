package net.bncloud.uaa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "wechat.work")
public class WxWorkProperties {

    private String corpId;
    private Integer agentId;
    private String secret;
    private String token;
    private String aesKey;
}
