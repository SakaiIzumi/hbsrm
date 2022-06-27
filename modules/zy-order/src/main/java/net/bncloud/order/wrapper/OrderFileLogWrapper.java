package net.bncloud.order.wrapper;


import net.bncloud.order.entity.OrderFileLog;
import net.bncloud.order.vo.OrderFileLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 订单文件下载日志表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-03-12
 */
public class OrderFileLogWrapper extends BaseEntityWrapper<OrderFileLog,OrderFileLogVo>  {

	public static OrderFileLogWrapper build() {
		return new OrderFileLogWrapper();
	}

	@Override
	public OrderFileLogVo entityVO(OrderFileLog entity) {
        OrderFileLogVo entityVo = BeanUtil.copy(entity, OrderFileLogVo.class);
		return entityVo;
	}



}
