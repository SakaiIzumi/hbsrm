package net.bncloud.quotation.param;

import lombok.Data;
import net.bncloud.quotation.entity.QuotationLineExt;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/3/12 11:26
 */
@Data
public class ComputeLineExtPriceParam {
    /**
     * 询价单ID
     */
    private Long quotationId;
    /**
     * 询价单扩展行字段 传普通值 不需要传计算字段
     */
    private List<QuotationLineExt> quotationLineExtList;
}
