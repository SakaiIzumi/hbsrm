package net.bncloud.oem.service.api.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.oem")
@EnableFeignClients(basePackages = {"net.bncloud.oem.service.api"})
@Import(FeignClientsConfiguration.class)
public class OemFeignConfiguration {


}
