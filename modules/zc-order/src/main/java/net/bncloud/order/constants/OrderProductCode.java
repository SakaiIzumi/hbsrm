package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 变更类型枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderProductCode {
	
	DELIVERY_CHANGE("交期变更"),
	NUM_CHANGE("数量变更"),
	PIRCE_CHANGE("价格变更");
	
	
	/**
	 * 中文信息描述
	 */
	final String message;
	
}
