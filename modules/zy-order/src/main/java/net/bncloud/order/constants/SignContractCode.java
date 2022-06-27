package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单发送类型枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum SignContractCode {
	
	/**
	 * 答交发送
	 */
	ORDER_SIGN_CONTRACT_WAIT("1","等待签约"),
	ORDER_SIGN_CONTRACT_ABNORMAL("2","异常"),
	ORDER_SIGN_CONTRACT_SIGNED("3","已签约"),
	;
	
	
	/**
	 * 编码
	 */
	final String code;
	
	/**
	 * 中文信息描述
	 */
	final String message;
}
