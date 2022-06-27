package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 物料表单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_material_form")

public class MaterialForm extends BaseEntity {

	private static final long serialVersionUID = 1L;

    /**
     * 物料表单名称
     */
    @NotBlank(message = "物料表单名称不能为空")
    @ApiModelProperty(value = "物料表单名称")
    private String formName;


    /**
     * 物料名称
     */
    @NotBlank(message = "物料名称不能为空")
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 物料编码
     */
    @NotBlank(message = "物料编码不能为空")
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String special;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 相关说明
     */
    @ApiModelProperty(value = "相关说明")
    private String description;

    /**
     * 表单内容
     */
    @ApiModelProperty(value = "表单内容")
    private String extContent;

    /**
     * 表单option
     */
    private String formOption;

    /**
     * 表单rule
     */
    private String formRule;

    /**
     * 创建人名字
     */
    private String createName;

}
