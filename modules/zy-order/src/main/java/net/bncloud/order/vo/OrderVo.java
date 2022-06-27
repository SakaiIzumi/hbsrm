package net.bncloud.order.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.order.entity.Order;

import java.util.List;

/**
 * <p>
 * 订单表 订单表,视图对象
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVo extends Order {

	private static final long serialVersionUID = -5333230126467076867L;
	/**
	 * 权限按钮
	 */
	private PermissionButtonVo permissionButton;

	/**
	 * 产品明细
	 */
	private List<ProductUnderOrderListVo> children;

}
