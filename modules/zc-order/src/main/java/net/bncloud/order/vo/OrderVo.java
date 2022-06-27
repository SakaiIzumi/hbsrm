package net.bncloud.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.common.util.DateUtil;
import net.bncloud.order.entity.Order;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 订单表,视图对象
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderVo extends Order {
	
	/**
	 * 权限按钮
	 */
	private PermissionButtonVo permissionButton;

	/**
	 * 产品明细
	 */
	private List<ProductUnderOrderListVo> children;



}
