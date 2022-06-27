package net.bncloud.information.config;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = {"net.bncloud.information.feign"})
@Import(FeignClientsConfiguration.class)
public class FeignInformationConfiguration {
    /**
     * Set the Feign specific log level to log client REST requests.
     */
    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC;
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
}