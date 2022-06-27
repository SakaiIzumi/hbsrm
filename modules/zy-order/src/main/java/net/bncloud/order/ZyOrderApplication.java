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
@ComponentScan(basePackages = {"net.bncloud"})
@MapperScan(basePackages = "net.bncloud.order.mapper")
public class ZyOrderApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZyOrderApplication.class);

        application.run(args);
    }
}
