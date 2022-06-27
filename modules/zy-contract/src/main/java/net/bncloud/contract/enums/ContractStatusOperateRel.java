package net.bncloud.contract.enums;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ContractStatusOperateRel
 * @Description: 合同状态与可执行操作关联关系枚举
 * @Author Administrator
 * @Date 2021/3/22
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
@Slf4j
public enum ContractStatusOperateRel {
    TO_BE_ANSWERED("2","待答交/待确认",new ContractOperateType[]{ContractOperateType.CONFIRM}),
    ;

    private String code;

    private String name;

    private ContractOperateType[] operations;

    /**
     * 获取可执行操作
     * @param operateRel
     * @return
     */
    public static Map<String,Boolean> operations(ContractStatusOperateRel operateRel){
        ContractOperateType[] operations = operateRel.getOperations();
        Map<String,Boolean> permissions = new HashMap<>();
        for (ContractOperateType operation : operations) {
            permissions.put(operation.getCode(),true);
        }
        return permissions;
    }

}
