package net.bncloud.financial.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Toby
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.password}")
    public String password;
    @Value("${spring.redis.host}")
    private String address;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    @ConditionalOnMissingBean({RedissonClient.class})
    public RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + address + ":" + port)
                .setPassword(password)
                .setRetryInterval(5000)
                .setTimeout(10000)
                .setConnectTimeout(10000);
        return Redisson.create(config);
    }

}
