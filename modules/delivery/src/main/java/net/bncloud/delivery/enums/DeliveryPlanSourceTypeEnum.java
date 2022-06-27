package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @description  送货计划来源
 * @since 2022/5/23
 */
@Getter
@AllArgsConstructor
public enum DeliveryPlanSourceTypeEnum {
    MRP("mrp","MRP计划方案"),
    PURCHASE_ORDER("purchaseOrder","计划订单")
    ;
    private final String code;
    private final String desc;
}
