package net.bncloud.delivery;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBncResourceServer
@EnableAsync
@ComponentScan(basePackages = "net.bncloud")
@EnableFeignClients(basePackages = {"net.bncloud" +
        ".*.feign", "net.bncloud.service.api", "net.bncloud.bis.service.api"})
@MapperScan(basePackages = "net.bncloud.delivery.mapper")
public class DeliveryApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DeliveryApplication.class);
        application.run(args);
    }
}
