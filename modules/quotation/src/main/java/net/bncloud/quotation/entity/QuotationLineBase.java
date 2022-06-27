package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 询价行基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_line_base")

public class QuotationLineBase extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价基础信息主键ID
     */
    @NotNull(message = "询价基础信息主键ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价基础信息主键ID")
    private Long quotationBaseId;

    /**
     * 物料报价模板ID
     */
    @NotNull(message = "物料报价模板不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "物料报价模板ID")
    private Long templateId;

    /**
     * 税别
     */
    @ApiModelProperty(value = "税别")
    private String taxType;

    /**
     * 是否含税
     */
    @ApiModelProperty(value = "是否含税")
    private String haveTax;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    /**
     * 付款条件
     */
    @ApiModelProperty(value = "付款条件")
    private String paymentTerm;

    /**
     * 付款周期
     */
    @ApiModelProperty(value = "付款周期")
    private String paymentPeriod;

    /**
     * 报价、公式内容，JSON
     */
    @ApiModelProperty(value = "报价、公式内容，JSON")
    private String extContent;

}
