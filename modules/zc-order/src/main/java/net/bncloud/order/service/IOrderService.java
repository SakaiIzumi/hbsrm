package net.bncloud.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.ExportOrderModel;
import net.bncloud.order.entity.Order;
import net.bncloud.order.param.OrderCommunicateLogParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.SendOrderParam;
import net.bncloud.order.vo.MsgCountVo;
import net.bncloud.order.vo.OrderVo;
import net.bncloud.serivce.api.order.dto.OrderErpDTO;
import net.bncloud.serivce.api.order.feign.OrderSych;

import java.math.BigDecimal;
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
     *
     * @return
     */
    IPage<OrderVo> selectListPage(IPage<Order> page, QueryParam<OrderParam> queryParam);


    /**
     * 查询当前用户待办消息数
     *
     * @return
     */
    MsgCountVo getMsgCount();


    /**
     * 根据采购单号获取订单详情
     *
     * @param purchaseOrderCode
     * @return
     */
    OrderVo getOrderDetails(String purchaseOrderCode);


    /**
     * 发起订单协同
     *
     * @param sendOrderParam
     * @return
     */
    boolean sendCommunicateOrder(List<OrderCommunicateLogParam> sendOrderParam);

    /**
     * 发起变更协同
     *
     * @param sendOrderParam
     * @return
     */
    boolean sendChangeOrder(List<OrderCommunicateLogParam> sendOrderParam);

    /**
     * 确认订单
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean confirmOrder(String purchaseOrderCode);

    /**
     * 确认订单
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean confirmOrder_old(String purchaseOrderCode);

    /**
     * 确认变更订单
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean confirmChangeOrder(String purchaseOrderCode);


    /**
     * 挂起订单
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean stopOrder(String purchaseOrderCode);


    /**
     * 启动订单
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean startOrder(String purchaseOrderCode);

    /**
     * 确定差异
     *
     * @param purchaseOrderCode
     * @return
     */
    boolean confirmDifferenceOrder(String purchaseOrderCode);

    /**
     * 保存答交操作
     *
     * @param sendOrderParam
     * @return
     */
    boolean saveCommunicateOrder(List<OrderCommunicateLogParam> sendOrderParam);

    /**
     * 提醒
     *
     * @return
     */
    boolean sendRemindMsg(String purchaseOrderCode);


    /**
     * 发送订单
     *
     * @param sendOrderParam
     * @return
     */
    boolean sendOrder(SendOrderParam sendOrderParam);

    /**
     * 同步ERP
     *
     * @return
     */
    void syncErp(List<OrderErpDTO> list);

    /**
     * 同步ERP
     *
     * @return
     */
    void syncErpOrder();

    void updateRelationOrder(OrderSych orderSych);



    /**
     * 不分页，根据条件查询所有的订单
     * @param
     * @param queryParam
     */
    List<ExportOrderModel> getOrderListByCondition(QueryParam<OrderParam> queryParam);

    /**
     * 添加订单产品明细的入库数量
     * @param orderProductDetailSourceId
     * @param receiptQuantity
     */
    void addOrderProductDetailInventoryQuantity(String orderProductDetailSourceId, BigDecimal receiptQuantity);

    /**
     * mrp按订单送货创建收料通知单回写的更新方法
     * @return
     */
    void updateErpId(Long orderId, Long fId, String fNumber, String erpStatus);
}
