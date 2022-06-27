package main.java.net.bncloud.enums;



import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @ClassName DeliveryOperateType
 * @Description: 发货单操作类型枚举
 * @Author liulu
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum ModularType {

//消息模块-0供应商管理-1客户管理-2采购订单协同-3客户订单协同-4采购收货协同-5供应出货协同
    SUPPLIER_MANAGEMENT("1","供应商管理"), //合同协同智采
    CUSTOMER_MANAGEMENT("2","客户管理"),
    PURCHASE_ORDER_COLLABORATION("3","采购订单协同"),
    CUSTOMER_ORDER_COLLABORATION("4","客户订单协同"),
    PURCHASING_RECEIVING_COLLABORATION("5","采购收货协同"),
    SUPPLY_DELIVERY_COLLABORATION("6","供应出货协同"),

    INQUIRY_AND_BIDDING_COLLABORATION("8","询价招标协同"),
    ;



    private String code;

    private String name;
}
