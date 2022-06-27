package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 答交日志枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum CommunicateCode {
	/**
	 * 答交字段-未修改
	 */
	NOT_UPDATED(0, "未修改"),
	/**
	 * 答交字段-修改
	 */
	UPDATED(1,"修改"),
	
	/**
	 * 答交类型-答交差异
	 */
	DIFFERENCE_TYPE(3,"答交差异"),
	
	/**
	 * 答交保存
	 */
	DIFFERENCE_STATUS_SAVE(0,"保存"),
	
	/**
	 * 答交发送
	 */
	DIFFERENCE_STATUS_SEND(1,"答交发送"),
	
	
	
	
	
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
