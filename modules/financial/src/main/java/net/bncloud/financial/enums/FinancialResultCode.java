package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

/**
 * @author Toby
 */
@Getter
@AllArgsConstructor
public enum FinancialResultCode implements IResultCode {
    //400(两位业务模块，3位自定义编码)
    PARAM_ERROR(40004001, "参数错误"),
    //400(两位业务模块，3位自定义编码)
    RESOURCE_NOT_FOUND(40004002, "请求的资源不存在"),
    SETTLEMENT_RECONCILE_REPEAT(40004003, "单据已发起对账，请重新选择"),
    SETTLEMENT_NOT_FOUND(40004004, "未查询到结算池单据信息"),
    SETTLEMENT_NOT_PERIOD(40004005, "当前日期不在对账周期"),
    SETTLEMENT_SUPPLIER_FROZEN(40004006, "供应商已冻结"),
    SETTLEMENT_SUPPLIER_SUSPEND_COOPERATION(40004007, "供应商已暂停合作"),
    SETTLEMENT_NOT_TO_BE_CONFIRM(40004008, "对账单非待确认状态"),
    SETTLEMENT_SELETED(40004009, "单据正在发起对账，请重新选择"),
    SETTLEMENT_PURCHASER_CLOSED(40004010, "该采购方已关闭，请重新选择"),

    ;
    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}
