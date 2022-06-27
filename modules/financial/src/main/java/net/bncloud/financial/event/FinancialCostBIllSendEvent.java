package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.financial.entity.FinancialCostBill;

/**
 * 费用单发送事件
 */
public class FinancialCostBIllSendEvent extends BizEvent<FinancialCostBill> {

    private static final String eventCode = EventCode.cost_bill_send.getCode();

    public FinancialCostBIllSendEvent(Object source, LoginInfo loginInfo, FinancialCostBill data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialCostBIllSendEvent(Object source, LoginInfo loginInfo, FinancialCostBill data, String SourcesCode, String sourcesName) {
        super(source, eventCode, loginInfo, data, SourcesCode, sourcesName);
    }


}
