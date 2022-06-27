package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单发送类型枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderSendCode {
	
	/**
	 * 答交发送
	 */
	ORDER_SEND_COMMUNICATE(1,"答交发送"),
	ORDER_SEND_CHANGE(2,"变更"),;
	
	
	/**
	 * 编码
	 */
	final Integer code;
	
	/**
	 * 中文信息描述
	 */
	final String message;
}
