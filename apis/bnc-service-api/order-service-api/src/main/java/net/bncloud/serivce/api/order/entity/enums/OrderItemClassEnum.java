package net.bncloud.serivce.api.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName
 * @Description: 物料分类
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum OrderItemClassEnum {
    FINISH_CLASS("001","成品类"),
    UNFINISH_CLASS("002","非成品类"),
    ;
    private String code;
    private String name;
}
