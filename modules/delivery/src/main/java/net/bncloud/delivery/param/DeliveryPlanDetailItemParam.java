package net.bncloud.delivery.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * <p>
 * 送货计划明细批次表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@ApiModel(value = "送货计划看板请求参数")
public class DeliveryPlanDetailItemParam extends DeliveryPlanDetailItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商模糊查询条件
     */
    @ApiModelProperty(value = "供应商")
    @Length(min = 0, max = 255)
    private String supplierCondition;
    /**
     * 采购方模糊查询条件
     */
    @ApiModelProperty(value = "采购方")
    @Length(min = 0, max = 255)
    private String customerCondition;
    /**
     * 产品模糊查询条件
     */
    @ApiModelProperty(value = "产品")
    @Length(min = 0, max = 255)
    private String productCondition;


}
