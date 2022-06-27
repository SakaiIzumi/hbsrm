package net.bncloud.order.wrapper;

import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.vo.OrderProductDetailsVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *包装类,返回视图层所需的字段
 * @author lv
 * @since 2021-03-12
 */
public class OrderProductDetailsWrapper extends BaseEntityWrapper<OrderProductDetails,OrderProductDetailsVo>  {
	
	public static OrderProductDetailsWrapper build() {
		return new OrderProductDetailsWrapper();
	}
	
	@Override
	public OrderProductDetailsVo entityVO(OrderProductDetails entity) {
		OrderProductDetailsVo entityVo = BeanUtil.copy(entity, OrderProductDetailsVo.class);
		return entityVo;
	}
	
	
	
}
