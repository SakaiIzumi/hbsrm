package net.bncloud.delivery.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @since 2022/5/17
 */
@Getter
@AllArgsConstructor
public enum MrpVacationTypeEnum {
    HOLIDAY("0", "节假日"),
    COMPENSATORY("1", "调休休息"),
    AUTO_SUBSCRIBE("2", "法定节假日"),
    DEFAULT_HOLIDAY("3", "休息日"),
    ;

    private String code;
    private String name;

    public static String returnCode(String name){
        MrpVacationTypeEnum[] values = MrpVacationTypeEnum.values();
        for (MrpVacationTypeEnum value : values) {
            if(StrUtil.equals(name,value.getName())){
                return value.getCode();
            }
        }
        return null;
    }

}
