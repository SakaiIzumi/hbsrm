package net.bncloud.saas.config;

import net.bncloud.common.security.data.DataSubjectHolder;
import net.bncloud.common.security.data.DesensitizeFieldHolder;
import net.bncloud.common.security.data.GrantDataHolder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */

@Configuration
@EnableConfigurationProperties({DataSecurityProperties.class})
public class DataSecurityAutoConfiguration {

    @Resource
    private DataSecurityProperties dataSecurityProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public DataSubjectHolder dataSubjectHolder() {
        return new DataSubjectHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }

    @Bean
    public GrantDataHolder grantDataHolder() {
        return new GrantDataHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }
    @Bean
    public DesensitizeFieldHolder DesensitizeFieldHolder() {
        return new DesensitizeFieldHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }
}
