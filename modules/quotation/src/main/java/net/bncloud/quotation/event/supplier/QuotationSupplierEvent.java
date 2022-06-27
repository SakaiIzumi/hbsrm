package net.bncloud.quotation.event.supplier;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.quotation.entity.QuotationSupplier;
import org.springframework.context.ApplicationEvent;

/**
 * @Title: QuotationSupplierEvent
 * @Description: 预警发送消息
 * @author: YangYu
 * @date: 2022/3/4 16:37
 */
public class QuotationSupplierEvent extends BizEvent<QuotationSupplier> {
    private static final String eventCode = EventCode.quotation_supplier_early_warning.getCode();
    private static final Long orgId = -1L;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo 当前用户登录信息
     * @param data      业务数据
     */
    public QuotationSupplierEvent(Object source, LoginInfo loginInfo, QuotationSupplier data) {
        super(source, eventCode, loginInfo, data);
    }

    public QuotationSupplierEvent(Object source, LoginInfo loginInfo, QuotationSupplier data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data, Sources, SourcesName);
    }
}
