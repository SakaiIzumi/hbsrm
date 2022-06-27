package net.bncloud.service.api.file.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.service.api.file")
@EnableFeignClients(basePackages = {"net.bncloud.service.api.file"})
@Import(FeignClientsConfiguration.class)
public class FinancialFeignConfiguration {


}
