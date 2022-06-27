package net.bncloud.uaa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dingtalk")
public class DingTalkProperties {

    private String corpId;
    private String appKey;
    private String appSecret;
}
