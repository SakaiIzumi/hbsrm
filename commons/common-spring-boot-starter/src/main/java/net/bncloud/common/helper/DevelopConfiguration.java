package net.bncloud.common.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发环境配置 (表示当开发环境存在)
 *   配置作用，用于处理非正式环境，自定义改变参数
 *    例如：短信发送测试，不需要发给实际用户，而是发送配置好的
 *         一些推送啊，等等
 */
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "application.develop")
public class DevelopConfiguration {

    /**
     * 短信环境
     */
    private SmsEnv smsEnv;

    @Setter
    @Getter
    public static class SmsEnv {
        /**
         * 可发送短信的手机号
         */
        private String phone;

        /**
         * 开发人员手机号
         */
        private List<String> devPhones = new ArrayList<>();

    }


}
