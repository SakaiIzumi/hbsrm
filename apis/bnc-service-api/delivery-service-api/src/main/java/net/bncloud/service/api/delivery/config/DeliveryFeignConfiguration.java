package net.bncloud.service.api.delivery.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.service.api.delivery")
@EnableFeignClients(basePackages = {"net.bncloud.service.api.delivery"})
@Import(FeignClientsConfiguration.class)
public class DeliveryFeignConfiguration {


}
