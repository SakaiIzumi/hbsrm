package net.bncloud.security.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.client.authorization")
public class ClientAuthorizationProperties {
    private String accessTokenUri;
    private String tokenServiceId;
    private String clientId;
    private String clientSecret;

    public String getAccessTokenUri() {
        return this.accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getTokenServiceId() {
        return this.tokenServiceId;
    }

    public void setTokenServiceId(String tokenServiceId) {
        this.tokenServiceId = tokenServiceId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
