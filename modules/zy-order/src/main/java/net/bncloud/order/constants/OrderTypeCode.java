package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 订单状态枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderTypeCode {
	/**
	 * 草稿
	 */
	ORDER_DRAFT("1", "草稿"),
	/**
	 * 待答交
	 */
	ORDER_WAIT("2","待答交"),
	
	/**
	 * 挂起
	 */
	ORDER_STOP("3","挂起"),
	
	/**
	 * 答交差异
	 */
	ORDER_DIFFERENCE("4","答交差异"),
	/**
	 * 退回
	 */
	ORDER_BACK("5","退回"),
	/**
	 * 变更中
	 */
	ORDER_CHANGE("6","变更中"),
	/**
	 * 已确认
	 */
	ORDER_CONFIRM("7","已确认"),
	/**
	 * 已完成
	 */
	ORDER_COMPLETE("8","已完成"),
	;
	
	
	/**
	 * code编码
	 */
	final String code;
	/**
	 * 中文信息描述
	 */
	final String message;
}
