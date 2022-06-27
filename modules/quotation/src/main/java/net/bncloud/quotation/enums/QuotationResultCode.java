package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

/**
 * @ClassName QuotationResultCode
 * @Description: 询价返回结果枚举
 * @Author Administrator
 * @Date 2021/4/9
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum QuotationResultCode implements IResultCode {

    //400(两位业务模块，3位自定义编码)
    PARAM_ERROR(40005001, "参数错误"),
    EXPRESSION_ERROR(40005002, "表达式错误(点击结果测试按钮通过结果计算后再次点击保存)"),
    SOURCE_NOT_FOUND(40005003, "未查询到相关信息"),
    FORBIDDEN_CHANGE_DATE(40005004, "开标成功,不可修改报价截止时间"),
    SUPPLIER_NOT_FOUND(40005005, "未查询到此账号的供应商信息"),
    USER_NOT_FOUND(40005006, "账号信息有误"),
    REQUEST_REPEAT(40005007, "请求处理中，请勿重复提交"),

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
