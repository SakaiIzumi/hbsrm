package net.bncloud.financial.entity;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 费用信息表  对账单中的费用明细表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_cost_detail")
@ExcelIgnoreUnannotated
public class FinancialCostDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 金蝶ERP单据id
     */
    @ExcelIgnore
    @ApiModelProperty(value = "金蝶ERP单据id")
    private Long erpBillId;
    /**
     * 对账单ID;t_account_statement.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    @ApiModelProperty(value = "对账单ID;t_account_statement.id")
    private Long statementId;

    /**
     * 费用id,t_financial_cost_bill.id
     */
    @ApiModelProperty(value = "费用id")
    @ExcelIgnore
    private Long costBillId;

    /**
     * 费用单号
     */
    @ExcelProperty(value = "费用单")
    @ApiModelProperty(value = "费用单号")
    private String billNo;

    /**
     * ERP费用单号
     */
    @ExcelProperty(value = "费用应付单")
    @ApiModelProperty(value = "ERP费用单号")
    private String erpBillNo;

    /**
     * 项次
     */
    @ExcelProperty(value = "项次")
    @ApiModelProperty(value = "项次")
    private String itemNo;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(value = "单据类型编码")
    @ExcelIgnore
    private String documentTypeCode;

    /**
     * 单据类型名称
     */
    @ExcelProperty(value = "单据类型")
    @ApiModelProperty(value = "单据类型名称")
    private String documentTypeName;

    /**
     * 费用金额
     */
    @ExcelProperty(value = "金额")
    @ApiModelProperty(value = "费用金额")
    private BigDecimal costAmount;

    /**
     * 对账含税金额
     */
    @ExcelProperty(value = "对账含税金额")
    @ApiModelProperty(value = "对账含税金额")
    private BigDecimal checkIncludeTax;

    /**
     * 对账不含税金额
     */
    @ExcelProperty(value = "对账不含税金额")
    @ApiModelProperty(value = "对账不含税金额")
    private BigDecimal checkNotTaxAmount;

    /**
     * 税额
     */
    @ExcelProperty(value = "税额")
    @ApiModelProperty(value = "税额")
    private BigDecimal checkTaxAmount;

    /**
     * 客户确认
     */
    @ApiModelProperty(value = "客户确认")
    @ExcelIgnore
    private String customerConfirm;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "费用说明")
    private String remark;

    /**
     * 费用说明
     */
    @ExcelIgnore
    @ApiModelProperty(value = "费用说明")
    private String costRemark;

}
