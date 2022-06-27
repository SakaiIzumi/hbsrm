package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.vo.QuotationBaseCommonVO;

import java.util.List;

/**
 * 询价单通用service
 * @author Toby
 */
public interface QuotationBaseCommonService {


    /**
     * 询价单通用保存接口，不推荐使用。可使用分步骤保存页签接口代替
     * @param quotationBaseCommonVo 询价单通用保存VO
     * @return 询价单基础信息 ID
     */
    @Deprecated
    Long commonSave(QuotationBaseCommonVO quotationBaseCommonVo);

    /**
     * 清空并保存供应商信息
     * @param quotationBaseId 询价单基础信息ID
     * @param quotationSupplierList 询价单供应商信息
     */
    void cleanAndSaveSuppliers(Long quotationBaseId, List<QuotationSupplier> quotationSupplierList);
}
