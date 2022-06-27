package net.bncloud.service.api.file.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.math.BigDecimal;

/**
 * @author: liulu
 * @Date: 2022-02-27 20:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_delivery_bill_line")

public class FinancialDeliveryBillLineDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 送货单据id
     */
    @ApiModelProperty(value = "送货单据id")
    private String deliveryBillId;

    /**
     * erp明细id
     */
    @ApiModelProperty(value = "erp明细id")
    private String erpLineId;

    /**
     * 入库单号
     */
    @ApiModelProperty(value = "入库单号")
    private String instockNo;

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
     * 含税单价
     */
    @ApiModelProperty(value = "含税单价")
    private BigDecimal taxIncludedUnitPrice;

    /**
     * 含税金额
     */
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxIncludedAmount;

    /**
     * 未税金额
     */
    @ApiModelProperty(value = "未税金额")
    private BigDecimal notTaxAmount;

    /**
     * 税额
     */
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;


}
