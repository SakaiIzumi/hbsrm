package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @description 工厂所属类型
 * @since 2022/5/17
 */
@AllArgsConstructor
@Getter
public enum FactoryBelongTypeEnum {

    PURCHASE("purchase","采购方"),
    SUPPLIER("supplier","供应商")
    ;

    private final String code;
    private final String desc;
}
