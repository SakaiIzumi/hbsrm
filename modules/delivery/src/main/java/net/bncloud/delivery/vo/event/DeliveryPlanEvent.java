package net.bncloud.delivery.vo.event;

import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.FileInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/17
 */
@Data
public class DeliveryPlanEvent extends DeliveryPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    ///**
    // * 送货明细
    // */
    //private List<DeliveryDetail> deliveryDetailList;



    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    private Long deliveryPlanId;

    private Long businessId;

    private String addTime = DateUtil.formatDateTime(new Date());
}
