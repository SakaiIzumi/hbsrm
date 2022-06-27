package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * <p>
 * 询价基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_base")

public class QuotationBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 询价单号
     */
    @ApiModelProperty(value = "询价单号")
    @NotBlank(groups = Update.class,message = "询价单号不能为空")
    private String quotationNo;

    /**
     * 询价标题
     */
    @NotBlank(message = "询价标题不能为空")
    @ApiModelProperty(value = "询价标题")
    private String title;

    /**
     * 采购公司编码
     */
    @NotBlank(message = "采购公司编码不能为空")
    @ApiModelProperty(value = "采购公司编码")
    private String customerCode;

    /**
     * 采购公司名称
     */
    @NotBlank(message = "采购公司名称不能为空")
    @ApiModelProperty(value = "采购公司名称")
    private String customerName;

    /**
     * 询价类别
     */
    @NotBlank(message = "询价类别不能为空")
    @ApiModelProperty(value = "询价类别")
    private String quotationType;

    /**
     * 询价开始时间
     */
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime quotationStartTime;

    /**
     * 报价截止时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "报价截止时间")
    private Date cutOffTime;

    /**
     * 期望交期时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "期望交期时间")
    private Date deliveryTime;

    /**
     * 重新报价次数
     */
    @NotNull(message = "重新报价次数不能为空")
    @ApiModelProperty(value = "重新报价次数")
    private Integer roundNumber;

    /**
     * 当前报价轮次，默认1
     */
    @ApiModelProperty(value = "当前报价轮次，默认1")
    private Integer currentRoundNumber;

    /**
     * 创建询价是否需要审批，1是，0否
     */
    @ApiModelProperty(value = "创建询价是否需要审批，1是，0否")
    private String quotationApprove;

    /**
     * 开标是否需要审批，1是，0否
     */
    @ApiModelProperty(value = "开标是否需要审批，1是，0否")
    private String bidApprove;

    /**
     * 定价是否需要审批，1 是，0否
     */
    @ApiModelProperty(value = "定价是否需要审批，1 是，0否")
    private String pricingApprove;

    /**
     * 询价范围，open 公开，specified 指定
     */
    @ApiModelProperty(value = "询价范围，open 公开，specified 指定")
    private String quotationScope;

    /**
     * 报价截止前是否可以查看价格，1是 ，0 否
     */
    @ApiModelProperty(value = "报价截止前是否可以查看价格，1是 ，0 否")
    private String viewPrice;

    /**
     * 是否可以提前结束报价，1是，0否
     */
    @ApiModelProperty(value = "是否可以提前结束报价，1是，0否")
    private String closingQuotationEarly;

    /**
     * 是否需要开标，1是，0否
     */
    @ApiModelProperty(value = "是否需要开标，1是，0否")
    private String needOpenBid;

    /**
     * 采购开标人员ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "采购开标人员ID")
    private Long purchaseEmployeeId;

    /**
     * 采购开标人员UserID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "采购开标人员UserID")
    private Long purchaseUserId;

    /**
     * 采购开标人员名称
     */
    @ApiModelProperty(value = "采购开标人员名称")
    private String purchaseName;

    /**
     * 财务开标人员ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "财务开标人员UserID")
    private Long financialEmployeeId;

    /**
     * 财务开标人员UserID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "财务开标人员UserID")
    private Long financialUserId;

    /**
     * 财务开标人员名称
     */
    @ApiModelProperty(value = "财务开标人员名称")
    private String financialUserName;

    /**
     * 审计开标人员ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "审计开标人员ID")
    private Long auditEmployeeId;

    /**
     * 审计开标人员UserID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "审计开标人员UserID")
    private Long auditUserId;

    /**
     * 审计开标人员名称
     */
    @ApiModelProperty(value = "审计开标人员名称")
    private String auditUserName;

    /**
     * 响应的供应商数量
     */
    @ApiModelProperty(value = "响应的供应商数量")
    private Integer responseNum;


    /**
     * 报价的供应商数量
     */
    @ApiModelProperty(value = "报价的供应商数量")
    private Integer biddingNum;


    /**
     * 供应商数量
     */
    @ApiModelProperty(value = "供应商数量")
    private Integer supplierNum;


    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME_MINUTE)
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String publisher;

    /**
     * 询价单状态
     */
    @ApiModelProperty(value = "询价单状态")
    private String quotationStatus;

    /**
     * 供应商预警，open 开启，close 关闭
     */
    @ApiModelProperty(value = "供应商预警")
    private String supplierWarningSwitch;

    /**
     * 是否开启预警 1 是, 0 否
     */
    @ApiModelProperty(value = "是否已发送预警 1 是, 0 否")
    private Integer isSendWarning;

    /**
     * 来源单据ID(复制单据)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "来源单据ID(复制单据)")
    private Long sourceQuotationBaseId;

    /**
     * 区分是否为草稿状态下作废  1 是草稿状态下作废
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "区分是否为草稿状态下作废")
    private String draftForObsolete;

}
