package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.QuotationAttRequire;
import lombok.Data;
import net.bncloud.quotation.entity.QuotationAttRequireAttachment;

import java.io.Serializable;
import java.util.List;

/**
 * 附件需求清单
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationAttRequireVo extends QuotationAttRequire implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer itemNo;

    /**
     * 需求清单的文件列表
     */
    List<QuotationAttRequireAttachmentVo> quotationAttRequireAttachmentVos;
}
