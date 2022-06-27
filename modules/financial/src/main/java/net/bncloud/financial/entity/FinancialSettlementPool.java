package net.bncloud.financial.entity;


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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 结算池单据信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_settlement_pool")

public class FinancialSettlementPool extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 金蝶ERP单据id
     */
    @ApiModelProperty(value = "金蝶ERP单据id")
    private Long erpBillId;

    /**
     * 单据ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据ID")
    private Long billId;

    /**
     * 单据编码
     */
    @ApiModelProperty(value = "金蝶ERP单据编码")
    private String erpBillNo;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "金蝶ERP单据类型")
    private String erpBillType;

    /**
     * 单据编码
     */
    @ApiModelProperty(value = "单据编码")
    private String billNo;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    private String billType;

    /**
     * 采购方编码
     */
    @ApiModelProperty(value = "采购方编码")
    private String customerCode;

    /**
     * 采购方名称
     */
    @ApiModelProperty(value = "采购方名称")
    private String customerName;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 币种编码
     */
    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String currencyName;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 送货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期")
    private Date deliveryDate;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private Integer deliveryNum;

    /**
     * 是否含税
     */
    @ApiModelProperty(value = "是否含税")
    private Boolean haveTax;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    /**
     * 确认时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "确认时间")
    private LocalDate confirmTime;

    /**
     * 协作组织编码
     */
    @ApiModelProperty(value = "协作组织编码")
    private String collaborationCode;

    /**
     * 对账状态，N未生成，Y已生成
     */
    @ApiModelProperty(value = "对账状态，N未生成，Y已生成")
    private String statementCreated;

}
