package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Toby
 */

@Getter
@AllArgsConstructor
@Slf4j
public enum  QuotationStatementStatusOperateRel {
    //
    TO_DRAFT("1","草稿",new QuotationOperateType[]{QuotationOperateType.LOOK,
            QuotationOperateType.LIST_OBSOLETE,QuotationOperateType.EDIT,QuotationOperateType.COPY,QuotationOperateType.OBSOLETE ,
            QuotationOperateType.QUOTATION_RETURN}),
    TO_QUOTATION("2","报价中",new QuotationOperateType[]{QuotationOperateType.LOOK,
            QuotationOperateType.LIST_OBSOLETE,QuotationOperateType.COPY,QuotationOperateType.DELIVER_TIME,QuotationOperateType.OBSOLETE,QuotationOperateType.QUOTE,
            QuotationOperateType.QUOTATION_RETURN, QuotationOperateType. COMPARISON_RETURN}),
    TO_BID_OPENING("3","待开标",new QuotationOperateType[]{QuotationOperateType.LOOK,QuotationOperateType.LIST_OBSOLETE,
            QuotationOperateType.COPY,QuotationOperateType.OBSOLETE,QuotationOperateType.QUOTATION_RETURN,
            QuotationOperateType. COMPARISON_RETURN,QuotationOperateType.QUOTATION_BID_OPENING}),
    TO_BE_ANSWERED("4","比价中",new QuotationOperateType[]{QuotationOperateType.LOOK,QuotationOperateType.LIST_OBSOLETE,
            QuotationOperateType.COPY,QuotationOperateType.QUOTATION_RETURN,QuotationOperateType.OBSOLETE,
            QuotationOperateType.DOWNLOAD_COMPARISON,QuotationOperateType.FRESH,QuotationOperateType.FAILURE_BID,QuotationOperateType.HAVE_PRICING,
            QuotationOperateType.COMPARISON_RETURN}),
    TO_BE_ANSWERED2("5","流标/已定价/已作废",new QuotationOperateType[]{QuotationOperateType.LOOK,
            QuotationOperateType.COPY,QuotationOperateType.QUOTATION_RETURN,QuotationOperateType.QUERY_PRICING,
            QuotationOperateType.DOWNLOAD_COMPARISON,QuotationOperateType.COMPARISON_RETURN}),
    TO_BE_ANSWERED3("8","新的轮次",new QuotationOperateType[]{QuotationOperateType.LOOK,QuotationOperateType.LIST_OBSOLETE,QuotationOperateType.QUOTE,
            QuotationOperateType.COPY,QuotationOperateType.QUOTATION_RETURN,QuotationOperateType.OBSOLETE,
            QuotationOperateType.DOWNLOAD_COMPARISON,QuotationOperateType.FRESH,QuotationOperateType.FAILURE_BID,QuotationOperateType.HAVE_PRICING,
            QuotationOperateType.COMPARISON_RETURN}),

    TO_QUOTATION_SALE_DETAIL_MARKED("6", "询价单销售协同已经应标的按钮", new QuotationOperateType[]{QuotationOperateType.QUOTATION_SALE_CANCEL, QuotationOperateType.QUOTATION_SALE_SUBMIT_QUOTATION}),
    TO_QUOTATION_SALE_DETAIL_FIRST("7", "询价单销售协同刚进去的按钮", new QuotationOperateType[]{QuotationOperateType.QUOTATION_SALE_MARKED, QuotationOperateType.QUOTATION_SALE_REJECT}),
    ;


    private String code;

    private String name;

    private QuotationOperateType[] operations;

    /**
     * 获取可执行操作
     * @param operateRel
     * @return
     */
    public static Map<String,Boolean> operations(Boolean comparisonDisplay,QuotationStatementStatusOperateRel operateRel){
        QuotationOperateType[] operations = operateRel.getOperations();
        if(comparisonDisplay){
            QuotationOperateType[] operations_copy = new QuotationOperateType[operations.length+1];
            System.arraycopy(operations,0,operations_copy,0,operations.length);
            operations_copy[operations.length]=QuotationOperateType.COMPARISON;
            operations=operations_copy;
        }
        Map<String,Boolean> permissions = new HashMap<>();
        for (QuotationOperateType operation : operations) {
            permissions.put(operation.getCode(),true);
        }
        return permissions;
    }
}
