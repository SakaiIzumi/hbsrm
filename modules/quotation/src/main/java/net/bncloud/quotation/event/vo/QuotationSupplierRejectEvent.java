package net.bncloud.quotation.event.vo;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.quotation.entity.QuotationBase;

/**
 * @author ddh
 * @version 1.0.0
 * @description 供应商应标拒绝
 * @since 2022/3/31
 */
public class QuotationSupplierRejectEvent extends BizEvent<QuotationBase> {

    private final static String eventCode = EventCode.quotation_supplier_reject.getCode();

    public QuotationSupplierRejectEvent(Object source, LoginInfo loginInfo, QuotationBase data) {
        super(source, eventCode, loginInfo, data);
    }

    public QuotationSupplierRejectEvent(Object source, LoginInfo loginInfo, QuotationBase data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
