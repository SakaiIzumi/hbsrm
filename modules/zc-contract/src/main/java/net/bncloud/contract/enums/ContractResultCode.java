package net.bncloud.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

/**
 * @ClassName ContractResultCode
 * @Description: 合同返回结果枚举
 * @Author Administrator
 * @Date 2021/4/9
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum ContractResultCode implements IResultCode {

    CONTRACT_CODE_EXISTS(200, "合同编号已存在"),
    ;
    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;


}
