package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 确认来源枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum SumErpCode {
	
	ON_RECKON(1, "未计算"),
	YES_RECKON(2, "已计算"),;
	
	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String message;
}
