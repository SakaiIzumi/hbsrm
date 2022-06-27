package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * 退回消息提醒
 */
public class FinancialCostBillReturnedEvent extends BizEvent {
    private final static String eventCode = EventCode.cost_bill_return.getCode();

    public FinancialCostBillReturnedEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialCostBillReturnedEvent(Object source, LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
