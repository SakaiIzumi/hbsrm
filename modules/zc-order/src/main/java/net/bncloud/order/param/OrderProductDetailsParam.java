package net.bncloud.order.param;


import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
public class OrderProductDetailsParam extends OrderProductDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 仅显示变更项
	 */
	private boolean existChange;

	/**
	 * 仅显示差异项
	 */
	private boolean existDifference;

	/**
	 * 按交货计划显示
	 */
	private boolean deliveryPlan;
	
	
}
