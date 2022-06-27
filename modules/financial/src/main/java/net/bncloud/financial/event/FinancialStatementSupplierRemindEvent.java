package net.bncloud.financial.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.financial.entity.FinancialStatement;

/**
 * @author Toby
 */
public class FinancialStatementSupplierRemindEvent extends BizEvent<FinancialStatement> {

    private static final String eventCode = EventCode.statement_supplier_remind.getCode();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo 当前用户登录信息
     * @param data      业务数据
     */
    public FinancialStatementSupplierRemindEvent(Object source, LoginInfo loginInfo, FinancialStatement data) {
        super(source, eventCode, loginInfo, data);
    }

    public FinancialStatementSupplierRemindEvent(Object source, LoginInfo loginInfo, FinancialStatement data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }


}
