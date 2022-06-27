package net.bncloud.sms.config;

import net.bncloud.sms.SendSmsMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfiguration {

    @Value("${application.sms.aliyun.appId}")
    private String appId;

    @Value("${application.sms.aliyun.appSecrete}")
    private String appSecrete;

    private String sign = "美尚智采";

//    @Value("${application.aliyun.tempcode.pricing}")
//    private String tempcode = "SMS_235476583";

    @Bean
    public SendSmsMsg sendSmsMsg() {
        SendSmsMsg sendSmsMsg = new SendSmsMsg();
        sendSmsMsg.setAppId(appId);
        sendSmsMsg.setAppSecrete(appSecrete);
        sendSmsMsg.setSign(sign);
        return sendSmsMsg;
    }

}
