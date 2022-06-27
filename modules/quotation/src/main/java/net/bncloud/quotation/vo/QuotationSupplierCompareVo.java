package net.bncloud.quotation.vo;


import lombok.Data;
import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.entity.QuotationSupplier;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 供应商比较信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Data
public class QuotationSupplierCompareVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<QuotationLineExt> firstColumeData;

    private List<QuotationSupplierVo> dynColumeData;

}
