package net.bncloud.service.api.platform.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.service.api.platform")
@EnableFeignClients(basePackages = {"net.bncloud.service.api.platform"})
@Import(FeignClientsConfiguration.class)
public class PlatformFeignConfiguration {


}
