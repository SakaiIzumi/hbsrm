package net.bncloud.financial.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 结算池费用明细信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_cost_bill_line")
public class FinancialCostBillLine extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "费用单据id")
    private String costBillId;

    /**
     * erp明细id
     */
    @ApiModelProperty(value = "erp明细id")
    private String erpLineId;
    /**
     * 费用摘要
     */
    @ApiModelProperty(value = "费用摘要")
    private String costSummary;

    /**
     * 费用名称
     */
    @ApiModelProperty(value = "费用名称")
    private String costName;

    /**
     * 费用原因
     */
    @ApiModelProperty(value = "费用原因")
    private String costReason;


    /**
     * 含税单价
     */
    @ApiModelProperty(value = "含税单价")
    private BigDecimal taxIncludedUnitPrice;

    /**
     * 计价数量
     */
    @ApiModelProperty(value = "计价数量")
    private Integer valuationNum;


    /**
     * erp费用编码
     */
    @ApiModelProperty(value = "erp费用编码")
    private String erpCostId;

    /**
     * 含税金额
     */
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxIncludedAmount;

    /**
     * 对账不含税金额
     */
    @ApiModelProperty(value = "对账不含税金额")
    private BigDecimal notTaxAmount;

    /**
     * 税额
     */
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    /**
     * 是否含税
     */
    @ApiModelProperty(value = "是否含税")
    private Boolean haveTax;
}
