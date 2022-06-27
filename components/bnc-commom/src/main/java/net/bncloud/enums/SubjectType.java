package net.bncloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 确认来源枚举
 * 创建人:    lv
 */
@Getter
@AllArgsConstructor
public enum SubjectType {

	org("org", "组织"),
	sup("supplier", "供应商"),;

	/**
	 * code编码
	 */
	final String code;
	/**
	 * 中文信息描述
	 */
	final String message;
}
