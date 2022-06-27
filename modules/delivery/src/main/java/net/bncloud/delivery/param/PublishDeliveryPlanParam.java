package net.bncloud.delivery.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ddh
 * @description
 * @since 2022/6/9
 */
@Data
public class PublishDeliveryPlanParam implements Serializable {

    /**
     * 计划id
     */
    @NotNull(message = "送货计划id不能为空！")
    private Long deliveryPlanId;

    /**
     * 计划说明
     */
    private String planDescription;
}
