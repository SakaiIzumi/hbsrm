package net.bncloud.saas.ding.web.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationConfig {
    private String corpId;
    private Long agentId;
    private String appKey;
    private String appSecret;
}
