package net.bncloud.order.vo.event;

import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.order.entity.Order;
import net.bncloud.order.vo.PermissionButtonVo;

import java.util.Date;

/**
 * <p>
 * 订单表 订单表,视图对象
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
@Data
public class OrderEvent extends Order {
	
	/**
	 * 权限按钮
	 */
	private PermissionButtonVo permissionButton;

	private String addTime = DateUtil.formatDateTime(new Date());
    private Long businessId;

}
