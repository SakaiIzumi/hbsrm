package net.bncloud.order.wrapper;


import net.bncloud.order.entity.OrderOperationLog;
import net.bncloud.order.vo.OrderOperationLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 订单操作记录表
 * </p>
 *包装类,返回视图层所需的字段
 * @author lv
 * @since 2021-03-12
 */
public class OrderOperationLogWrapper extends BaseEntityWrapper<OrderOperationLog,OrderOperationLogVo>  {

	public static OrderOperationLogWrapper build() {
		return new OrderOperationLogWrapper();
	}

	@Override
	public OrderOperationLogVo entityVO(OrderOperationLog entity) {
        OrderOperationLogVo entityVo = BeanUtil.copy(entity, OrderOperationLogVo.class);
		return entityVo;
	}



}
