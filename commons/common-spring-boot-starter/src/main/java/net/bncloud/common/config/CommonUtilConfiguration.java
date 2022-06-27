package net.bncloud.common.config;

import net.bncloud.common.util.ApplicationContextProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonUtilConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

}
