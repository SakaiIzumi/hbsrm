package net.bncloud.serivce.api.order.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import net.bncloud.serivce.api.order.entity.OrderProductDetailsErpDetail;


/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
@Accessors(chain = true)
public class OrderProductDetailsErpDTO extends OrderProductDetailsErpDetail {
	
	private static final long serialVersionUID = 1L;

	/**
	 * md 为什么多了8个小时
	 */
	private String deliveryTimeStr;

	/**
	 * 采购订单来源ID
	 */
	private String sourceId;

	/**
	 * 计划单位
	 */
	private String planUnit;

	/**
	 * 剩余数量
	 */
	private String remainingQuantity;

	/**
	 * 送货数量
	 */
	private String deliveryQuantity;

	/**
	 * 交货地址
	 */
	private String deliveryAddress;

}
