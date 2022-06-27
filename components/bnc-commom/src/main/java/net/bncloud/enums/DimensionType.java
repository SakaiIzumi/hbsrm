package net.bncloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DimensionType {

    purchaser_code("purcharser_code", "采购方编码"),
    supplier_code("supplier_code", "采购方编码"),
    amount_field("amount_field", "金额"),

    ;

    private final String code;

    private final String name;
}
