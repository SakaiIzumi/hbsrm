package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName DeliveryStatusEnum
 * @Description: 送货状态枚举类
 * @Author Administrator
 * @Date 2021/3/18
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum DeliveryStatusEnum {
    /*送货状态：
    1)草稿
    2)待发货
    3)申请中
    4)申请作废
    5)申请撤回
    6)申请退回
    7)已同意
    8)部分同意
    9)待签收
    10)送货撤回
    11)送货作废
    12)送货退回
    13)已完成
    14)已冻结
    15)签收确认中*/
    DRAFT("1","草稿"),
    TO_SEND_GOODS("2","待发货"),
    APPLYING("3","申请中"),
    APPLICATION_INVALIDATION("4","申请作废"),
    APPLICATION_WITHDRAWAL("5","申请撤回"),
    APPLICATION_RETURN("6","申请退回"),
    APPROVED("7","已同意"),
    PARTIALLY_AGREE("8","部分同意"),
    TO_BE_SIGNED("9","待签收"),
    DELIVERY_WITHDRAWAL("10","送货撤回"),
    DELIVERY_INVALIDATION("11","送货作废"),
    DELIVERY_RETURN("12","送货退回"),
    COMPLETED("13","已完成"),
    FROZEN("14","已冻结"),
    SIGNING_AND_CONFIRMING("15","签收确认中"),
    ;

    private String code;

    private String name;
}