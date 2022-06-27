package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 费用单来源
 *
 * @author ddh
 * @version 1.0.0
 * @date 2021/12/17
 */
@Getter
@AllArgsConstructor
public enum SourceType {

    /**
     * 供应商端（销售协同）
     */
    SUPPLIER("supplier", "supplier"),

    /**
     * 采购方端（采购协同）
     */
    PURCHASE("purchase", "purchase");


    private String code;
    private String name;

    /**
     * 通过code取枚举
     *
     * @param code
     * @return
     */
    public static SourceType getTypeByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (SourceType enums : SourceType.values()) {
            if (StringUtils.equals(enums.getCode(), code)) {
                return enums;
            }
        }
        return null;
    }
}
