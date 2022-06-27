package net.bncloud.saas.supplier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName SupplierRelevanceOperateType
 * @Description: 供应商操作类型枚举
 * @Author liulu
 * @Date 2021/4/25
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum SupplierRelevanceOperateType {
    CONTINUE_TO_COOPERATE("continue_to_cooperate","继续合作"),
    SUSPEND_COOPERATION("suspend_cooperation","暂停合作"),
    FROZEN("frozen","冻结"),
    CANCEL_FROZEN("cancel_frozen","取消冻结"),
;
    private String code;
    private String name;

}
