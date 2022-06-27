package net.bncloud.quotation.event.vo;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.quotation.entity.QuotationBase;

/**
 *
 *
 *
 * @author liyh
 * @since 2021-03-18
 */
public class QuotationSupplierQuotedPriceEvent extends BizEvent<QuotationBase> {


    private static final String eventCode = EventCode.quotation_supplier_quoted_price.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public QuotationSupplierQuotedPriceEvent(Object source, LoginInfo loginInfo, QuotationBase data) {
        super(source, eventCode, loginInfo, data);
    }
    public QuotationSupplierQuotedPriceEvent(Object source, LoginInfo loginInfo, QuotationBase data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }




}
