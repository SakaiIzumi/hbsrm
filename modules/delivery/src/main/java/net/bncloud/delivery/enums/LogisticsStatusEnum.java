package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName LogisticsStatusEnum
 * @Description: 物流状态枚举
 * @Author Administrator
 * @Date 2021/4/12
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum LogisticsStatusEnum {
    NOT_SHIPPED("1","未发货"),
    TO_BE_DELIVERED("2","待送达"),
    ARRIVED("3","已送达"),
    ;

    private String code;

    private String name;
}
