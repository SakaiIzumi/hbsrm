package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 确认来源枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum ConfirmSourceCode {
	
	ZC(1, "采购工作台"),
	ZY(2, "销售工作台"),;
	
	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String message;
}
