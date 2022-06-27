package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @version 1.0.0
 * @description
 * @since 2022/3/24
 */
@Getter
@AllArgsConstructor
public enum QuotationCfgParam {

    QUOTATION_SUPPLIER_EARLY_WARNING("quotation_supplier:early_warning","是否开启供应商预警"),
            ;
    private String code;
    private String description;
}
