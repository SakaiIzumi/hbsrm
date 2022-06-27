package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum OrderMsgTypeCode {
	
	/**
	 * 阅读状态
	 */
	UNREAD("0","未读"),
	READ("1","已读"),;
	
	
	/**
	 * 编码
	 */
	final String code;
	
	/**
	 * 中文信息描述
	 */
	final String message;
}
