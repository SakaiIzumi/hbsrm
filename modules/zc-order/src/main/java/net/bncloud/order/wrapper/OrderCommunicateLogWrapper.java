package net.bncloud.order.wrapper;


import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.vo.OrderCommunicateLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 订单答交日志表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-03-12
 */
public class OrderCommunicateLogWrapper extends BaseEntityWrapper<OrderCommunicateLog,OrderCommunicateLogVo>  {

	public static OrderCommunicateLogWrapper build() {
		return new OrderCommunicateLogWrapper();
	}

	@Override
	public OrderCommunicateLogVo entityVO(OrderCommunicateLog entity) {
        OrderCommunicateLogVo entityVo = BeanUtil.copy(entity, OrderCommunicateLogVo.class);
		return entityVo;
	}



}
