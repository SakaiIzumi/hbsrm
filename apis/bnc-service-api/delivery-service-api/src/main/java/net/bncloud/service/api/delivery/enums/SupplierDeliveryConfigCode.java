package net.bncloud.service.api.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * desc: 供应商送货配置枚举
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Getter
@AllArgsConstructor
public enum SupplierDeliveryConfigCode {

    /**
     * 是否设置默认工作日
     */
    defaultWeekdayEnable("false","是否设置默认工作日"),

    /**
     * 自动导入法定节假日安排
     */
    autoImportLegalHolidaysEnable("false","自动导入法定节假日安排"),

    ;

    /**
     * 默认值
     */
    private final String defaultValue;
    private final String desc;
}
