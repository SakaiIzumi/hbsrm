package net.bncloud.financial.entity;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
import net.bncloud.financial.service.excelconverter.HaveTaxConverter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 送货明细信息表  对账单里的送货明细
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_delivery_detail")
@ExcelIgnoreUnannotated
public class FinancialDeliveryDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 金蝶ERP单据id
     */
    @ApiModelProperty(value = "金蝶ERP单据id")
    @ExcelIgnore
    private Long erpBillId;

    /**
     * 对账单ID;t_account_statement.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ExcelIgnore
    @ApiModelProperty(value = "对账单ID;t_account_statement.id")
    private Long statementId;

    /**
     * 送货id
     */
    @ExcelIgnore
    @ApiModelProperty(value = "送货id")
    private Long deliveryBillId;

    /**
     * 项次
     */
    @ExcelProperty("项次")
    @ApiModelProperty(value = "项次")
    private String itemNo;


    /**
     * 送货单(待定)
     */
    @ExcelProperty("送货单号")
    @ApiModelProperty(value = "送货单号")
    private String billNo;

    /**
     * ERP标准应付单号
     */
    @ExcelProperty("ERP标准应付单号")
    @ApiModelProperty(value = "ERP标准应付单号")
    private String erpBillNo;


    /**
     * 送货数量
     */
    @ExcelProperty("送货数量")
    @ApiModelProperty(value = "送货数量")
    private Integer deliveryNum;

    /**
     * 送货金额
     */
    @ExcelProperty("送货金额")
    @ApiModelProperty(value = "送货金额")
    private BigDecimal deliveryAmount;

    /**
     * 送货日期
     */
    @ExcelProperty("送货日期")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期")
    private Date deliveryDate;

    /**
     * 对账金额
     */
    @ExcelProperty(value = "送货金额")
    @ApiModelProperty(value = "对账金额")
    private BigDecimal checkAmount;

    /**
     * 是否含税
     */
    @ExcelProperty(value = "是否含税",converter = HaveTaxConverter.class)
    @ApiModelProperty(value = "是否含税")
    private Boolean haveTax;

    /**
     * 税率
     */
    @ExcelProperty(value = "税率")
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    /**
     * 确认金额
     */
    @ExcelIgnore
    @ApiModelProperty(value = "确认金额")
    private BigDecimal confirmedAmount;

    /**
     * 对账数量
     */
    @ExcelProperty(value = "对账数量")
    @ApiModelProperty(value = "对账数量")
    private Integer checkQuantity;

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
    @ExcelProperty(value = "对账税额")
    @ApiModelProperty(value = "对账税额")
    private BigDecimal checkTaxAmount;

    /**
     * 客户确认
     */
    @ExcelIgnore
    @ApiModelProperty(value = "客户确认")
    private String customerConfirm;

    /**
     * 备注
     */
    @ExcelIgnore
    @ExcelProperty(value = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 送货备注
     */
    @ExcelIgnore
    @ApiModelProperty(value = "送货备注")
    private String deliveryRemark;

}
