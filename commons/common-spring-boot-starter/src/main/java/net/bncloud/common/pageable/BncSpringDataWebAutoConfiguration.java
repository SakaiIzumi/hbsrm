package net.bncloud.common.pageable;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpringDataWebProperties.class)
@AutoConfigureBefore(SpringDataWebAutoConfiguration.class)
public class BncSpringDataWebAutoConfiguration {
    private final SpringDataWebProperties properties;

    public BncSpringDataWebAutoConfiguration(SpringDataWebProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return (resolver) -> {
            SpringDataWebProperties.Pageable pageable = this.properties.getPageable();
            resolver.setPageParameterName(pageable.getPageParameter());
            resolver.setSizeParameterName(pageable.getSizeParameter());
            resolver.setOneIndexedParameters(true);
            resolver.setPrefix(pageable.getPrefix());
            resolver.setQualifierDelimiter(pageable.getQualifierDelimiter());
            resolver.setFallbackPageable(PageRequest.of(0, pageable.getDefaultPageSize()));
            resolver.setMaxPageSize(pageable.getMaxPageSize());
        };
    }
}
