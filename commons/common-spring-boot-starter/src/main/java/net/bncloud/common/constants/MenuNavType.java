package net.bncloud.common.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MenuNavType {
    /**
     * 采购工作台
     */
    purchaser,
    /**
     * 采购配置
     */

    purchaserSetting,
    /**
     * 协同配置
     */
    purchaserCooperate,

    /**
     * 销售工作台
     */
    supplier,
    /**
     * 销售配置
     */
    supplierSetting,

    /**
     * 平台配置
     */
    platformSetting,

    bigDataScreen;

    public static List<String> menuNavTypes() {
        return Arrays.stream(MenuNavType.values()).map(MenuNavType::name).collect(Collectors.toList());
    }
}
