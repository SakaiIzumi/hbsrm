package net.bncloud.contract;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableBncResourceServer
@EnableDiscoveryClient
@MapperScan(basePackages="net.bncloud.contract.mapper")
public class ZyContractApplication {



    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZyContractApplication.class);

        application.run(args);
    }


}
