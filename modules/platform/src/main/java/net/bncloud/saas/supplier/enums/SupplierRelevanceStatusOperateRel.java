package net.bncloud.saas.supplier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SupplierRelevanceStatusOperateRel
 * @Description: 供应商状态与可执行操作关联关系枚举
 * @Author liulu
 * @Date 2021/4/25
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
@Slf4j
public enum SupplierRelevanceStatusOperateRel {
    INVALID("S01","无效",new SupplierRelevanceOperateType[]{}),
    RELEVANCE("S02","合作中",new SupplierRelevanceOperateType[]{SupplierRelevanceOperateType.SUSPEND_COOPERATION,SupplierRelevanceOperateType.FROZEN}),
    SUSPEND_COOPERATION("S03","暂停合作",new SupplierRelevanceOperateType[]{SupplierRelevanceOperateType.CONTINUE_TO_COOPERATE,SupplierRelevanceOperateType.FROZEN}),
    FROZEN("S04","冻结",new SupplierRelevanceOperateType[]{SupplierRelevanceOperateType.SUSPEND_COOPERATION,SupplierRelevanceOperateType.CANCEL_FROZEN})

    ;
    private String code;

    private String name;

    private SupplierRelevanceOperateType[] operations;

    /**
     * 根据供应商状态获取可执行操作
     * @param deliveryStatusCode
     * @return
     */
    public static Map<String,Boolean> operations(String  deliveryStatusCode){
        Map<String,Boolean> permissions = new HashMap<>();
        for(SupplierRelevanceOperateType deliveryOperateType: SupplierRelevanceOperateType.values())
            permissions.put(deliveryOperateType.getCode(),false);
        for(SupplierRelevanceStatusOperateRel supplierRelevanceStatusOperateRel : SupplierRelevanceStatusOperateRel.values()){
            if(supplierRelevanceStatusOperateRel.getCode().equals(deliveryStatusCode)){
                SupplierRelevanceOperateType[] operations = supplierRelevanceStatusOperateRel.getOperations();
                for (SupplierRelevanceOperateType operation : operations) {
                    permissions.put(operation.getCode(),true);
                }
            }
        }
        return permissions;
    }
}
