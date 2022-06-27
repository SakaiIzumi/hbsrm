package net.bncloud.serivce.api.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName SupplierRelevanceStatusEnum
 * @Description: 供应商状态枚举类
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum OrderChangeEnum {
    INVALID("Y","产生变化"),
    RELEVANCE("N","无变化"),

    ;
    private String code;
    private String name;
}
