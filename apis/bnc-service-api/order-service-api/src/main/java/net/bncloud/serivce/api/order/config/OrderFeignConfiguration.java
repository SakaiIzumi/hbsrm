package net.bncloud.serivce.api.order.config;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.serivce.api.order")
@EnableFeignClients(basePackages = {"net.bncloud.serivce.api.order"})
@Import(FeignClientsConfiguration.class)
public class OrderFeignConfiguration {


}
