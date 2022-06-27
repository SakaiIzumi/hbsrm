package net.bncloud.delivery.vo;


import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.delivery.entity.DeliveryPlanDetail;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 送货计划基础信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
@Data
public class DeliveryPlanVo extends DeliveryPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货计划明细列表
     */
    private List<DeliveryPlanDetail> detailList;



}
