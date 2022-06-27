package net.bncloud.delivery.param;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 送货明细表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryDetailParam extends DeliveryDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划planId
     */
    @ApiModelProperty(value = "计划planId")
    private Long deliveryPlanId ;



}
