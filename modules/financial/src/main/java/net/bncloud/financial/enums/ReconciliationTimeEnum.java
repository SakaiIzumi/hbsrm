package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 对账周期枚举
 * @author: liulu
 * @Date: 2022-03-10 18:19
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum ReconciliationTimeEnum {
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    UNLIMITED("unlimited"),
    ;


    String value;

    public static ReconciliationTimeEnum getEnum(String value){
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (ReconciliationTimeEnum enums : ReconciliationTimeEnum.values()) {
            if (StringUtils.equals(enums.getValue(), value)) {
                return enums;
            }
        }
        return null;
    }
}
