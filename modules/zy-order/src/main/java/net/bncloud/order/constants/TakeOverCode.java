package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收货状态
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum TakeOverCode {
	
	TAKE_OVER_WAIT("1", "待收货"),
	TAKE_OVER_PART("2", "部分收货"),
	TAKE_OVER_FINISH("3", "收货完成"),
	TAKE_OVER_END("4", "已结案"),;
	
	/**
	 * code编码
	 */
	final String code;
	/**
	 * 中文信息描述
	 */
	final String message;
}
