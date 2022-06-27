package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 物料表单扩展信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_material_form_ext")

public class MaterialFormExt extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 物料表单ID，t_rfq_material_form.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "物料表单ID，t_rfq_material_form.id")
    private Long materialFormId;

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
     * 必填，1是，0否
     */
    @ApiModelProperty(value = "必填，1是，0否")
    private String required;

    /**
     * 排序值，升序
     */
    @ApiModelProperty(value = "排序值，升序")
    private Integer orderValue;

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
