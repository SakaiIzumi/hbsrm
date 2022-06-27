package net.bncloud.quotation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsMsgTempConfig {

    private String purchaserBidOpeningVerification;//"SMS_235491617","采购方-开标校验"
    private String supplierQuotationDeadlineAlertNotice;//"SMS_235491612","供应商-报价截止时间预警通知"
    private String supplierReQuotationNotice;//"SMS_235496390","供应商-重新报价通知"
    private String supplierPleaseInformOfInquiry;//"SMS_235481494","供应商-请询价通知"
    private String supplierNotificationOfUnsuccessfulResults;//"SMS_235476588","供应商-未中标结果通知"
    private String supplierNotificationOfAwardResults;//"SMS_235476583",""

}
