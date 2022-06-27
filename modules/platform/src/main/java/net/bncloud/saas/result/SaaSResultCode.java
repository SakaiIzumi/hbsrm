package net.bncloud.saas.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

@Getter
@AllArgsConstructor
public enum SaaSResultCode implements IResultCode {


    // TODO 定义模块错误代码
    USER_MOBILE_REGISTERED(2000, "手机号已注册"),
    COMPANY_CREDIT_CODE_EXIST(2000, "统一社会信用代码已注册"),
    DICT_EXIST_ERROR(20001, "字典已存在"), // TODO
    EMPLOYEE_NOT_EXIST_ERROR(20002, "员工不存在"),

    MANAGER_TRANSFER_FORBIDDEN(20401, ""),
    MANAGER_DELETE_FORBIDDEN(20401, "只有主管理员才有权限删除管理员"),
    COMPANY_LOAD_FAILED(20401,"获取采购方信息失败"),
    USER_LOAD_FAILED(20402,"获取用户信息失败"),
    DEPARTMENT_DELETE_FORBIDDEN(20403,"子部门不为空，不能删除"),
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
