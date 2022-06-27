package net.bncloud.quotation.vo;

import lombok.*;
import net.bncloud.api.feign.file.FileInfo;
import net.bncloud.quotation.entity.PricingRemark;
import net.bncloud.quotation.entity.QuotationAttachment;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingInfoForRemarkAndFileVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private PricingRemark remark;

    private List<QuotationAttachment> quotationAttachments;
    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;


}
