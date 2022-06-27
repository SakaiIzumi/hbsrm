package net.bncloud.order.wrapper;


import net.bncloud.order.entity.OrderChangeLog;
import net.bncloud.order.vo.OrderChangeLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 修改日志表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-03-12
 */
public class OrderChangeLogWrapper extends BaseEntityWrapper<OrderChangeLog,OrderChangeLogVo>  {

	public static OrderChangeLogWrapper build() {
		return new OrderChangeLogWrapper();
	}

	@Override
	public OrderChangeLogVo entityVO(OrderChangeLog entity) {
		return BeanUtil.copy(entity, OrderChangeLogVo.class);
	}



}
