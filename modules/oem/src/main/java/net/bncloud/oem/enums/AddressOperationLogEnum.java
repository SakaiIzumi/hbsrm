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
public enum AddressOperationLogEnum {
    CREATE_LOG("1","新建"),
    EDIT_LOG("2","维护"),
    IMPORT("3","导入"),
    ;
    private final String code;
    private final String name;
}
