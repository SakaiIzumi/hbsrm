package net.bncloud.event.config;

import net.bncloud.event.publisher.DefaultEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {

    @Bean
    public DefaultEventPublisher eventPublisher() {
        return new DefaultEventPublisher();
    }
}
