package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * desc: 工厂类型
 *
 * @author Rao
 * @Date 2022/06/17
 **/
@AllArgsConstructor
@Getter
public enum FactoryTypeEnum {

    receipt("收货厂"),

    delivery("发货厂"),

    ;

    private final String desc;
}
