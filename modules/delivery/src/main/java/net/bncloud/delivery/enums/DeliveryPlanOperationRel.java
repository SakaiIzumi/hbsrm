package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/25
 */
@Getter
@AllArgsConstructor
public enum DeliveryPlanOperationRel {

    SEND("delivery_plan:send","发送送货计划"),
    REMIND("delivery_plan:remind","提醒供应商"),
    CONFIRM("delivery_plan:confirm","确认送货计划")
    ;
    private String code;
    private String msg;


}
