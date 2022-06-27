package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.financial.entity.FinancialCostBill;

/**
 * 费用单作废事件
 */
public class FinancialCostBillInvalidEvent extends BizEvent<FinancialCostBill> {
    private static final String eventCode = EventCode.cost_bill_invalid.getCode();

    public FinancialCostBillInvalidEvent(Object source, LoginInfo loginInfo, FinancialCostBill data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialCostBillInvalidEvent(Object source, LoginInfo loginInfo, FinancialCostBill data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }

}
