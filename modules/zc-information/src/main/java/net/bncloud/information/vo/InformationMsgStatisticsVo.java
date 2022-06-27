package net.bncloud.information.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName DeliveryStatisticsVo
 * @Description: 消息数量
 * @Author Administrator
 * @Date 2021/3/18
 * @Version V1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformationMsgStatisticsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 代办消息数量
     */
    @ApiModelProperty(value = "代办消息数量")
    private Integer agentMessages;

    /**
     * 待签收的数量
     */
    @ApiModelProperty(value = "通知")
    private Integer notice;

    /**
     * 待签收的数量
     */
    @ApiModelProperty(value = "未读消息")
    private Integer UnReadCount;

    /**
     * 待签收的数量
     */
    @ApiModelProperty(value = "供应商管理")
    private Integer supplierCount;

    /**
     * 客户管理
     */
    @ApiModelProperty(value = "客户管理")
    private Integer customerCount;


    /**
     * 采购订单协同
     */
    @ApiModelProperty(value = "采购订单协同")
    private Integer purchaseOrderCollaborationCount;

    /**
     * 客户订单协同
     */
    @ApiModelProperty(value = "客户订单协同")
    private Integer customerOrderCollaborationCount;



    /**
     * 采购收货协同
     */
    @ApiModelProperty(value = "采购收货协同")
    private Integer purchasingReceivingCollaborationCount;

    /**
     * 供应出货协同
     */
    @ApiModelProperty(value = "供应出货协同")
    private Integer supplyDeliveryCollaborationCount;
    //        SUPPLIER_MANAGEMENT("1","供应商管理"),
//                CUSTOMER_MANAGEMENT("2","客户管理"),
//                PURCHASE_ORDER_COLLABORATION("3","采购订单协同"),
//                CUSTOMER_ORDER_COLLABORATION("4","客户订单协同"),
//                PURCHASING_RECEIVING_COLLABORATION("5","采购收货协同"),
//                SUPPLY_DELIVERY_COLLABORATION("6","供应出货协同");




}
