package net.bncloud.delivery.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.DeliveryPlan;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 送货计划基础信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryPlanParam extends DeliveryPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分栏：0全部，1待确认，2已确认，3差异待确认 4待发布
     */
    @ApiModelProperty("分栏：0全部，1待确认，2已确认，3差异待确认 4待发布")
    @NotNull
    private String tabCategory;


    /**
     * 采购方
     */
    @ApiModelProperty(value = "采购方")
    @Length(min = 0, max = 255)
    private String purchaseCondition;




}
