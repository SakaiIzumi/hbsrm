package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description  计划排程的计划明细项次
 * @since 2022/5/25
 */
@Accessors(chain = true)
@Data
public class DeliveryPlanDetailItemPage implements Serializable {
    /**
     * 项次列表
     */
    private List<DeliveryPlanDetailItem> detailList;

    ///**
    // * 操作按钮：addition新增，delete删除，edit编辑
    // */
    //private Map<String,Boolean> operationButtons;
}
