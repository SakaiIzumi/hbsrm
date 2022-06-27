package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.financial.entity.FinancialCostBill;

/**
 * 费用单撤回事件
 *
 * @author Toby
 */
public class FinancialCostBillWithdrawEvent extends BizEvent<FinancialCostBill> {
    private final static String eventCode = EventCode.cost_bill_withdraw.getCode();

    public FinancialCostBillWithdrawEvent(Object source, LoginInfo loginInfo, FinancialCostBill data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialCostBillWithdrawEvent(Object source, LoginInfo loginInfo, FinancialCostBill data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
