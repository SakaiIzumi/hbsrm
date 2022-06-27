package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsMsgTempEnum {


    //采购方-开标校验  SMS_235491617  您的验证码是:${idenfityCode}，仅用于开标校验，5分钟内有效。请勿向他人泄露，如非本人操作，请忽略此短信。
    //供应商-报价截止时间预警通知  SMS_235491612  您参与的询价单 ${inquiryNo}，即将截止报价，请及时处理!
    //供应商-重新报价通知  SMS_235496390  您本次参与询价单号为${inquiryNo}，邀请重新报价，请及时登录系统处理!
    //供应商-请询价通知  SMS_235481494  ${companyName}请您参与物料询价，询价单号${inquiryNo}
    //供应商-未中标结果通知  SMS_235476588  ${inquiryNo} 询价单，本次询价您未中标，感谢您的参与
    //供应商-中标结果通知  SMS_235476583  ${inquiryNo} 询价单，本次询价您已成功中标，请及时和客户取得联系
    SMS_235491617("SMS_235491617","采购方-开标校验"),
    SMS_235491612("SMS_235491612","供应商-报价截止时间预警通知"),
    SMS_235496390("SMS_235496390","供应商-重新报价通知"),
    SMS_235481494("SMS_235481494","供应商-请询价通知"),
    SMS_235476588("SMS_235476588","供应商-未中标结果通知"),
    SMS_235476583("SMS_235476583","供应商-中标结果通知"),

    ;

    private String code;

    private String name;
}
