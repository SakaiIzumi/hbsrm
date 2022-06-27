package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @description 工厂类型
 * @since 2022/5/17
 */
@Getter
@AllArgsConstructor
public enum PlantTypeEnum {

    RECEIPT_PLANT("receipt","收货厂"),
    DELIVERY_PLANT("delivery","发货厂"),
    ;


    private final String code;
    private final String desc;
}
