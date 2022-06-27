package net.bncloud.quotation.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;
import net.bncloud.quotation.entity.*;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 询价基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@Accessors(chain = true)
public class QuotationBaseVo extends QuotationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 定价请示记录信息/中标企业信息
     */
    @ApiModelProperty(value = "定价请示记录信息/中标企业信息")
    private List<PricingRecordVo> pricingRecordList;

    /**
     * 设备列表
     */
    @ApiModelProperty(value = "设备列表")
    private List<QuotationEquipment> equipmentList;

    /**
     * 询价基础信息行
     */
    @ApiModelProperty(value = "询价基础信息行")
    List<QuotationLineBaseVo> quotationLineBaseVoList;

    /**
     * 询价供应商信息
     */
    @ApiModelProperty(value = "询价供应商信息")
    List<QuotationSupplier> quotationSupplierList;

    /**
     * 询价单附件需求清单
     */
    @ApiModelProperty(value = "询价单附件需求清单")
    List<QuotationAttRequire> quotationAttRequireList;

    /**
     * 询价单附件需求带编号
     */
    @ApiModelProperty(value = "询价单附件需求清单")
    List<QuotationAttRequireVo> quotationAttRequireVos;

    /**
     * 询价单附件
     */
    @ApiModelProperty(value = "询价单附件")
    List<QuotationAttachment> quotationAttachmentList;


    /**
     * 询价单附件带编号
     */
    @ApiModelProperty(value = "询价单附件带编码")
    List<QuotationAttachmentVo> quotationAttachmentVos;


    /**
     * 询价单应标状态 0未应标未拒绝 1应标 -1拒绝
     */
    @ApiModelProperty("询价单应标状态 0未应标未拒绝 1应标 -1拒绝")
    private Integer quotationMarkStatus;

    /**
     * 上次报价 如果是空的 就是没有上次报价 使用当前价格字段显示 都是普通字段不包含计算字段
     */
    @ApiModelProperty("上次报价")
    private List<BiddingLineExt> lastReportBiddingLineExtList;

    /**
     * 期望价格 如果没有 就是还没有期望报价 使用当前价格字段显示 都是普通字段不包含计算字段
     */
    @ApiModelProperty("期望价格")
    private List<BiddingLineExt> exceptBiddingLineExtList;
    /**
     * 当前价格 以及普通字段信息
     */
    @ApiModelProperty("当前价格 以及普通字段信息")
    private List<BiddingLineExt> quotationLineNormalExts;

    /**
     * 当前价格 计算字段信息
     */
    @ApiModelProperty("当前价格 计算字段信息")
    private List<BiddingLineExt> quotationLineExpressionExts;

    /**
     * 模板数据
     */
    @ApiModelProperty("模板数据")
    private MaterialQuotationTemplate materialQuotationTemplate;

    /**
     * 询价单模板名称
     */
    @ApiModelProperty("询价单模板名称")
    private String templateName;

    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    /**
     * 销售协同 操作按钮
     */
    private QuotationSaleDetailButtonStatus quotationSaleDetailButtonStatus;

    /**
     * 当前轮次当前的供应商是否已经报价  false没有报价 true已经报过价了
     */
    private Boolean quotedOfTheCurrentRound;

}
