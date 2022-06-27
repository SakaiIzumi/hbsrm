package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 订单状态枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderChangeTypeCode {
	/**
	 * 无变更
	 */
	ORDER_NO_CHANGE("1", "无变更"),
	/**
	 * 已确认并更
	 */
	ORDER_YES_CHANGE("2","已确认变更"),
	
	/**
	 * 未确认变更
	 */
	ORDER_UNCONFIRMED_CHANGE("3","未确认变更"),
	/**
	 * 待确认变更
	 */
	ORDER_WAIT_CHANGE("4","待确认变更"),
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
