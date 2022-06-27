package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @version 1.0.0
 * @description 订单收货
 * @since 2022/4/25
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderOperationLogEnum {
    CONFIRM_LOG("1","确认收货"),
    RETURN_LOG("2","退回收货"),
    ;
    private final String code;
    private final String name;
}
