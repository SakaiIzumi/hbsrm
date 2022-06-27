package net.bncloud.order.wrapper;

import net.bncloud.common.util.BeanUtil;
import net.bncloud.order.entity.Order;
import net.bncloud.order.vo.OrderVo;
import net.bncloud.support.BaseEntityWrapper;

/**
 *
 * 类名称:    OrderWrapper
 * 类描述:    包装类,返回视图层所需的字段
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/10 6:38 下午
 */
public class OrderWrapper extends BaseEntityWrapper<Order, OrderVo> {
	
	public static OrderWrapper build() {
		return new OrderWrapper();
	}
	
	@Override
	public OrderVo entityVO(Order entity) {
		OrderVo orderVo = BeanUtil.copy(entity, OrderVo.class);
		return orderVo;
	}
}
