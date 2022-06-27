package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author ddh
 * @description 计划排程明细项次
 * @since 2022/6/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanSchedulingDetailItem extends DeliveryPlanDetailItem implements Serializable {

    /**
     * 送货日期
     */
    private LocalDate deliveryLocalDate;


    private LocalDate confirmDeliveryLocalDate;
}
