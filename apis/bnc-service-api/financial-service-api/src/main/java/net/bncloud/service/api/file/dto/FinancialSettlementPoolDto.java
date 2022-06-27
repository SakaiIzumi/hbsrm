package net.bncloud.service.api.file.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 结算池单据信息表
 * </p>
 */
@Data

public class FinancialSettlementPoolDto extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 单据ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据ID")
    private Long billId;

    /**
     * 金蝶ERP单据id
     */
    @ApiModelProperty(value = "金蝶ERP单据id")
    private Long erpBillId;

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
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

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
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "确认时间")
    private Date confirmTime;

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
