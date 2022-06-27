package net.bncloud.security.config;

import net.bncloud.common.security.LoginInfoContextPersistenceFilter;
import net.bncloud.common.security.SecurityUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({SecurityPropertiesConfiguration.class})
public class SecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public RedisTokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    @Bean
    public LoginInfoContextPersistenceFilter loginInfoContextPersistenceFilter(StringRedisTemplate stringRedisTemplate) {
        return new LoginInfoContextPersistenceFilter(stringRedisTemplate);
    }


    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            if (authentication.isClientOnly()) {
                return accessToken;
            }
            final Map<String, Object> additionalInfo = new HashMap<>(4);

            SecurityUtils.getCurrentUser().ifPresent(userDetails -> {
                additionalInfo.put("id", userDetails.getId());
                additionalInfo.put("name", userDetails.getName());
                additionalInfo.put("mobile", userDetails.getMobile());
            });

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
