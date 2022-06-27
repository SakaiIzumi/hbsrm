package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum QuotationAttBizEnum {
    /*询报价附件biz类型：
        quotation_base 询价单,quotation_pricing 定价单
    */
    QUOTATION_BASE("quotation_base","询价单"),
    QUOTATION_PRICING("quotation_pricing","定价单"),

    ;

    private String code;

    private String name;
}