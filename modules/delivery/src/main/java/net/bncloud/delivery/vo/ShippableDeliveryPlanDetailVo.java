package net.bncloud.delivery.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc: 可发货的送货计划明细
 *
 * @author Rao
 * @Date 2022/03/10
 **/
@Data
public class ShippableDeliveryPlanDetailVo implements Serializable {
    private static final long serialVersionUID = 2998313382423436240L;

    /**
     * 送货计划明细ID
     */
    private Long deliveryPlanDetailId;

    /**
     * 送货计划ID
     */
    private Long deliveryPlanId;

    /**
     * 剩余可发货数量
     */
    private Integer remainingQuantityTotal;

    /**
     * 物料编码
     */
    private String productCode;

    /**
     * 物料名称
     */
    private String productName;

    /**
     * 条码 (商家编码)
     */
    private String merchantCode;

    /**
     * 物料分类
     */
    private String materialType;

    /**
     * 订单类型
     */
    private String orderType;

}
