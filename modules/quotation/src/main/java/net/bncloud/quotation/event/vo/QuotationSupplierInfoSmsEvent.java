package net.bncloud.quotation.event.vo;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.event.SmsBizEvent;
import net.bncloud.quotation.entity.QuotationBase;

/**
 * <p>
 * 询价表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
public class QuotationSupplierInfoSmsEvent extends SmsBizEvent<QuotationBase> {


    private static final String eventCode = EventCode.quotation_supplier_sms_notice_bid.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public QuotationSupplierInfoSmsEvent(Object source, LoginInfo loginInfo, QuotationBase data) {
        super(source, eventCode, loginInfo, data);
    }
    public QuotationSupplierInfoSmsEvent(Object source, LoginInfo loginInfo, QuotationBase data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }




}
