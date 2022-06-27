package net.bncloud.logging.config;

import net.bncloud.logging.LogConstants;
import net.bncloud.logging.service.LoginLogService;
import net.bncloud.logging.service.SysLogService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class LoggingRedisMessageListenerConfig {

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                            @Qualifier("sysLogListenerAdapter") MessageListenerAdapter sysLogListenerAdapter,
                                                                @Qualifier("loginLogListenerAdapter") MessageListenerAdapter loginLogListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(sysLogListenerAdapter, new PatternTopic(LogConstants.REDIS_CHANNEL_SYS_LOG));
        container.addMessageListener(loginLogListenerAdapter, new PatternTopic(LogConstants.REDIS_CHANNEL_LOGIN_LOG));
        return container;
    }

    @Bean(name = "sysLogListenerAdapter")
    MessageListenerAdapter sysLogListenerAdapter(SysLogService receiverService) {
        return new MessageListenerAdapter(receiverService, "receiveSysLog");
    }

    @Bean(name = "loginLogListenerAdapter")
    MessageListenerAdapter loginLogListenerAdapter(LoginLogService loginLogService) {
        return new MessageListenerAdapter(loginLogService, "receiveLoginLog");
    }
}
