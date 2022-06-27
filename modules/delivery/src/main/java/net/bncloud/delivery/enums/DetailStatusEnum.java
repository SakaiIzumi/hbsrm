package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @description 计划明细状态
 * @since 2022/5/26
 */
@Getter
@AllArgsConstructor
public enum DetailStatusEnum {
    //计划明细状态：1待发布、2待确认、3差异待确认、4已确认
    DRAFT("1", "待发布"),
    TO_BE_CONFIRM("2", "待确认"),
    DIFFERENCE_TO_BE_CONFIRMED("3", "差异待确认"),
    CONFIRMED("4", "已确认"),
    ;
    private final String code;

    private final String desc;
}
