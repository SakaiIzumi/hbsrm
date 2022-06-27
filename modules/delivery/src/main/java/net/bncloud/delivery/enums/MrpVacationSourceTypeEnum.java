package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @since 2022/5/17
 */
@Getter
@AllArgsConstructor
public enum MrpVacationSourceTypeEnum {
    ADD_MANUALLY("0", "手动新增"),
    AUTO_SUBSCRIBE("1", "自动订阅"),
    DEFAULT_HOLIDAY("2", "默认节假日"),
    DEFAULT_HOLIDAY_996("3", "自动订阅-上班"),

    //FactoryWorkDay的batchSet方法中使用
    SAVE_LIST("100", "保存的列表"),
    UPDATE_LIST("200", "更新的列表"),

    ;

    private String code;
    private String name;
}
