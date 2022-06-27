package net.bncloud.order.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 类名称:    StockParam
 * 类描述:    库存操作
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/9 11:55 上午
 */
@Data
public class StockParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 交期ID
	 */
	private Long communicateId;
	
	/**
	 * 操作库存数
	 */
	private BigDecimal sendNum;
	
	
}
