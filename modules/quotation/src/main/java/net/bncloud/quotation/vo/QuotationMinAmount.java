package net.bncloud.quotation.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报价最小金额信息
 * @author Toby
 */
@Data
public class QuotationMinAmount {

    /**
     * 字段
     */
    private String field;

    /**
     * 最小金额
     */
    private BigDecimal minAmount;
}
