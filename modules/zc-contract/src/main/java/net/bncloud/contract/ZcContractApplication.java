package net.bncloud.contract;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBncResourceServer
@EnableDiscoveryClient
@EnableScheduling
@MapperScan(basePackages="net.bncloud.contract.mapper")
public class ZcContractApplication {



    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZcContractApplication.class);

        application.run(args);
    }


}
