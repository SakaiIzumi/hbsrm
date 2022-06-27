package net.bncloud.order.vo;

import lombok.Data;
import net.bncloud.order.entity.OrderProductDetails;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
public class OrderProductDetailsVo extends OrderProductDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	


	List<OrderProductDetailsHistoryVo> orderProductDetailsHistoryList;


}
