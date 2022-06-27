package net.bncloud.financial.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName StatementStatus
 * @Description: 对账单状态枚举类
 * @Author Administrator
 * @Date 2021/3/16
 * @Version V1.0
 **/
public enum StatementStatus {

    /**
     * 3、对账单状态：
     * 1）草稿
     * 2）待采购方确认
     * 3）已撤回
     * 4）作废
     * 5）已退回
     * 6）已确认
     * 7）待供应商确认
     */

    DRAFT("1", "草稿"),
    TO_BE_CUSTOMER_CONFIRM("2", "待采购方确认"),
    WITHDRAWN("3", "已撤回"),
    INVALID("4", "作废"),
    RETURNED("5", "已退回"),
    CONFIRMED("6", "已确认"),
    TO_BE_SUPPLIER_CONFIRM("7", "待供应商确认"),
    ;

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;


    StatementStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 通过code取枚举
     *
     * @param code
     * @return
     */
    public static StatementStatus getTypeByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (StatementStatus enums : StatementStatus.values()) {
            if (StringUtils.equals(enums.getCode(), code)) {
                return enums;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
