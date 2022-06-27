package net.bncloud.quotation.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.quotation.vo.PricingRecordVo;
import net.bncloud.quotation.vo.QuotationAttachmentSaveVo;
import net.bncloud.quotation.vo.QuotationAttachmentVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class PricingRecordAndRemark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询价单主键ID（适应前端传参）
     */
    @ApiModelProperty(value = "询价单主键ID")
    private String quotationId;

    /**
     * 询价单主键ID
     */
    @ApiModelProperty(value = "询价单主键ID")
    private String quotationBaseId;

    /**
     * 说明信息
     */
    @ApiModelProperty(value = "说明信息")
    private String pricingRemark;

    /**
     * 附件列表信息
     */
    @ApiModelProperty(value = "定价请示记录信息")
    private List<PricingRecordVo> pricingRecords;

    /**
     * 适应前端传过来的份额信息（保存接受的时候需要把份额保存到pricingRecords的字段里面）
     */
    @ApiModelProperty(value = "附件列表信息")
    private List<PricingRecordVo> undefined;

    /**
     * 附件列表信息
     */
    @ApiModelProperty(value = "附件列表信息")
    private List<QuotationAttachment> quotationAttachmentsList;

    /**
     * 附件列表信息
     */
    @ApiModelProperty(value = "附件列表信息")
    private List<QuotationAttachmentVo> attachmentList;


}
