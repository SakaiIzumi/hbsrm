package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;


/**
 * <p>
 * 招标行信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_bidding_line_ext")

public class BiddingLineExt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 询价单主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String type;

    /**
     * label名称
     */
    @ApiModelProperty(value = "label名称")
    private String title;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String field;

    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值")
    private String value;

    /**
     * 必填
     */
    @ApiModelProperty(value = "必填")
    private String required;

    /**
     * 排序值
     */
    @ApiModelProperty(value = "排序值")
    private Integer orderValue;

    /**
     * 报价记录ID
     */
    @ApiModelProperty(value = "报价记录ID")
    private Long quotationRecordId;

    /**
     * 供应商ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    /**
     * 报价轮次
     */
    @ApiModelProperty(value = "报价轮次")
    private String quoteRound;


    /**
     * 业务类型，normal 普通，except 期望
     */
    @ApiModelProperty(value = "业务类型，normal 普通，except 期望")
    private String bizType;

    /**
     * 数据类型: normal 普通数据,expression 表达式数据, summary 汇总数据
     */
    @ApiModelProperty(value = "数据类型: normal 普通数据,expression 表达式数据, summary 汇总数据")
    private String dataType;

    /**
     * 计算公式
     */
    @ApiModelProperty(value = "计算公式")
    private String formula;
    /**
     * 表达式
     */
    @ApiModelProperty("表达式")
    private String expression;

    /**
     * 表达式计算结果
     */
    @ApiModelProperty("表达式计算结果")
    private BigDecimal expressionValue;

    /**
     * 字段校验JSON
     */
    @ApiModelProperty(value = "字段校验JSON")
    private String props;

    /**
     * 字段校验规则，根据props字段属性校验
     */
    @ApiModelProperty(value = "字段校验规则，根据props字段属性校验")
    private String validate;

    /**
     * 是否为金额字段，1是，0否
     */
    @ApiModelProperty(value = "是否为金额字段，1是，0否")
    private String isAmount;

    /**
     * 定价显示，true 是，false 否
     */
    private String pricingShow;

    /**
     * 保存表单设计器选择器组件字段
     */
    private String options;

}
