package net.bncloud.order.vo;

import lombok.Data;
import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.entity.OrderProductDetailsHistory;

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
public class OrderProductDetailsHistoryVo extends OrderProductDetailsHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ColorChange colorChange;

	//含税金额变更颜色
	private String  taxPriceStatus;

	//税率变更颜色
	private String  taxRateStatus;

	//采购数量变更颜色
	private String  purchaseNumStatus;

	//交互日期变更颜色
	private String  deliveryTimeStatus;




}
