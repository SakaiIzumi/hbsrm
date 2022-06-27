package net.bncloud.event.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

@Getter
@AllArgsConstructor
public enum EventResultCode implements IResultCode {
    EVENT_TYPE_NOT_FOUND(4000, "事件类型未定义"),
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
