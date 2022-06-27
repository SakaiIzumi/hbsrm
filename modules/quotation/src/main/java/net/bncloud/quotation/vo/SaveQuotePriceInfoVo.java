package net.bncloud.quotation.vo;

import lombok.Data;
import lombok.ToString;
import net.bncloud.quotation.entity.QuotationLineExt;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/3/11 17:08
 */
@Data
@ToString
public class SaveQuotePriceInfoVo {
    /**
     * 询价单ID
     */
    private Long quotationId;
    /**
     * 扩展字段 只传普通字段
     */
    private List<QuotationLineExtVo> quotationLineExts;
}
