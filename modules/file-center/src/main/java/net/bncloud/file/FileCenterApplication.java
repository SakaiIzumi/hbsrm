package net.bncloud.file;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableBncResourceServer
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "net.bncloud.file.repository")
public class FileCenterApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FileCenterApplication.class);

        application.run(args);
    }
}
