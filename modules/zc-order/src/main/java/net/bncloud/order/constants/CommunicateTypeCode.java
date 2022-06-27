package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 答交日志枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum CommunicateTypeCode {
	/**
	 * 变更信息
	 */
	NOT_UPDATED(1, "变更信息"),
	/**
	 * 答交信息
	 */
	UPDATED(2,"答交信息"),
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
