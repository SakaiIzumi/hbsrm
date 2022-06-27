package net.bncloud.quotation.vo;


import lombok.Data;
import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.quotation.entity.PricingRecord;
import net.bncloud.quotation.entity.QuotationSupplier;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 询价供应商信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Data
public class QuotationSupplierVo extends QuotationSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BiddingLineExt> allBiddingLineExt;

    private PricingRecord pricingRecordVo;

    private Boolean noticeBidStatus;

}
