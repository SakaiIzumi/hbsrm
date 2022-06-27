package net.bncloud.order.wrapper;

import net.bncloud.common.util.BeanUtil;
import net.bncloud.order.entity.OrderProductDetailsHistory;
import net.bncloud.order.vo.OrderProductDetailsHistoryVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *包装类,返回视图层所需的字段
 * @author lv
 * @since 2021-03-12
 */
public class OrderProductDetailsHistoryWrapper extends BaseEntityWrapper<OrderProductDetailsHistory, OrderProductDetailsHistoryVo>  {
	
	public static OrderProductDetailsHistoryWrapper build() {
		return new OrderProductDetailsHistoryWrapper();
	}
	
	@Override
	public OrderProductDetailsHistoryVo entityVO(OrderProductDetailsHistory entity) {
		OrderProductDetailsHistoryVo entityVo = BeanUtil.copy(entity, OrderProductDetailsHistoryVo.class);
		return entityVo;
	}
	
	
	
}
