package net.bncloud.quotation.event.vo;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.quotation.entity.QuotationBase;

/**
 * @author ddh
 * @version 1.0.0
 * @description 供应商应标通知
 * @since 2022/3/26
 */
public class QuotationSupplierResponseNoticeEvent extends BizEvent<QuotationBase> {

    private static final String eventCode = EventCode.quotation_supplier_response_notice.getCode();

    public QuotationSupplierResponseNoticeEvent(Object source, LoginInfo loginInfo, QuotationBase data) {
        super(source, eventCode, loginInfo, data);
    }

    public QuotationSupplierResponseNoticeEvent(Object source, LoginInfo loginInfo, QuotationBase data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
