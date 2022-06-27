package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liyh
 */

@Getter
@AllArgsConstructor
@Slf4j
public enum PurchaseOrderReceivingStatusOperateRel {

    TO_BE_CONFIRM("1","待确认",new PurchaseOrderReceivingOperateType[]{PurchaseOrderReceivingOperateType.CONFIRM,
            PurchaseOrderReceivingOperateType.REJECT}),

    CONFIRM("2","已确认",new PurchaseOrderReceivingOperateType[]{}),

    RETURNED("3","已退回",new PurchaseOrderReceivingOperateType[]{}),
    ;



    private String code;

    private String name;

    private PurchaseOrderReceivingOperateType[] operations;

    /**
     * 获取可执行操作
     * @param operateRel
     * @return
     */
    public static Map<String,Boolean> operations(Boolean comparisonDisplay,PurchaseOrderReceivingStatusOperateRel operateRel){
        PurchaseOrderReceivingOperateType[] operations = operateRel.getOperations();
        if(comparisonDisplay){
            PurchaseOrderReceivingOperateType[] operations_copy = new PurchaseOrderReceivingOperateType[operations.length+1];
            System.arraycopy(operations,0,operations_copy,0,operations.length);
            operations_copy[operations.length]=PurchaseOrderReceivingOperateType.REMARK;
            operations=operations_copy;
        }
        Map<String,Boolean> permissions = new HashMap<>();
        for (PurchaseOrderReceivingOperateType operation : operations) {
            permissions.put(operation.getCode(),true);
        }
        return permissions;
    }
}
