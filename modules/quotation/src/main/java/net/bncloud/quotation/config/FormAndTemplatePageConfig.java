package net.bncloud.quotation.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "strip")
@Data
public class FormAndTemplatePageConfig {
    private Integer defaultPageSize;

}
