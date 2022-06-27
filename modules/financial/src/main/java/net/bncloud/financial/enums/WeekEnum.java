package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: liulu
 * @Date: 2022-03-10 18:04
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum WeekEnum {
    MONDAY("Monday",1),
    TUESDAY("Tuesday",2),
    WEDNESDAY("Wednesday",3),
    THURSDAY("Thursday",4),
    FRIDAY("Friday",5),
    SATURDAY("Saturday",6),
    SUNDAY("Sunday",7),
    ;

    private String code;
    private Integer value ;


    /**
     * 通过code取枚举
     *
     * @param code
     * @return
     */
    public static WeekEnum getWeekEnum(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (WeekEnum enums : WeekEnum.values()) {
            if (StringUtils.equals(enums.getCode(), code)) {
                return enums;
            }
        }
        return null;
    }
}
