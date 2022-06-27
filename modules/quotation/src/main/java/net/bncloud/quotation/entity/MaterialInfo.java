package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

/**
 * <p>
 * 物料信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_material_info")
public class MaterialInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 物料条码
     */
    @ApiModelProperty(value = "物料条码")
    private String barCode;

    /**
     * 分类编码ID
     */
    @ApiModelProperty(value = "分类编码ID")
    private String genreCodeId;

    /**
     * 分类编码
     */
    @ApiModelProperty(value = "分类编码")
    private String genreCode;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "物料分类")
    private String genreName;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格")
    private String special;

    /**
     * 物料描述
     */
    @ApiModelProperty(value = "物料描述")
    private String description;

    /**
     * 单位
     */
    @ApiModelProperty(value = "基本单位")
    private String unit;

    /**
     * 来源类型
     */
//    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    /**
     * 来源名称
     */
//    @ApiModelProperty(value = "来源名称")
    private String sourceName;

    /**
     * 来源系统ID
     */
    @ApiModelProperty(value = "来源系统ID")
    private Long sourceId;

    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 物料属性
     */
    @ApiModelProperty(value = "物料属性")
    private String property;

    /**
     * 物料分组名称
     */
    @ApiModelProperty("物料分组名称")
    private String materialGroupName;

    /**
     * 物料分组编码
     */
    @ApiModelProperty("物料分组编码")
    private Integer materialGroupId;

    /**
     * 标准工时
     */
    @ApiModelProperty("标准工时")
    private String unitStandHour;

    /**
     * 是否套件
     */
    @ApiModelProperty("是否套件")
    private String suite;

    /**
     * 发料方式
     */
    @ApiModelProperty("发料方式")
    private String issueType;

    /**
     * 税分类
     */
    @ApiModelProperty("税分类")
    private String taxType;

    /**
     * 默认税率
     */
    @ApiModelProperty("默认税率")
    private String taxRate;

    /**
     * 禁用状态：0有效，1无效
     */
    private String forbiddenStatus;

}
