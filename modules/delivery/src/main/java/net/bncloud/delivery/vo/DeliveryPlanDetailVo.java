package net.bncloud.delivery.vo;


import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
public class DeliveryPlanDetailVo extends DeliveryPlanDetail implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 送货计划明细批次列表
     */
    private List<DeliveryPlanDetailItem> detailItemList;

}
