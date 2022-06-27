package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @version 1.0.0
 * @description 销售工作台  or  采购工作台
 * @since 2022/1/6
 */
@Getter
@AllArgsConstructor
public enum WorkBench {

    ORG("org", "组织"),
    SUPPLIER("supplier", "销售工作台"),
    PURCHASE("purchase", "采购工作台");

    private String code;

    private String name;
}
