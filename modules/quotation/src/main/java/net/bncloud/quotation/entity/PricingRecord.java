package net.bncloud.quotation.entity;


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
 * 定价请示记录信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_pricing_record")

public class PricingRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 询价单号
     */
    @ApiModelProperty(value = "询价单号")
    private String quotationNo;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    /**
     * 报价记录主键ID
     */
    @ApiModelProperty(value = "报价记录主键ID")
    private String quotationRecordId;

    /**
     * 配额信息
     */
    @ApiModelProperty(value = "配额信息")
    private BigDecimal quoteAmount;

}
