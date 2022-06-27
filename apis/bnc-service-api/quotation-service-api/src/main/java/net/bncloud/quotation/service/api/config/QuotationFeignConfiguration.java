package net.bncloud.quotation.service.api.config;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("net.bncloud.quotation")
@EnableFeignClients(basePackages = {"net.bncloud.quotation.service.api"})
@Import(FeignClientsConfiguration.class)
public class QuotationFeignConfiguration {

}
