package net.bncloud.order.service;

import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.ExportOrderModel;
import net.bncloud.order.entity.Order;
import net.bncloud.order.param.OrderCommunicateLogParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.vo.MsgCountVo;
import net.bncloud.order.vo.OrderVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
public interface IOrderService extends BaseService<Order> {
	
	/**
	 * 通过xml自定义sql查询
	 * @return
	 */
	Page<OrderVo> selectListPage(QueryParam<OrderParam> queryParam, Pageable pageable);
	
	
	/**
	 * 根据采购单号获取订单详情
	 * @param purchaseOrderCode
	 * @return
	 */
	OrderVo getOrderDetails(String purchaseOrderCode);
	
	
	/**
	 * 发起订单协同
	 * @param sendOrderParam
	 * @return
	 */
	boolean sendOrder(List<OrderCommunicateLogParam>  sendOrderParam);
	
	
	/**
	 * 发起变更协同
	 * @param sendOrderParam
	 * @return
	 */
	boolean sendOrderChange(List<OrderCommunicateLogParam>  sendOrderParam);
	
	
	/**
	 * 确认订单
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean confirmOrder(String purchaseOrderCode);

	boolean confirmOrder_old(String purchaseOrderCode);
	
	/**
	 * 保存答交操作
	 * @param sendOrderParam
	 * @return
	 */
	boolean saveCommunicateOrder(List<OrderCommunicateLogParam> sendOrderParam);
	
	/**
	 * 确认变更订单
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean confirmChangeOrder(String purchaseOrderCode);
	
	
	/**
	 * 挂起订单
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean stopOrder(String purchaseOrderCode);
	
	
	/**
	 * 启动订单
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean startOrder(String purchaseOrderCode);
	
	/**
	 * 确定差异
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean confirmDifferenceOrder(String purchaseOrderCode);
	
	/**
	 * 退回订单
	 * @param purchaseOrderCode
	 * @return
	 */
	boolean goBackOrder(String purchaseOrderCode);
	
	
	/**
	 * 查询当前用户待办消息数
	 * @return
	 */
	MsgCountVo getMsgCount();


	Boolean sendRemindMsg(String purchaseOrderCode);

	/**
	 * 不分页，根据条件查询所有的订单
	 * @param
	 * @param queryParam
	 */
	List<ExportOrderModel> getOrderListByCondition(QueryParam<OrderParam> queryParam);
}
