package net.bncloud.service.api.delivery.dto;

import lombok.Data;
import net.bncloud.service.api.delivery.entity.DeliveryPlanEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 送货计划信息同步 DTO
 * @author Toby
 */
@Data
public class DeliveryPlanDTO implements Serializable {

    private static final long serialVersionUID = 8279907220635289693L;
    private DeliveryPlanEntity deliveryPlanEntity;

    private List<DeliveryPlanDetailEntity> planDetailEntityList;
}
