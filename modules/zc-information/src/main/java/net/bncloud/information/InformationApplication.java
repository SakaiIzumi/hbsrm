package net.bncloud.information;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages="net.bncloud.order.mapper")
@EnableBncResourceServer
@ComponentScan(basePackages={"net.bncloud.sms","net.bncloud.information","net.bncloud.common.helper"})
public class InformationApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(InformationApplication.class);
        application.run(args);
    }
}