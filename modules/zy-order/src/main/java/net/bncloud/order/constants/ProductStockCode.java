package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 类名称:    ProductStockCode
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/6 5:43 下午
 */
@Getter
@AllArgsConstructor
public enum ProductStockCode {
	
	OUT_OF_STOCK("473","库存不足！"),;
	
	
	/**
	 * 编码
	 */
	final String code;
	
	/**
	 * 中文信息描述
	 */
	final String message;
}
