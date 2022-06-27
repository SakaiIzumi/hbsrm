package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Toby
 */

@Getter
@AllArgsConstructor
public enum QuotationOperateType {
    //
    LOOK("look","列表查看按钮"),
    EDIT("edit","列表编辑按钮"),
    QUOTE("quote","列表的报价按钮"),
    LIST_OBSOLETE("list_obsolete","列表作废按钮"),
    COPY("copy","询价单详情的复制按钮"),
    DELIVER_TIME("deliver_time","询价单详情的调整截止时间按钮"),
    COMPARISON("comparison","询价单详情的比价按钮"),
    OBSOLETE("obsolete","询价单详情的作废按钮"),
    QUOTATION_RETURN("quotation_return","询价单详情的返回按钮"),
    QUOTATION_BID_OPENING("quotation_bid_opening","询价单详情的开标按钮"),
    DOWNLOAD_COMPARISON("download_comparison","比价大厅的导出比价按钮"),
    FRESH("fresh","比价大厅的重报按钮"),
    FAILURE_BID("failure_bid","比价大厅的流标按钮"),
    HAVE_PRICING("have_pricing","比价大厅的定价按钮"),
    QUERY_PRICING("query_pricing","比价大厅定价询价单查看定价详情按钮"),
    COMPARISON_RETURN("comparison_return","比价大厅的返回按钮"),
    QUOTATION_SALE_MARKED("quotation_sale_marked","销售协同询价单详情应标按钮"),
    QUOTATION_SALE_REJECT("quotation_sale_reject","销售协同询价单详情拒绝按钮"),
    QUOTATION_SALE_CANCEL("quotation_sale_cancel","销售协同询价单详情返回按钮"),
    QUOTATION_SALE_SUBMIT_QUOTATION("quotation_sale_submit_quotation","销售协同询价单详情提交报价按钮"),
    ;

    private String code;

    private String name;
}
