package net.bncloud.security.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
@ConditionalOnClass({ClientCredentialsResourceDetails.class, LoadBalancerClient.class})
@EnableConfigurationProperties(ClientAuthorizationProperties.class)
@ConditionalOnProperty("security.client.authorization.client-id")
public class ClientAuthorizationAutoConfiguration {

    private final ClientAuthorizationProperties properties;

    public ClientAuthorizationAutoConfiguration(ClientAuthorizationProperties properties) {
        this.properties = properties;
    }

    /**
     * <p>loadBalancedResourceDetails.</p>
     *
     * @param loadBalancerClient a {@link LoadBalancerClient} object.
     */
    @Bean
    public LoadBalancedResourceDetails loadBalancedResourceDetails(LoadBalancerClient loadBalancerClient) {
        LoadBalancedResourceDetails loadBalancedResourceDetails = new LoadBalancedResourceDetails(loadBalancerClient);
        loadBalancedResourceDetails.setAccessTokenUri(properties.getAccessTokenUri());
        loadBalancedResourceDetails.setTokenServiceId(properties.getTokenServiceId());
        loadBalancedResourceDetails.setClientId(properties.getClientId());
        loadBalancedResourceDetails.setClientSecret(properties.getClientSecret());
        return loadBalancedResourceDetails;
    }
}
