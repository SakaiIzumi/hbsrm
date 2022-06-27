package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划状态枚举
 * @since 2022/1/17
 */
@Getter
@AllArgsConstructor
public enum DeliveryPlanStatus {
    //计划状态：0待发布、1待确认、2已确认、3差异待确认
    DRAFT("0", "待发布"),
    TO_BE_CONFIRM("1", "待确认"),
    CONFIRMED("2", "已确认"),
    DIFFERENCE_TO_BE_CONFIRMED("3","差异待确认"),
    ;


    private final String code;

    private final String description;
}
