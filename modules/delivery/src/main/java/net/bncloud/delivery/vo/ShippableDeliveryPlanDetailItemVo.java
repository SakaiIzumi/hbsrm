package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * desc: 可发货送货计划项次
 *
 * @author Rao
 * @Date 2022/03/10
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ShippableDeliveryPlanDetailItemVo extends DeliveryPlanDetailItemVo implements Serializable {
    private static final long serialVersionUID = 5186989390749753516L;

    /**
     * 产品编码
     */
    private String productCode;

    // 送货计划

    /**
     * 计划编号
     */
    private String planNo;

    /**
     * 采购单号
     */
    private String purchaseOrderCode;

    /**
     * 项次可超出的值
     */
    private transient int excessValue = 0;

    /**
     * 添加
     * @param count
     */
    public void addExcessValue(int count){
        excessValue += count;
    }


}
