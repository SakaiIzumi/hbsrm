package net.bncloud.contract.enums;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
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
    DRAFT("1","草稿", new ContractOperateType[]{ContractOperateType.SEND}),
    TO_BE_ANSWERED("2","待答交",new ContractOperateType[]{ContractOperateType.WITHDRAW}),
    REJECTED("3","被拒",new ContractOperateType[]{ContractOperateType.EDIT,ContractOperateType.SEND}),
    TO_BE_SIGNED_ONLINE("4","待网签",new ContractOperateType[]{}),
    ABNORMAL("5","异常",new ContractOperateType[]{ContractOperateType.RE_SIGN}),
    VALID("6","有效",new ContractOperateType[]{}),
    INVALID("7","无效",new ContractOperateType[]{ContractOperateType.RE_INITIATE}),
    EXPIRED("8","过期",new ContractOperateType[]{ContractOperateType.RE_INITIATE}),
    WITHDRAWN("9","已撤回",new ContractOperateType[]{ContractOperateType.SEND}),
    UPDATING("10","更新中",new ContractOperateType[]{ContractOperateType.SEND})
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
