package net.bncloud.service.api.platform.supplier.enums;

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
public enum SupplierRelevanceStatusEnum {
    INVALID("S01","无效"),
    RELEVANCE("S02","合作中"),
    SUSPEND_COOPERATION("S03","暂停合作"),
    FROZEN("S04","冻结"),

    ;
    private String code;
    private String name;
}
