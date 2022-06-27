package net.bncloud.quotation.event.vo;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.quotation.entity.QuotationBase;

/**
 * 询价单重报事件
 *
 * @author huangtao
 * @since 2021-03-12
 */
public class QuotationRestateInfoEvent extends BizEvent<QuotationBase> {


    private static final String eventCode = EventCode.quotation_supplier_restate.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public QuotationRestateInfoEvent(Object source, LoginInfo loginInfo, QuotationBase data) {
        super(source, eventCode, loginInfo, data);
    }
    public QuotationRestateInfoEvent(Object source, LoginInfo loginInfo, QuotationBase data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }




}
