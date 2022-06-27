package net.bncloud.uaa.security;

import net.bncloud.common.api.IResultCode;

public enum AuthResultCode implements IResultCode {
    LOGIN_ERROR(401, "账号或密码错误"),
    WX_WORK_SSO_LOGIN_ERROR(401, "账号或密码错误"),
    ;


    AuthResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}
