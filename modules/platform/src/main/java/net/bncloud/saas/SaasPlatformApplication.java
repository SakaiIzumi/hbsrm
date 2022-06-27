package net.bncloud.saas;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableBncResourceServer
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableScheduling
@EnableJpaRepositories(basePackages = "net.bncloud.saas.**.repository")
public class SaasPlatformApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SaasPlatformApplication.class);
        application.run(args);
    }
}