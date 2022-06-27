package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @since 2022/5/17
 */
@Getter
@AllArgsConstructor
public enum MrpVacationStatusEnum {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    IS_996(2, "当前假日类型为上班状态"),
    ;

    private Integer code;
    private String name;
}
