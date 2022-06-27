package net.bncloud.quotation.param;


import lombok.Data;
import net.bncloud.quotation.entity.QuotationBase;

import java.io.Serializable;


/**
 * <p>
 * 询价基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationBaseRestateParam extends QuotationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID数组
     */
    private Long[] supplierIds;

    /**
     * 是否推送每行最低报价行 `0-否 1-是`
     */
    private String pushCheapest;



}
