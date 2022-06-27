package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.param.BiddingLineExtParam;
import net.bncloud.quotation.param.QuotationLineExtParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 招标行信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class BiddingLineExtVo extends BiddingLineExt implements Serializable ,Comparable {

    private static final long serialVersionUID = 1L;

    /**
     * 物料名称
     */
    private String quotationLineExtValue;

    /**
     * 物料价格
     */
    private String biddingLineExtValue;

    @Override
    public int compareTo(Object o) {
        BiddingLineExtVo biddingLineExtParam = (BiddingLineExtVo)o;
        BigDecimal cur = new BigDecimal(this.getOrderValue());
        BigDecimal oth = new BigDecimal(biddingLineExtParam.getOrderValue());

        return cur.compareTo(oth);
    }
}
