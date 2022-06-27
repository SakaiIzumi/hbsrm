package net.bncloud.quotation.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 询价单状态数量的VO
 * @author lijiaju
 * @date 2022/3/10 11:50
 */
@Data
@ToString
public class QuotationStaticCountVo {
    /**
     *草稿数量
     */
    private Integer draftCount;
    /**
     *报价中数量
     */
    private Integer quotationCount;
    /**
     *待开标数量
     */
    private Integer bidOpeningCount;
    /**
     *比价中数量
     */
    private Integer comparisonCount;
    /**
     *流标数量
     */
    private Integer failureBidCount;
    /**
     *已定价数量
     */
    private Integer havePricingCount;
    /**
     *已作废数量
     */
    private Integer obsoleteCount;
    /**
     *新的轮次数量
     */
    private Integer freshCount;
    /**
     *未报价数量
     */
    private Integer noQuotationCount;

}
