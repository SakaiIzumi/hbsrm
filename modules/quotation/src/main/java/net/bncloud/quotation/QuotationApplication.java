package net.bncloud.quotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bncloud.security.annotation.EnableBncResourceServer;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

/**
 * @author Toby
 */
@SpringBootApplication
@EnableBncResourceServer
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(basePackages = {"net.bncloud.*"})
@EnableFeignClients(basePackages = {"net.bncloud.*.feign", "net.bncloud.service.api", "net.bncloud.*.service.api"})
@MapperScan(basePackages="net.bncloud.quotation.mapper")
public class QuotationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationApplication.class);

    private final Environment environment;

    public QuotationApplication(Environment environment) {
        this.environment = environment;
    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QuotationApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }


    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.warn("The host name could not be determined, using `localhost` as fallback");
        }
        LOGGER.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }


    @PostConstruct
    public void setDefTime() {
        TimeZone.getTimeZone("Asia/Shanghai");
    }






}
