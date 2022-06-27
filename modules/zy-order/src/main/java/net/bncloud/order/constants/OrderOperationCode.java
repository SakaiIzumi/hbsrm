package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderOperationCode {
	
	/**
	 * 发送订单
	 */
	ORDER_OPERATION_SEND("发送"),
	
	/**
	 * 确认订单
	 */
	ORDER_OPERATION_YES("确认订单"),
	
	/**
	 * 确认差异
	 */
	ORDER_OPERATION_YES_DIFFERENCE("确认差异"),
	
	
	/**
	 * 确认变更
	 */
	ORDER_OPERATION_YES_CHANGE("确认变更"),
	
	
	/**
	 * 挂起
	 */
	ORDER_OPERATION_STOP("挂起"),
	
	/**
	 * 启动
	 */
	ORDER_OPERATION_START("取消挂起"),
	
	/**
	 * 差异回复
	 */
	ORDER_OPERATION_UNCONFIRMED_CHANGE("差异回复"),
	/**
	 * 退回
	 */
	ORDER_OPERATION_OG_BACK("退回"),
	/**
	 * 提醒
	 */
	ORDER_OPERATION_MSG("提醒"),
	;

	
	/**
	 * 中文信息描述
	 */
	final String message;
}
