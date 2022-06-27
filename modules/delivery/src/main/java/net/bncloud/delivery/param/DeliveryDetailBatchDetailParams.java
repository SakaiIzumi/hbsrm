package net.bncloud.delivery.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * desc: 批次详情参数
 *
 * @author Rao
 * @Date 2022/03/30
 **/
@Data
public class DeliveryDetailBatchDetailParams implements Serializable {

    private static final long serialVersionUID = 8252824369791764429L;

    /**
     * 送货单ID (若送货单已经保存则需要传递)
     */
    private Long deliveryNoteId;

    /**
     * 送货计划明细项次ID
     */
    @NotNull(message = "未指定送货计划明细项次！")
    private Long deliveryPlanDetailItemId;

}
