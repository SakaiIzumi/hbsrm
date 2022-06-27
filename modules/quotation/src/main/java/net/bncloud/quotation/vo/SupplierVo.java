package net.bncloud.quotation.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.quotation.entity.QuotationSupplier;

/**
 * @author ddh
 * @description
 * @since 2022/4/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierVo extends QuotationSupplier {

    /**
     * 供应商编码
     */
    private String code;
}
