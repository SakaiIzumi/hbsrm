package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liyh
 * @version 1.0.0
 * @description
 * @since 2022/4/24
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderReceivingStatusEnum {
    //  状态：0待确认，1已确认，2已退回
    TO_BE_CONFIRM("0","待确认"),
    CONFIRM("1","已确认"),
    RETURNED("2","已退回"),
    ;
    private final String code;
    private final String name;

    /**
     * 通过code取枚举
     *
     * @param code
     * @return
     */
    public static PurchaseOrderReceivingStatusEnum getTypeByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (PurchaseOrderReceivingStatusEnum enums : PurchaseOrderReceivingStatusEnum.values()) {
            if (StringUtils.equals(enums.getCode(), code)) {
                return enums;
            }
        }
        return null;
    }
}
