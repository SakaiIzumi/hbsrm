package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.DeliveryPlanDetail;

import java.io.Serializable;

/**
 * @author ddh
 * @description 计划排程明细视图
 * @since 2022/6/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanSchedulingDetailVo extends DeliveryPlanDetail implements Serializable {

    /**
     * 采购方编码
     */
    private String purchaseCode;

    /**
     * 采购方名称
     */
    private String purchaseName;
}
