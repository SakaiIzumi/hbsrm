package net.bncloud.delivery.enums;


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
public enum DeliveryOperateType {
    APPLY_DELIVERY("apply_delivery","申请发货"),
    INVALID_APPLY("invalid_apply","作废申请"),
    WITHDRAW_APPLY("withdraw_apply","撤回申请"),
    CONFIRMATION_ISSUED("confirmation_issued","确认发出"),
    WITHDRAW_DELIVERY("withdraw_delivery","撤回送货"),
    DELIVERED("delivered","货物已发"),
    INVALID_DELIVERY("invalid_delivery","作废送货"),
    REMIND("remind","提醒"),
    AGREE_DELIVERY("agree_delivery","同意发货"),
    RETURN_APPLY("return_apply","退回申请"),
    RETURN_DELIVERY("return_delivery","退回送货"),
    GOOD_DELIVERED("good_delivered","货物送达"),
    CONFIRM_RECEIPT("confirm_receipt","确认签收"),
    WITHDRAW_RECEIPT("withdraw_receipt","撤回签收"),
    PRINT("print","打印"),
    DELETE("delete","删除"),
    SAVE("save","保存"),
    CANCEL("cancel","取消"),
    AGREE("agree","同意"),
    REFUSED("refused","拒绝"),
    EDIT("edit","编辑"),
    MODIFY("modify","修改");

    private String code;

    private String name;
}