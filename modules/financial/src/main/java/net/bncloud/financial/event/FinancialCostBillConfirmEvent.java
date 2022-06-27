package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.financial.entity.FinancialCostBill;

/**
 * 费用单确认事件
 */
public class FinancialCostBillConfirmEvent extends BizEvent<FinancialCostBill> {

    private final static String eventCode = EventCode.cost_bill_confirm.getCode();

    public FinancialCostBillConfirmEvent(Object source, LoginInfo loginInfo, FinancialCostBill data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialCostBillConfirmEvent(Object source, LoginInfo loginInfo, FinancialCostBill data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
