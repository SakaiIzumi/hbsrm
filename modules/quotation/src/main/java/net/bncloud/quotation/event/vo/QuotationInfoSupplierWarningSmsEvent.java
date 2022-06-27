package net.bncloud.quotation.event.vo;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.event.SmsBizEvent;
import net.bncloud.quotation.entity.QuotationSupplier;

/**
 * 询价单供应商预警
 */
public class QuotationInfoSupplierWarningSmsEvent extends SmsBizEvent<QuotationSupplier> {
    private static final String eventCode = EventCode.quotation_supplier_sms_early_warning.getCode();


    public QuotationInfoSupplierWarningSmsEvent(Object source, LoginInfo loginInfo, QuotationSupplier data) {
        super(source, eventCode, loginInfo, data);
    }

    public QuotationInfoSupplierWarningSmsEvent(Object source, LoginInfo loginInfo, QuotationSupplier data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
