package net.bncloud.order;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@EnableBncResourceServer
@MapperScan(basePackages = "net.bncloud.order.mapper")
@ComponentScan(basePackages ={"net.bncloud"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OrderApplication.class);

        application.run(args);
    }
}
