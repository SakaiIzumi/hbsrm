package net.bncloud.logging.config;

import net.bncloud.logging.LogManager;
import net.bncloud.logging.aspect.SysLogAspect;
import net.bncloud.logging.extractor.LogContextExtractorManager;
import net.bncloud.logging.extractor.LogContextExtractor;
import net.bncloud.logging.support.RedisChannelLogManager;
import net.bncloud.logging.support.Slf4jLoggingManager;
import net.bncloud.logging.web.ClientInfoThreadLocalFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(LoggingProperties.class)
public class SysLogAutoConfiguration {

    @Resource
    private LoggingProperties loggingProperties;
    @Bean
    @ConditionalOnProperty(prefix = "application.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
    public SysLogAspect loggingAspect(ObjectProvider<LogContextExtractor> objectProvider,
                                      ObjectProvider<LogManager> logManagerObjectProvider) {
        List<LogContextExtractor> extractors = objectProvider.stream().collect(Collectors.toList());
        LogContextExtractorManager manager = new LogContextExtractorManager(extractors);
        List<LogManager> logManagers = logManagerObjectProvider.stream().collect(Collectors.toList());
        if (logManagers.isEmpty()) {
            logManagers.add(new Slf4jLoggingManager());
        }
        SysLogAspect aspect = new SysLogAspect(manager, logManagers);
        aspect.setFailOnLogFailures(!loggingProperties.isIgnoreLogFailures());
        return aspect;
    }

    @Bean
    public FilterRegistrationBean<ClientInfoThreadLocalFilter> clientInfoThreadLocalFilter() {

        FilterRegistrationBean<ClientInfoThreadLocalFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ClientInfoThreadLocalFilter());
        bean.setName("clientInfoThreadLocalFilter");
        bean.setUrlPatterns(Collections.singleton("/*"));
        bean.setAsyncSupported(true);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        Map<String, String> initParams = new HashMap<>();
        if (StringUtils.isNotBlank(loggingProperties.getAlternateClientAddrHeaderName())) {
            initParams.put(ClientInfoThreadLocalFilter.CONST_IP_ADDRESS_HEADER, loggingProperties.getAlternateClientAddrHeaderName());
        }
        if (StringUtils.isNotBlank(loggingProperties.getAlternateServerAddrHeaderName())) {
            initParams.put(ClientInfoThreadLocalFilter.CONST_SERVER_IP_ADDRESS_HEADER, loggingProperties.getAlternateServerAddrHeaderName());
        }
        initParams.put(ClientInfoThreadLocalFilter.CONST_USE_SERVER_HOST_ADDRESS, String.valueOf(loggingProperties.isUseServerHostAddress()));
        bean.setInitParameters(initParams);
        return bean;
    }

    @Configuration
    public static class LogManagerConfiguration {

        @Bean
        @ConditionalOnProperty(prefix = "application.logging.slf4j", name = "enabled", havingValue = "true", matchIfMissing = true)
        public Slf4jLoggingManager slf4jLoggingManager() {
            return new Slf4jLoggingManager();
        }

        @Bean
        @ConditionalOnProperty(prefix = "application.logging.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
        public RedisChannelLogManager redisChannelLogManager(StringRedisTemplate stringRedisTemplate) {
            RedisChannelLogManager logManager = new RedisChannelLogManager(stringRedisTemplate);
            return logManager;
        }
    }
}
