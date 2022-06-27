package net.bncloud.financial.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 对账单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_statement")

public class FinancialStatement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 协作组织ID
     */
    private Long orgId;

    /**
     * 协作组织名称
     */
    private String orgName;

    /**
     * 采购方编码
     */
    @NotBlank(message = "客户编码不能为空")
    @ApiModelProperty(value = "采购方/客户编码")
    private String customerCode;

    /**
     * 采购方名称
     */
    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "采购方/客户名称")
    private String customerName;

    /**
     * 审核时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "审核时间")
    private OffsetDateTime auditTime;

    /**
     * 对账单号
     */
    @ApiModelProperty(value = "对账单号")
    private String statementNo;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String auditName;

    /**
     * 供应商编号
     */
    @NotBlank(message = "供应商编号不能为空")
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerId;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createdName;

    /**
     * 开户行名称
     */
    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    /**
     * 对账周期开始时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDate periodStart;

    /**
     * 对账周期截止时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "对账周期截止时间")
    private LocalDate periodEnd;

    /**
     * 币种编码
     */
    @NotBlank(message = "币种编码不能为空")
    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String currencyName;

    /**
     * 付款条件
     */
    @ApiModelProperty(value = "付款条件")
    private String paymentTerms;

    /**
     * 账款到期日
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "账款到期日")
    private Date accountDueDate;


    /**
     * 支付方式编码
     */
    @ApiModelProperty(value = "支付方式编码")
    private String payMethodCode;

    /**
     * 支付方式名称
     */
    @ApiModelProperty(value = "支付方式名称")
    private String payMethodName;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String statementStatus;

    /**
     * 采购方备注
     */
    @ApiModelProperty(value = "采购方备注")
    private String customerMarks;

    /**
     * 供应商备注
     */
    @ApiModelProperty(value = "供应商备注")
    private String supplierMarks;

    /**
     * 来源，purchase 采购方，supplier 供应商
     */
    @ApiModelProperty(value = "来源，purchase 采购方，supplier 供应商")
    private String sourceType;

    /**
     * 送货汇总含税金额
     */
    @ApiModelProperty(value = "送货汇总含税金额")
    private BigDecimal shipmentIncludingTax;

    /**
     * 送货汇总未含税金额
     */
    @ApiModelProperty(value = "送货汇总未含税金额")
    private BigDecimal shipmentNotTax;

    /**
     * 送货汇总税额
     */
    @ApiModelProperty(value = "送货汇总税额")
    private BigDecimal shipmentTaxAmount;


    /**
     * 费用汇总含税金额
     */
    @ApiModelProperty(value = "费用汇总含税金额")
    private BigDecimal costIncludingTax;

    /**
     * 费用汇总未含税金额
     */
    @ApiModelProperty(value = "费用汇总未含税金额")
    private BigDecimal costNotIncludingTax;

    /**
     * 费用汇总税额
     */
    @ApiModelProperty(value = "费用汇总税额")
    private BigDecimal costTaxAmount;

    /**
     * 对账含税金额
     */
    @ApiModelProperty(value = "对账含税金额")
    private BigDecimal statementIncludingTax;

    /**
     * 未税金额
     */
    @ApiModelProperty(value = "对账不含税金额")
    private BigDecimal statementNotTax;

    /**
     * 对账税额
     */
    @ApiModelProperty(value = "对账税额")
    private BigDecimal statementTaxAmount;

    /**
     * 确认送货金额
     */
    @ApiModelProperty(value = "确认送货金额")
    private BigDecimal checkShipmentAmount;

    /**
     * 确认费用金额
     */
    @ApiModelProperty(value = "确认费用金额")
    private BigDecimal checkCostAmount;

    /**
     * 确认对账含税金额
     */
    @ApiModelProperty(value = "确认对账含税金额")
    private BigDecimal checkStatementIncludingTax;

    /**
     * 确认未税金额
     */
    @ApiModelProperty(value = "确认对账不含税金额")
    private BigDecimal checkStatementNotTax;

    /**
     * 确认对账税额
     */
    @ApiModelProperty(value = "确认对账税额")
    private BigDecimal checkStatementTaxAmount;

}
