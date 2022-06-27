package net.bncloud.common.bizutil.number;

import lombok.Getter;

/**
 * 单号 枚举
 * @author Rao
 * @Date 2021/12/24
 **/
@Getter
public enum NumberType {

    /**
     * 发票
     */
    invoice("FP",""),
    /**
     * 费用
     */
    cost("FY",""),
    /**
     * 发货
     */
    ship("S",""),

    /**
     * 送货计划
     */
    plan("J",""),

    /**
     * 对账单
     */
    statement("DZ",""),

    /**
     * 询价单
     */
    quotation("XJ",""),

    /**
     * 询价模板
     */
    quotation_template("QT",""),

    /**
     * oem
     */
    oem_template("OEM",""),

    /**
     * oem 地址编码
     */
    oem_address_template("AD",""),

    ;

    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 后缀
     */
    private final String suffix;

    NumberType(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
