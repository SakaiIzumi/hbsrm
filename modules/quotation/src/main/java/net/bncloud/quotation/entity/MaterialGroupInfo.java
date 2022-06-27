package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

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
@TableName("t_material_group_info")
public class MaterialGroupInfo extends BaseEntity {


    private static final long serialVersionUID = 1L;
    /**
     *ERP组ID
     */
    @ApiModelProperty("ERP组ID")
    private Long erpId;
    /**
     *同步ERP暂未知字段用意
     */
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpLeft;
    /**
     *ERP组父ID
     */
    @ApiModelProperty("ERP组父ID")
    private Long erpParentId;
    /**
     *同步ERP暂未知字段用意
     */
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpNullParentId;
    /**
     *编码
     */
    @ApiModelProperty("编码")
    private String erpNumber;
    /**
     *分组ID
     */
    @ApiModelProperty("分组ID")
    private String erpGroupId;
    /**
     *同步ERP暂未知字段用意
     */
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpRight;
    /**
     *分组名称
     */
    @ApiModelProperty("分组名称")
    private String erpName;
}
