package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DeliveryStatusOperateRel
 * @Description: 送货单状态与可执行操作关联关系枚举
 * @Author liulu
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
@Slf4j
public enum DeliveryStatusOperateRel {
    //
    DRAFT("1","草稿",new DeliveryOperateType[]{
        DeliveryOperateType.APPLY_DELIVERY,
        DeliveryOperateType.DELETE,
        DeliveryOperateType.EDIT,
        DeliveryOperateType.PRINT,
        DeliveryOperateType.CONFIRMATION_ISSUED

    }),

    TO_SEND_GOODS("2","待发货",new DeliveryOperateType[]{
            DeliveryOperateType.EDIT,
            DeliveryOperateType.DELETE,
            DeliveryOperateType.CONFIRMATION_ISSUED

    }),

    APPLYING("3","申请中",new DeliveryOperateType[]{
        DeliveryOperateType.INVALID_APPLY,
        DeliveryOperateType.WITHDRAW_APPLY,
        DeliveryOperateType.REMIND
    }),

    APPLICATION_INVALIDATION("4","申请作废",new DeliveryOperateType[]{}),

    APPLICATION_WITHDRAWAL("5","申请撤回",new DeliveryOperateType[]{
        DeliveryOperateType.APPLY_DELIVERY,
        DeliveryOperateType.EDIT,
        DeliveryOperateType.INVALID_APPLY
    }),

    APPLICATION_RETURN("6","申请退回",new DeliveryOperateType[]{DeliveryOperateType.APPLY_DELIVERY,
            DeliveryOperateType.EDIT,
            DeliveryOperateType.INVALID_APPLY,
            DeliveryOperateType.APPLY_DELIVERY}),

    APPROVED("7","已同意",new DeliveryOperateType[]{DeliveryOperateType.CONFIRMATION_ISSUED}),

    PARTIALLY_AGREE("8","部分同意",new DeliveryOperateType[]{DeliveryOperateType.CONFIRMATION_ISSUED}),

    TO_BE_SIGNED("9","待签收",new DeliveryOperateType[]{
            DeliveryOperateType.MODIFY,
            DeliveryOperateType.PRINT
    }),

    DELIVERY_WITHDRAWAL("10","送货撤回",new DeliveryOperateType[]{
        DeliveryOperateType.EDIT,
        DeliveryOperateType.INVALID_DELIVERY,
        DeliveryOperateType.CONFIRMATION_ISSUED
    }),

    DELIVERY_INVALIDATION("11","送货作废",new DeliveryOperateType[]{
        DeliveryOperateType.EDIT,
        DeliveryOperateType.CONFIRMATION_ISSUED
    }),

    DELIVERY_RETURN("12","送货退回",new DeliveryOperateType[]{DeliveryOperateType.EDIT,
        DeliveryOperateType.INVALID_DELIVERY,
        DeliveryOperateType.EDIT,
        DeliveryOperateType.CONFIRMATION_ISSUED}),

    COMPLETED("13","已完成",new DeliveryOperateType[]{}),

    FROZEN("14","已冻结",new DeliveryOperateType[]{}),

    SIGNING_AND_CONFIRMING("15","签收确认中",new DeliveryOperateType[]{DeliveryOperateType.WITHDRAW_RECEIPT}),
    ;

    private String code;

    private String name;

    private DeliveryOperateType[] operations;

    /**
     * 根据送货单状态获取可执行操作
     * @param deliveryStatusCode
     * @return
     */
    public static Map<String,Boolean> operations(String  deliveryStatusCode,String logisticsStatus){
        Map<String,Boolean> permissions = new HashMap<>();
        //初始化
        for(DeliveryOperateType deliveryOperateType:DeliveryOperateType.values()){
            permissions.put(deliveryOperateType.getCode(),false);
        }
        for(DeliveryStatusOperateRel deliveryStatusOperateRel:DeliveryStatusOperateRel.values()){
            if(deliveryStatusOperateRel.getCode().equals(deliveryStatusCode)){
                DeliveryOperateType [] operations = deliveryStatusOperateRel.getOperations();
                //相同的时候取出里面的对应的操作，比如草稿状态对应1，取出草稿的所有操作，设置为true
                for (DeliveryOperateType operation : operations) {
                    permissions.put(operation.getCode(),true);
                }
                //设置为正确之后，这个应该是设置送货的状态的
                if(DeliveryStatusOperateRel.TO_BE_SIGNED.code.equals(deliveryStatusCode) && ObjectUtils.nullSafeEquals(logisticsStatus,LogisticsStatusEnum.NOT_SHIPPED.getCode())){
                    permissions.put(DeliveryOperateType.WITHDRAW_DELIVERY.getCode(),false);
                    permissions.put(DeliveryOperateType.DELIVERED.getCode(),false);
                    permissions.put(DeliveryOperateType.REMIND.getCode(),false);
                }/*else if(ObjectUtils.nullSafeEquals(logisticsStatus,LogisticsStatusEnum.ARRIVED.getCode())){
                    permissions.put(DeliveryOperateType.CONFIRM_RECEIPT.getCode(),true);
                }*/
            }
        }
        return permissions;
    }
}