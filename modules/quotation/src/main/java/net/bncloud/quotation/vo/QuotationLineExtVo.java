package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.quotation.param.QuotationLineExtParam;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 询价行动态行扩展信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationLineExtVo extends QuotationLineExt implements Serializable,Comparable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compareTo(Object o) {

        QuotationLineExtVo biddingLineExtParam = (QuotationLineExtVo)o;
        BigDecimal cur = new BigDecimal(this.getOrderValue());
        BigDecimal oth = new BigDecimal(biddingLineExtParam.getOrderValue());

        return cur.compareTo(oth);
    }


}
