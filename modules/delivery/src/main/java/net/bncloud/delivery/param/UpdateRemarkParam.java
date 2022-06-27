package net.bncloud.delivery.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ddh
 * @description
 * @since 2022/6/14
 */
@Data
public class UpdateRemarkParam implements Serializable {

    /**
     * 计划明细详情id
     */
    @NotNull
    private Long deliveryPlanDetailId;
    /**
     * 备注（供应商备注或采购方备注）
     */
    private String remark;
}
