package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志：对账单的操作日志、费用单的操作日志
 */
@AllArgsConstructor
@Getter
public enum OperationLogType {
    STATEMENT("statement", "对账单的日志"),
    /**
     * 对账单退回
     */
    STATEMENT_REJECT("statement_purchase:reject","退回对账单"),

    COST_BILL("cost", "费用单的日志"),
    /**
     * 发票通知单
     */
    INVOICE("invoice", "发票通知单");

    private String code;
    private String name;
}
