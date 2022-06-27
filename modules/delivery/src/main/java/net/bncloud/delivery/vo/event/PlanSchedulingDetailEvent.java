package net.bncloud.delivery.vo.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.common.util.DateUtil;
import net.bncloud.delivery.entity.DeliveryPlanDetail;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author ddh
 * @description
 * @since 2022/6/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanSchedulingDetailEvent extends DeliveryPlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    private Long deliveryPlanDetailId;

    private Long businessId;

    private String addTime = DateUtil.formatDateTime(new Date());
}
