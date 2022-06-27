package net.bncloud.security.data.config;

import net.bncloud.common.security.data.DataSubjectHolder;
import net.bncloud.common.security.data.DesensitizeFieldHolder;
import net.bncloud.common.security.data.GrantDataHolder;
import net.bncloud.common.web.jackson.LongToStringSerializer;
import net.bncloud.security.data.DataSecurityMybatisInterceptor;
import net.bncloud.security.serialize.BigDecimalSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */

@Configuration
@ConditionalOnBean(StringRedisTemplate.class)
@EnableConfigurationProperties({DataSecurityProperties.class})
public class DataSecurityAutoConfiguration {

    @Resource
    private DataSecurityProperties dataSecurityProperties;


    @Bean
    public DataSecurityMybatisInterceptor dataSecurityMybatisInterceptor(DataSubjectHolder dataSubjectHolder,
                                                                         GrantDataHolder grantDataHolder) {
        String appCode = dataSecurityProperties.getAppCode();
        if (StringUtils.isBlank(appCode)) {
            throw new IllegalStateException("应用app-code不能为空");
        }

        return new DataSecurityMybatisInterceptor(appCode, dataSubjectHolder, grantDataHolder);
    }

    @Bean
    public DataSubjectHolder dataSubjectHolder(StringRedisTemplate stringRedisTemplate) {
        return new DataSubjectHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }

    @Bean
    public GrantDataHolder grantDataHolder(StringRedisTemplate stringRedisTemplate) {
        return new GrantDataHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }

    @Bean
    public DesensitizeFieldHolder DesensitizeFieldHolder(StringRedisTemplate stringRedisTemplate) {
        return new DesensitizeFieldHolder(dataSecurityProperties.getCacheKeyPrefix(), stringRedisTemplate);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizerMp() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, BigDecimalSerializer.instance);
        };
    }
}
