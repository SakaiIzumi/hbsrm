package net.bncloud.common.config;

import net.bncloud.common.security.SpringSecurityAuditorAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class BncJpaConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditorAware.class)
    public SpringSecurityAuditorAware auditorAware() {
        return new SpringSecurityAuditorAware();
    }
}
