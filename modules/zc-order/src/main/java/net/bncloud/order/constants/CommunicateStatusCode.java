package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 答交日志枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum CommunicateStatusCode {
	/**
	 * 未生效
	 */
	NOT_UPDATED(0, "未生效"),
	/**
	 * 已生效
	 */
	UPDATED(1,"已生效"),
	
	/**
	 * 作废
	 */
	DIFFERENCE_TYPE(2,"作废"),
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
