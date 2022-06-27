package net.bncloud.order.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.SumUtils;
import net.bncloud.order.constants.SumErpCode;
import net.bncloud.order.entity.Order;
import net.bncloud.order.entity.OrderErp;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.entity.OrderProductDetailsErp;
import net.bncloud.order.service.IOrderErpService;
import net.bncloud.order.service.IOrderProductDetailsErpService;
import net.bncloud.order.service.IOrderProductDetailsService;
import net.bncloud.order.service.IOrderService;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * 类名称:    SumOrderErpPrice
 * 类描述:    定时计算ERP订单价格
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/26 3:07 下午
 */
@Slf4j
@Component
public class SumOrderErpPrice {
	
	@Autowired
	private IOrderService iOrderErpService;
	
	@Autowired
	private IOrderProductDetailsService iOrderProductDetailsErpService;
	
	
//	@Scheduled(cron = "0 0/2 * * * ?")
//	public void execute() throws Exception {
//		log.info("进入订单价格计算调度---------------------->");
//		//查询出所有未计算的订单
//		Order queryOrderErp = new Order();
//		queryOrderErp.setSumStatus(SumErpCode.ON_RECKON.getCode());
//		List<Order> orderErps = iOrderErpService.list(Condition.getQueryWrapper(queryOrderErp));
//
//		for (int i = 0;i<orderErps.size();i++){
//			Order orderErp = orderErps.get(i);
//			String purchaseOrderCode = orderErp.getPurchaseOrderCode();
//			//查询出下面的产品信息
//			OrderProductDetails queryOrderProductDetailsErp = new OrderProductDetails();
//			OrderProductDetails orderProductDetailsErp = queryOrderProductDetailsErp.setPurchaseOrderCode(purchaseOrderCode);
//			List<OrderProductDetails> orderProductDetailsErps = iOrderProductDetailsErpService.list(Condition.getQueryWrapper(orderProductDetailsErp));
//			//循环查询出下面订单产品的价格
//			BigDecimal orderConfirmPrice = new BigDecimal("0.00");
//			for (OrderProductDetails productDetailsErp:orderProductDetailsErps){
//				BigDecimal purchaseNum = productDetailsErp.getPurchaseNum();
//				BigDecimal unitPrice = productDetailsErp.getUnitPrice();
//				//总价=单价*数量  保留两位小数
//				orderConfirmPrice =orderConfirmPrice.add(SumUtils.multiply(unitPrice, purchaseNum, 2));
//			}
//			//把计算的价格更新到订单，并且修改订单的计算状态
//			iOrderErpService.update(Wrappers.<Order>update().lambda()
//					.set(Order::getOrderPrice, orderConfirmPrice)
//					.set(Order::getSumStatus,SumErpCode.YES_RECKON.getCode())
//					.eq(Order::getPurchaseOrderCode, purchaseOrderCode));
//		}
//
//
//
//	}
//
	
	
	
}
