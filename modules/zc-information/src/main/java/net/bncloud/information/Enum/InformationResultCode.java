package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

@Getter
@AllArgsConstructor
public enum InformationResultCode implements IResultCode {


    // TODO 定义模块错误代码
    VALIDATION_MISMATCH(500, "模板与参数验证不匹配"),

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
