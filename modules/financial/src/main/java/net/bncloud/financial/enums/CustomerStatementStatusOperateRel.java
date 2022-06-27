package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName StatementStatusOperateRel
 * @Description: 对账单状态与可执行操作关联关系枚举(采购方创建)
 * @Author Administrator
 * @Date 2021/3/22
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
@Slf4j
public enum CustomerStatementStatusOperateRel {
    //
    DRAFT("1", "草稿", new StatementOperateType[]{StatementOperateType.EDIT,StatementOperateType.DELETE, StatementOperateType.SEND}, new StatementOperateType[]{}),
    TO_BE_CUSTOMER_CONFIRM("2", "待采购方确认", new StatementOperateType[]{StatementOperateType.REJECT,StatementOperateType.CUSTOMER_CONFIRM}
            , new StatementOperateType[]{StatementOperateType.WITHDRAW,StatementOperateType.REMIND}),
    WITHDRAWN("3", "已撤回", new StatementOperateType[]{
            StatementOperateType.EDIT,
            StatementOperateType.INVALID,
            StatementOperateType.SEND
    }, new StatementOperateType[]{}),
    INVALID("4", "作废", new StatementOperateType[]{}, new StatementOperateType[]{}),
    RETURNED("5", "已退回", new StatementOperateType[]{
            StatementOperateType.EDIT,
            StatementOperateType.INVALID,
            StatementOperateType.SEND
    }, new StatementOperateType[]{}),
    CONFIRMED("6", "已确认", new StatementOperateType[]{}, new StatementOperateType[]{StatementOperateType.MAKE_INVOICE}),
    TO_BE_SUPPLIER_CONFIRM("7", "待供应商确认", new StatementOperateType[]{StatementOperateType.WITHDRAW, StatementOperateType.REMIND}
            , new StatementOperateType[]{StatementOperateType.REJECT, StatementOperateType.SUPPLIER_CONFIRM}),
    ;

    private String code;

    private String name;

    private StatementOperateType[] customerOperations;

    private StatementOperateType[] supplierOperations;

    /**
     * 获取采购方可执行操作
     *
     * @param operateRel
     * @return
     */
    public static Map<String, Boolean> getCustomerOperations(CustomerStatementStatusOperateRel operateRel) {
        Map<String, Boolean> permissions = new HashMap<>();
        StatementOperateType[] operations = operateRel.getCustomerOperations();
        for (StatementOperateType operation : operations) {
            permissions.put(operation.getCode(), true);
        }

        return permissions;
    }

    /**
     * 获取供应商可执行操作
     *
     * @param operateRel
     * @return
     */
    public static Map<String, Boolean> getSupplierOperations(CustomerStatementStatusOperateRel operateRel) {
        Map<String, Boolean> permissions = new HashMap<>();
        StatementOperateType[] operations = operateRel.getSupplierOperations();
        for (StatementOperateType operation : operations) {
            permissions.put(operation.getCode(), true);
        }

        return permissions;
    }

}
