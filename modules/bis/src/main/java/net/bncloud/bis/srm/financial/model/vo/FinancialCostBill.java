package net.bncloud.bis.srm.financial.model.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用单据信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("费用单据信息表")
@TableName("t_financial_cost_bill")
public class FinancialCostBill extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 协助组织
     */
    @ApiModelProperty(value = "协作组织id")
    private Long orgId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码")
    @NotBlank(message = "客户编码不能为空")
    private String customerCode;


    /**
     * 供应商编码
     * */
    @ApiModelProperty(value = "供应商编码")
    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode ;

    /**
     * 供应商名称
     * */
    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName ;


    /**
     * 费用单号
     */
    @ApiModelProperty(value = "费用单号，自动生成")
    private String costBillNo;


    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;



    /**
     * 发布人名称
     */
    @ApiModelProperty(value = "发布人名称")
    private String publisher;

    /**
     * 币种编码
     */
    @ApiModelProperty(value = "币种编码")
    @NotBlank(message = "币种编码不能为空")
    private String currencyCode;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String currencyName;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRatio;

    /**
     * 应付凭证
     */
    @ApiModelProperty(value = "应付凭证")
    private String certificatePayable;

    /**
     * 费用周期开始时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "费用周期开始时间")
    private LocalDateTime costPeriodStart;

    /**
     * 费用周期结束时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "费用周期结束时间")
    private LocalDateTime costPeriodEnd;


    /**
     * 总账凭证
     */
    @ApiModelProperty(value = "总账凭证")
    private String generalCredential;

    /**
     * 立账状态
     */
    @ApiModelProperty(value = "立账状态")
    private String bookBuilding;

    /**
     * 单据状态
     */
    @ApiModelProperty(value = "单据状态：1草稿、2待供应方确认、3待采购方确认、4已退回、5已确认、6作废、7已撤回")
    private String billStatus;

    /**
     * 费用单类型
     */
    @ApiModelProperty(value = "费用单类型:0一般费用单（目前只有一种）")
    private String costBillType;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String sourceType;

    /**
     * 发票开具状态;Y已开票，N未开票
     */
    @ApiModelProperty(value = "发票开具状态;Y已开票，N未开票")
    private String invoiceStatus;

    /**
     *结算池，入池状态，N未入池，Y已入池
     */
    @ApiModelProperty(value = "结算池，入池状态，N未入池，Y已入池")
    private String settlementPoolSyncStatus;

    /**
     * 合计金额
     */
    @ApiModelProperty(value = "合计金额")
    private BigDecimal costAmount;

    /**
     * 确认时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    /**
     * 确认人名称
     */
    @ApiModelProperty(value = "确认人名称")
    private String confirmName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerIdentificationNumber;

    /**
     * 开户行名称
     */
    @ApiModelProperty(value = "开户行名称")
    private String openingBank;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    private String bankAccountNumber;

    /**
     * 抵扣发票：Y抵扣，N不抵扣
     */
    @ApiModelProperty(value = "抵扣发票：Y抵扣，N不抵扣")
    private String offsetInvoice;

    /**
     * 是否内部：Y是，N不是
     */
    @ApiModelProperty(value = "是否内部：Y是，N不是")
    private String internal;

    @ApiModelProperty(value = "费用明细列表")
    private List<FinancialCostBillLine> costBillLineList;
}
