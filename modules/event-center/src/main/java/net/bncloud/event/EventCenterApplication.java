package net.bncloud.event;

import net.bncloud.security.annotation.EnableBncResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "net.bncloud.event.repository")
@EnableBncResourceServer
public class EventCenterApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EventCenterApplication.class);

        application.run(args);
    }
}
