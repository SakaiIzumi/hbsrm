package net.bncloud.order.param;

import lombok.Data;
import net.bncloud.order.entity.Order;

import java.io.Serializable;

/**
 *
 * 类名称:    OrderParam
 * 类描述:    订单请求参数
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/10 11:16 上午
 */
@Data
public class OrderParam extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 采购日期开始
	 */
	private String purchaseTimeStart;
	/**
	 * 采购日期结束
	 */
	private String purchaseTimeEnd;
	/**
	 * 确认日期开始
	 */
	private String confirmTimeStart;
	/**
	 * 确认日期结束
	 */
	private String confirmTimeEnd;
	
	/**
	 * 订单金额起
	 */
	private String orderPriceStart;
	
	/**
	 * 订单金额止
	 */
	private String orderPriceEnd;
	
	/**
	 * 答交金额起
	 */
	private String orderConfirmPriceStart;
	
	/**
	 * 答交金额止
	 */
	private String orderConfirmPriceEnd;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	
	
}
