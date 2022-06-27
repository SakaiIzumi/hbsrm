package net.bncloud.notify.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class MessageCenterApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MessageCenterApplication.class);

        application.run(args);
    }
}
