package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 物料报价模板
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_material_quotation_template")

public class MaterialQuotationTemplate extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 模板编号
     */
    @ApiModelProperty(value = "模板编号")
    private String templateCode;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    /**
     * 模板描述
     */
    @ApiModelProperty(value = "模板描述")
    private String templateDescription;

    /**
     * 物料表单ID
     */
    @ApiModelProperty(value = "物料表单ID")
    private String materialFormId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 物料编码
     */
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
     * 模板扩展内容
     */
    @ApiModelProperty(value = "模板扩展内容")
    private String extContent;


    /**
     * 创建人姓名
     */
    private String createdName;

    /**
     * 模板关联的表单的名字
     */
    private String formName;
}
