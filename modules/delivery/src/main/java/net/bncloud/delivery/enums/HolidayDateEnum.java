package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HolidayDateEnum {

    //法定节假日类型
    HOLIDAY_DAY("YES","法定节假日","0"),
    IS_996("996","法定上班日","0"),

    //是否法定节假日
    IS_HOLIDAY_LEGAL("1","法定节假日","1"),
    NOT_HOLIDAY_LEGAL("2","法定上班日","2"),

    //是否法定节假日
    IS_HOLIDAY_RECESS("1","假期节假日","1"),
    NOT_HOLIDAY_RECESS("2","非假期节假日","2"),

    //是否工作日
    IS_WORKDAY("3","法定上班日","1"),
    NOT_WORKDAY("4","法定节假日","2"),

    //是否周末
    IS_WEEKEND("5","是周末","1"),
    NOT_WEEKEND("6","不是周末","2"),
    ;


    private String code;
    private String name;
    private String inHoliday;
}
