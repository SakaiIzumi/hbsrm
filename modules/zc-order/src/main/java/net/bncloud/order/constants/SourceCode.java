package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum SourceCode {
	
	/**
	 * 发送订单
	 */
	ERP("ERP信息"),
	
	/**
	 * 确认订单
	 */
	ZY("销售工作台"),

	/**
	 * 确认订单
	 */
	ZC("采购工作台");
	/**
	 * 中文信息描述
	 */
	final String message;
}
