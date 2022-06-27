package net.bncloud.quotation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 询价单销售协同 按钮控制
 */
@Data
@ToString
@AllArgsConstructor
public class QuotationSaleDetailButtonStatus {
    /**
     * 是否隐藏应标的按钮
     */
    private Boolean markedHideStatus;
    /**
     * 是否隐藏拒绝的按钮
     */
    private Boolean rejectHideStatus;
    /**
     * 是否隐藏取消按钮
     */
    private Boolean cancelHideStatus;
    /**
     * 是否隐藏提交报价
     */
    private Boolean submitQuotationHideStatus;
}