package net.bncloud.quotation.vo;

import lombok.Data;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.quotation.entity.QuotationSupplier;

import java.io.Serializable;
import java.util.List;

/**
 * 询价单通用VO
 * @author Toby
 */
@Data
public class QuotationBaseCommonVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基础信息
     */
    private QuotationBaseVo quotationBaseVo;

    /**
     * 询价行信息
     */
    private QuotationLineBaseVo quotationLineBaseVo;

    /**
     * 供应商信息
     */
    private List<QuotationSupplier> quotationSupplierList;

    /**
     * 附件需求清单
     */
    private List<QuotationAttRequire> quotationAttRequireList;

    /**
     * 附件信息
     */
    private List<QuotationAttachment> quotationAttachmentList;

}
