package net.bncloud.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.*;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.order.constants.*;
import net.bncloud.order.entity.*;
import net.bncloud.order.event.*;
import net.bncloud.order.feign.SupplierElectricSignatureConfigResourceFeignClient;
import net.bncloud.order.mapper.OrderMapper;
import net.bncloud.order.mapper.OrderProductDetailsMapper;
import net.bncloud.order.param.OrderCommunicateLogParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.SendOrderParam;
import net.bncloud.order.service.*;
import net.bncloud.order.vo.*;
import net.bncloud.order.vo.event.OrderEvent;
import net.bncloud.order.wrapper.OrderWrapper;
import net.bncloud.serivce.api.order.dto.OrderErpDTO;
import net.bncloud.serivce.api.order.dto.OrderProductDetailsErpDTO;
import net.bncloud.serivce.api.order.entity.enums.OrderChangeReasonEnum;
import net.bncloud.serivce.api.order.feign.OrderSych;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import net.bncloud.utils.CompareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private IOrderProductDetailsService iOrderProductDetailsService;

    @Autowired
    private IOrderCommunicateLogService iOrderCommunicateLogService;

    @Autowired
    private IOrderOperationLogService iOrderOperationLogService;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private PurchaseOrderFeignClient purchaseOrderFeignClient;


    @Autowired
    private DefaultEventPublisher defaultEventPublisher;

    @Autowired
    private IOrderProductDetailHistoryService iOrderProductDetailHistoryService;
    @Autowired
    private IOrderErpService iSyncErpService;


    @Autowired
    private IOrderChangeLogService iOrderChangeLogService;

    @Autowired
    private IOrderCommunicateLogSaveService iOrderCommunicateLogSaveService;

    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    @Resource
    private SupplierElectricSignatureConfigResourceFeignClient supplierElectricSignatureConfigResourceFeignClient;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IOrderProductDetailsService orderProductDetailsService;
    @Resource
    private OrderProductDetailsMapper orderProductDetailsMapper;
    @Resource
    private NumberFactory numberFactory;

    public void getCurrentSupplierStatus(String code) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(code);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        if (oneSupplierByCode.isSuccess()) {
            SupplierDTO supplier = oneSupplierByCode.getData();
            if (supplier == null) {
                log.error("供应商" + supplier.getCode() + "不存在");
                throw new ApiException(500, "供应商" + supplier.getCode() + "不存在");
            }

            if (supplier.getRelevanceStatus().equals(SupplierRelevanceStatusEnum.FROZEN.getCode())) {
                log.error("供应商" + supplier.getCode() + "状态" + SupplierRelevanceStatusEnum.FROZEN.getName());
                throw new ApiException(500, "供应商编号" + supplier.getCode() + "状态" + SupplierRelevanceStatusEnum.FROZEN.getName());
            }

        }
    }

    @Override
    public IPage<OrderVo> selectListPage(IPage<Order> page, QueryParam<OrderParam> queryParam) {
        IPage<Order> orderVoIPage = page.setRecords(baseMapper.selectListPage(page, queryParam));
        IPage<OrderVo> orderVoPageResult = OrderWrapper.build().pageVO(orderVoIPage);

        orderVoPageResult.getRecords().forEach(orderVo -> {
            //设置明细信息
            List<OrderProductDetails> productDetailsList = orderProductDetailsService.list(Wrappers.<OrderProductDetails>lambdaQuery().eq(OrderProductDetails::getPurchaseOrderCode, orderVo.getPurchaseOrderCode()));
            List<ProductUnderOrderListVo> productLists = BeanUtil.copyProperties(productDetailsList, ProductUnderOrderListVo.class);
            orderVo.setChildren(productLists);
            //设置按钮权限
            PermissionButtonVo permissionButton = getPermissonButton(orderVo);
            orderVo.setPermissionButton(permissionButton);
        });

        return orderVoPageResult;

    }

    @Override
    public MsgCountVo getMsgCount() {
        MsgCountVo msgCountVo = new MsgCountVo();
        //差异等处理数
        Order queryDifferenceMsgCount = new Order();
        queryDifferenceMsgCount.setOrderStatus(OrderTypeCode.ORDER_DIFFERENCE.getCode());
        int differenceMsgCount = count(Condition.getQueryWrapper(queryDifferenceMsgCount));
        msgCountVo.setDifferenceMsgCount(differenceMsgCount);
        //待答交
        Order queryWaitingForAnswersCount = new Order();
        queryWaitingForAnswersCount.setOrderStatus(OrderTypeCode.ORDER_WAIT.getCode());
        int waitingForAnswersCount = count(Condition.getQueryWrapper(queryWaitingForAnswersCount));
        msgCountVo.setWaitingForAnswersCount(waitingForAnswersCount);
        //未确认变更
        Order queryUnconfirmedChangeCount = new Order();
        queryUnconfirmedChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode());
        int unconfirmedChangeCount = count(Condition.getQueryWrapper(queryUnconfirmedChangeCount));
        msgCountVo.setUnconfirmedChangeCount(unconfirmedChangeCount);
        //待确认变更
        Order queryWaitForUnconfirmedChangeCount = new Order();
        queryWaitForUnconfirmedChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode());
        int waitForUnconfirmedChangeCount = count(Condition.getQueryWrapper(queryWaitForUnconfirmedChangeCount).lambda().ne(Order::getOrderStatus, OrderTypeCode.ORDER_STOP.getCode()));
        msgCountVo.setWaitForUnconfirmedChangeCount(waitForUnconfirmedChangeCount);
        //退回
        Order queryReturnCount = new Order();
        queryReturnCount.setOrderStatus(OrderTypeCode.ORDER_BACK.getCode());
        int returnCount = count(Condition.getQueryWrapper(queryReturnCount));
        msgCountVo.setReturnCount(returnCount);
        //未完成订单
        LambdaQueryWrapper<Order> ne = Wrappers.<Order>query().lambda().ne(Order::getOrderStatus, OrderTypeCode.ORDER_COMPLETE.getCode());
        int notFinishedCount = count(ne);
        msgCountVo.setNotFinishedCount(notFinishedCount);
        //暂停执行
        Order queryStopCount = new Order();
        queryStopCount.setOrderStatus(OrderTypeCode.ORDER_STOP.getCode());
        int stopCount = count(Condition.getQueryWrapper(queryStopCount));
        msgCountVo.setStopCount(stopCount);
        //确认变更
        Order queryConfirmChangeCount = new Order();
        queryConfirmChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_YES_CHANGE.getCode());
        int confirmChangeCount = count(Condition.getQueryWrapper(queryConfirmChangeCount));
        msgCountVo.setConfirmChangeCount(confirmChangeCount);
        //草稿
        Order queryDraftCount = new Order();
        queryDraftCount.setOrderStatus(OrderTypeCode.ORDER_DRAFT.getCode());
        int draftCount = count(Condition.getQueryWrapper(queryDraftCount));
        msgCountVo.setDraftCount(draftCount);
        //确认
        Order queryConfirmCount = new Order();
        queryConfirmCount.setOrderStatus(OrderTypeCode.ORDER_CONFIRM.getCode());
        int confirmCount = count(Condition.getQueryWrapper(queryConfirmCount));
        msgCountVo.setConfirmCount(confirmCount);
        //完成
        Order queryCompleteCount = new Order();
        queryCompleteCount.setOrderStatus(OrderTypeCode.ORDER_COMPLETE.getCode());
        int completeCount = count(Condition.getQueryWrapper(queryCompleteCount));
        msgCountVo.setCompleteCount(completeCount);
        //变更
        Order queryChangeCount = new Order();
        queryChangeCount.setOrderStatus(OrderTypeCode.ORDER_CHANGE.getCode());
        int changeCount = count(Condition.getQueryWrapper(queryChangeCount));
        msgCountVo.setChangeCount(changeCount);

        return msgCountVo;
    }

    @Override
    public OrderVo getOrderDetails(String purchaseOrderCode) {
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));
        //转换成视图对象
        OrderVo orderVo = OrderWrapper.build().entityVO(order);
        //进行权限判断
        PermissionButtonVo permissionButtonVo = getPermissonButton(orderVo);

        orderVo.setPermissionButton(permissionButtonVo);
        if (orderVo.getConfirmSource() != null) {
            if (ConfirmSourceCode.ZC.getCode() == orderVo.getConfirmSource() && !OrderTypeCode.ORDER_DRAFT.getCode().equals(orderVo.getOrderStatus())) {
                //设置订单已读状态
                update(Wrappers.<Order>update().lambda()
                        .set(Order::getMsgType, OrderMsgTypeCode.READ.getCode())
                        .set(Order::getMsgReadTime, LocalDateTime.now())
                        .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
            }
        }
        return orderVo;
    }

    /**
     * 判断按钮权限
     *
     * @param orderVo
     * @return
     */
    private PermissionButtonVo getPermissonButton(OrderVo orderVo) {
        //设置按钮权限
        PermissionButtonVo permissionButtonVo = new PermissionButtonVo();
        //只有确认来源为各自终端才能进行按钮操作
        if (orderVo.getConfirmSource() != null && ConfirmSourceCode.ZC.getCode() == orderVo.getConfirmSource()) {
            //发送：订单在草稿状态下可进行发布操作
            if (OrderTypeCode.ORDER_DRAFT.getCode().equals(orderVo.getOrderStatus()) && OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(orderVo.getChangeStatus())) {
                permissionButtonVo.setSend(true);
            }
            if (OrderTypeCode.ORDER_WAIT.getCode().equals(orderVo.getOrderStatus()) && OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(orderVo.getChangeStatus())) {
                permissionButtonVo.setConfirm(true);
            }
            if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderVo.getOrderStatus()) && OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderVo.getChangeStatus())) {
                permissionButtonVo.setSend(true);
            }
        }
        return permissionButtonVo;
    }


    @Override
    @Transactional
    public boolean sendCommunicateOrder(List<OrderCommunicateLogParam> sendOrderParam) {

        try {


            //查询当前订单状态
            Order orderQuery = new Order();
            orderQuery.setPurchaseOrderCode(sendOrderParam.get(0).getPurchaseOrderCode());
            Order order = getOne(Condition.getQueryWrapper(orderQuery));

            getCurrentSupplierStatus(order.getSupplierCode());
            String purchaseOrderCode = order.getPurchaseOrderCode();
            //用于计算项次的集合
            List<CommunicateNumVo> communicateNumVos = new ArrayList<>();

            //作废之前的答交信息
            iOrderCommunicateLogService.update((Wrappers.<OrderCommunicateLog>update().lambda()
                    .set(OrderCommunicateLog::getStatus, CommunicateStatusCode.DIFFERENCE_TYPE.getCode())
                    .eq(OrderCommunicateLog::getPurchaseOrderCode, purchaseOrderCode))
            );

            for (int j = 0; j < sendOrderParam.size(); j++) {
                CommunicateNumVo communicateNumVo = new CommunicateNumVo();
                //查询产品信息
                OrderProductDetails orderProductDetails = iOrderProductDetailsService.getById(sendOrderParam.get(j).getOrderProductDetailsId());
                //是否需要取数据库拿数据的标记
                boolean sign = true;
                if (communicateNumVos.size() > 0) {
                    for (CommunicateNumVo communicateNumVo1 : communicateNumVos) {
                        //如果查到该产品已经计算过一次，那么就类推下去
                        if (orderProductDetails.getId().equals(communicateNumVo1.getProductDetailsId())) {
                            communicateNumVo = communicateNumVo1;
                            sign = false;
                        }
                    }
                }
                if (sign) {
                    //查询当前答交了多少次
                    int count = iOrderCommunicateLogService.getMaxBatch(orderProductDetails.getId(), orderProductDetails.getPurchaseOrderCode());
                    communicateNumVo.setCount(count);
                    communicateNumVo.setProductDetailsId(orderProductDetails.getId());
                    communicateNumVo.setThisCount(0);

                }


                OrderCommunicateLogParam orderCommunicateLogParam = sendOrderParam.get(j);
                orderCommunicateLogParam.setType(OrderSendCode.ORDER_SEND_COMMUNICATE.getCode());
                OrderCommunicateLog orderCommunicateLog = BeanUtil.copy(orderCommunicateLogParam, OrderCommunicateLog.class);


                //删除保存的日志
                OrderCommunicateLogSave orderCommunicateLogSave = new OrderCommunicateLogSave();
                orderCommunicateLogSave.setOrderProductDetailsId(orderProductDetails.getId());
                orderCommunicateLogSave.setPurchaseOrderCode(purchaseOrderCode);
                orderCommunicateLogSave.setCreatedBy(AuthUtil.getUser().getUserId());
                iOrderCommunicateLogSaveService.remove(Condition.getQueryWrapper(orderCommunicateLogSave));

                //进行数据设置
                if (orderCommunicateLog.getDeliveryTime() != null) {
                    orderCommunicateLog.setDeliveryTimeType(CommunicateCode.UPDATED.getCode());
                } else {
                    orderCommunicateLog.setDeliveryTimeType(CommunicateCode.NOT_UPDATED.getCode());
                    orderCommunicateLog.setDeliveryTime(orderProductDetails.getDeliveryTime());
                }
                if (orderCommunicateLog.getPurchaseNum() != null) {
                    orderCommunicateLog.setPurchaseNumType(CommunicateCode.UPDATED.getCode());
                } else {
                    orderCommunicateLog.setPurchaseNumType(CommunicateCode.NOT_UPDATED.getCode());
                    orderCommunicateLog.setPurchaseNum(orderProductDetails.getPurchaseNum());
                }
                if (orderCommunicateLog.getUnitPrice() != null) {
                    orderCommunicateLog.setUnitPriceType(CommunicateCode.UPDATED.getCode());
                } else {
                    orderCommunicateLog.setUnitPriceType(CommunicateCode.NOT_UPDATED.getCode());
                    orderCommunicateLog.setUnitPrice(orderProductDetails.getUnitPrice());
                }
                //orderCommunicateLog.setStatus(CommunicateCode.DIFFERENCE_STATUS_SEND.getCode());
                //设置产品信息
                orderCommunicateLog.setOrderProductDetailsId(orderProductDetails.getId());
                orderCommunicateLog.setItemCode(orderProductDetails.getItemCode());
                orderCommunicateLog.setPurchaseOrderCode(orderProductDetails.getPurchaseOrderCode());

                if (order.getChangeStatus().equals(OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode()) || order.getChangeStatus().equals(OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode())) {
                    orderCommunicateLog.setType(CommunicateTypeCode.NOT_UPDATED.getCode());
                } else {
                    orderCommunicateLog.setType(CommunicateTypeCode.UPDATED.getCode());
                }
                orderCommunicateLog.setStatus(CommunicateStatusCode.NOT_UPDATED.getCode());
                //设置产品信息
                orderCommunicateLog.setOrderProductDetailsId(orderProductDetails.getId());
                orderCommunicateLog.setItemCode(orderProductDetails.getItemCode());
                orderCommunicateLog.setPurchaseOrderCode(purchaseOrderCode);
                orderCommunicateLog.setItem(orderProductDetails.getItemCode() + "-" + StringUtil.numberToLetter(communicateNumVo.getCount() + 1) + (communicateNumVo.getThisCount() + 1));
                //保存批次
                orderCommunicateLog.setBatch(communicateNumVo.getCount() + 1);
                //orderCommunicateLog.setType(CommunicateCode.DIFFERENCE_TYPE.getCode());
                iOrderCommunicateLogService.saveOrUpdate(orderCommunicateLog);
                communicateNumVo.setThisCount(communicateNumVo.getThisCount() + 1);
                communicateNumVos.add(communicateNumVo);
            }


            //修改订单状态 和确认信息
            Order updateById = new Order();
            updateById.setId(order.getId());
            updateById.setConfirmSource(ConfirmSourceCode.ZY.getCode());
            updateById.setOrderStatus(OrderTypeCode.ORDER_WAIT.getCode());
            updateById.setMsgType(OrderMsgTypeCode.UNREAD.getCode());
            updateById(updateById);
            //保存操作日志
            OrderOperationLog saveOperationLog = new OrderOperationLog();
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_UNCONFIRMED_CHANGE.getMessage());
            saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
            saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
            iOrderOperationLogService.save(saveOperationLog);    //发布事件
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);


            //推送消息 TODO 未完成接口
            //发送变更事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderSendCommunicateEvent zcOrderSendEvent = new ZcOrderSendCommunicateEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);

        } catch (Exception e) {
            log.error("系统异常：{}", e);
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR);
        }


        return true;
    }

    @Override
    @Transactional
    public boolean sendOrder(SendOrderParam sendOrderParam) {

        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(sendOrderParam.getPurchaseOrderCode());
        Order order = getOne(Condition.getQueryWrapper(orderQuery));
        getCurrentSupplierStatus(order.getSupplierCode());
        LoginInfo loginInfo = null;
        loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        if (loginInfo == null) {
            loginInfo = new LoginInfo();
            loginInfo.setId(new Long(1));
            loginInfo.setName("manage");
        }
        //推送消息 TODO 未完成接口调用事件接口
        //
        if (OrderTypeCode.ORDER_CHANGE.getCode().equals(order.getOrderStatus()) && OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(order.getChangeStatus())) {
            //修改订单状态 和确认信息
            update(Wrappers.<Order>update().lambda()
                    .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, sendOrderParam.getPurchaseOrderCode()));
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderSendDifferenceEvent zcOrderSendEvent = new ZcOrderSendDifferenceEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);


        }
        //1、草稿状态&&无变更 扭转待答交&&无变更
        else if (OrderTypeCode.ORDER_DRAFT.getCode().equals(order.getOrderStatus()) && OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(order.getChangeStatus())) {
            //修改订单状态 和确认信息
            update(Wrappers.<Order>update().lambda()
                    .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                    .set(Order::getOrderStatus, OrderTypeCode.ORDER_WAIT.getCode())
                    .eq(Order::getPurchaseOrderCode, sendOrderParam.getPurchaseOrderCode()));

            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderSendEvent zcOrderSendEvent = new ZcOrderSendEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);
        }

        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_SEND.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(sendOrderParam.getPurchaseOrderCode());
        iOrderOperationLogService.save(saveOperationLog);


        return true;
    }


//	@Async
//	@Override
//	public void syncErp() {
//		//查询所有的ERP订单
//		List<OrderErp> list = iSyncErpService.list();
//
//		//循环判断
//		for (int i = 0; i < list.size(); i++) {
//			OrderErp syncRep = list.get(i);
//			//查询当前已经存在的订单
//			Order orderQuery = new Order();
//			orderQuery.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
//			Order order = getOne(Condition.getQueryWrapper(orderQuery));
//			//1。查询出这个订单下所有的项次
//			OrderProductDetailsErp orderProductDetailsErpQuery = new OrderProductDetailsErp();
//			orderProductDetailsErpQuery.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
//			List<OrderProductDetailsErp> orderProductDetailsErps = iOrderProductDetailsErpService.list(Condition.getQueryWrapper(orderProductDetailsErpQuery));
//			if (order != null) {
//				//修改订单信息除了需要答交的字段
//				Order updateOrder = BeanUtil.copy(syncRep, Order.class);
//				updateOrder.setId(order.getId());
//				List<String> updateTypes = new ArrayList<>();
//				if ((updateOrder.getOrgId() == null) || (updateOrder.getOrgId().longValue() == 0L)) {
//					updateOrder.setOrgId(1L);
//				}
//
//				//如果订单属于草稿状态，那么就直接变更
//				if (OrderTypeCode.ORDER_DRAFT.getCode().equals(order.getOrderStatus())) {
//					//修改当前订单
//					updateById(updateOrder);
//				} else {
//
//					boolean operationSign = false;
//					//判断订单是否有变更
//					//2.循环判断所有的项次
//					for (OrderProductDetailsErp orderProductDetailsErp : orderProductDetailsErps) {
//						//更具当前的项次来判断
//						OrderProductDetails orderProductDetailsQuery = new OrderProductDetails();
//						orderProductDetailsQuery.setProductCode(orderProductDetailsErp.getProductCode());
//						orderProductDetailsQuery.setPurchaseOrderCode(orderProductDetailsErp.getPurchaseOrderCode());
//						//产品说不可能出现加项次的可能性，所以不做考虑
//						OrderProductDetails orderProductDetails = iOrderProductDetailsService.getOne(Condition.getQueryWrapper(orderProductDetailsQuery));
//						//3。判断项次中的 交期，数量，单价是否有变更
//						boolean sign = false;
//						OrderCommunicateLog orderCommunicateLog = new OrderCommunicateLog();
//						orderCommunicateLog.setPurchaseOrderCode(orderProductDetailsErp.getPurchaseOrderCode());
//						orderCommunicateLog.setOrderProductDetailsId(orderProductDetails.getId());
//						orderCommunicateLog.setItemCode(orderProductDetails.getItemCode());
//						if (!DateUtil.isSameDay(orderProductDetailsErp.getDeliveryTime(), orderProductDetails.getDeliveryTime())) {
//							sign = true;
//							orderCommunicateLog.setDeliveryTimeType(CommunicateCode.UPDATED.getCode());
//							orderCommunicateLog.setDeliveryTime(orderProductDetailsErp.getDeliveryTime());
//							updateTypes.add(OrderProductCode.DELIVERY_CHANGE.getMessage());
//						} else {
//							orderCommunicateLog.setDeliveryTimeType(CommunicateCode.NOT_UPDATED.getCode());
//							orderCommunicateLog.setDeliveryTime(orderProductDetails.getDeliveryTime());
//						}
//						if (!orderProductDetailsErp.getPurchaseNum().equals(orderProductDetails.getPurchaseNum())) {
//							sign = true;
//							orderCommunicateLog.setPurchaseNum(orderProductDetailsErp.getPurchaseNum());
//							orderCommunicateLog.setPurchaseNumType(CommunicateCode.UPDATED.getCode());
//							updateTypes.add(OrderProductCode.NUM_CHANGE.getMessage());
//
//						} else {
//							orderCommunicateLog.setPurchaseNum(orderProductDetails.getPurchaseNum());
//							orderCommunicateLog.setPurchaseNumType(CommunicateCode.NOT_UPDATED.getCode());
//						}
//						if (orderProductDetailsErp.getUnitPrice().compareTo(orderProductDetails.getUnitPrice()) != 0) {
//							sign = true;
//
//							orderCommunicateLog.setUnitPrice(orderProductDetailsErp.getUnitPrice());
//							orderCommunicateLog.setUnitPriceType(CommunicateCode.UPDATED.getCode());
//							updateTypes.add(OrderProductCode.PIRCE_CHANGE.getMessage());
//
//						} else {
//							orderCommunicateLog.setUnitPrice(orderProductDetails.getUnitPrice());
//							orderCommunicateLog.setUnitPriceType(CommunicateCode.NOT_UPDATED.getCode());
//						}
//						//4。如果发现变更，那么新增变更答交并且修改订单的状态添加答交信息
//						if (sign) {
//							orderCommunicateLog.setStatus(CommunicateCode.DIFFERENCE_STATUS_SEND.getCode());
//							orderCommunicateLog.setType(CommunicateTypeCode.NOT_UPDATED.getCode());
//							iOrderCommunicateLogService.save(orderCommunicateLog);
//							operationSign = true;
//						}
//						OrderProductDetails updateOrderProductDetails = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);
//						updateOrderProductDetails.setId(orderProductDetails.getId());
//						iOrderProductDetailsService.updateById(updateOrderProductDetails);
//
//					}
//
//					//如果进行了变更，记录变更操作
//					if (operationSign) {
//						//保存操作记录
//						OrderOperationLog saveOperationLog = new OrderOperationLog();
//						saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE_SYNC.getMessage());
//						saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
//						saveOperationLog.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
//						iOrderOperationLogService.save(saveOperationLog);
//
//						//保存变更记录
//						OrderChangeLog saveOrderChangeLog = new OrderChangeLog();
//						saveOrderChangeLog.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
//						saveOrderChangeLog.setSource("ERP同步");
//						if (updateTypes.size() > 0) {
//							String json = JSONArray.toJSONString(updateTypes);
//							saveOrderChangeLog.setUpdateTypes(json);
//						}
//						iOrderChangeLogService.save(saveOrderChangeLog);
//						LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
//						//发送变更事件
//						OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
//						saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
//						ZcOrderConfirmChangeEvent zcOrderSendEvent = new ZcOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent,order.getPurchaseCode(),order.getPurchaseName());
//						defaultEventPublisher.publishEvent(zcOrderSendEvent);
//						//修改当前订单状态
//						updateOrder.setMsgType(OrderMsgTypeCode.UNREAD.getCode());
//						updateOrder.setChangeStatus(OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode());
//						updateOrder.setOrderStatus(OrderTypeCode.ORDER_CHANGE.getCode());
//						updateOrder.setConfirmSource(ConfirmSourceCode.ZY.getCode());
//
//					}
//					//修改当前订单
//					updateById(updateOrder);
//				}
//			} else {
//				//如果不存在 那么就新增
//				Order saveOrder = BeanUtil.copy(syncRep, Order.class);
//				//设置默认状态
//				saveOrder.setOrderStatus(OrderTypeCode.ORDER_DRAFT.getCode());
//				saveOrder.setChangeStatus(OrderChangeTypeCode.ORDER_NO_CHANGE.getCode());
//				saveOrder.setSignContractStatus(SignContractCode.ORDER_SIGN_CONTRACT_NO.getCode());
//				saveOrder.setConfirmSource(ConfirmSourceCode.ZC.getCode());
//				saveOrder.setSumStatus(SumErpCode.ON_RECKON.getCode());
//				saveOrder.setOrgId(AuthUtil.getUser().getCurrentOrg().getId());
//				save(saveOrder);
//				//新增项次
//				for (OrderProductDetailsErp orderProductDetailsErp : orderProductDetailsErps) {
//					OrderProductDetails orderProductDetails = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);
//					iOrderProductDetailsService.save(orderProductDetails);
//				}
//
//			}
//		}
//	}

    @Transactional
    public void syncErpOrder(OrderErpDTO syncRep) {

        //订单变更原因
        List<String> updateReason = new ArrayList<>();
        //订单变更编码
        List<String> updateCode = new ArrayList<>();
        //是否变更标识
        boolean operationSign = false;

        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
        Order order = getOne(Condition.getQueryWrapper(orderQuery));


        List<OrderProductDetailsErpDTO> orderProductDetailsErps = syncRep.getOrderProductDetailsErpList();
        //判断订单是否存在
        if (order != null) {


            Order updateOrder = BeanUtil.copy(syncRep, Order.class);
            updateOrder.setId(order.getId());


            //1.循环判断所有的项次
            for (OrderProductDetailsErpDTO orderProductDetailsErp : orderProductDetailsErps) {
                Map<String, Boolean> changeList = new HashMap<>();
                //更具当前的项次来判断
                OrderProductDetails orderProductDetailsQuery = new OrderProductDetails();
                orderProductDetailsQuery.setProductCode(orderProductDetailsErp.getProductCode());
                orderProductDetailsQuery.setPurchaseOrderCode(orderProductDetailsErp.getPurchaseOrderCode());
                orderProductDetailsQuery.setItemCode(orderProductDetailsErp.getItemCode());
                OrderProductDetails orderProductDetails = iOrderProductDetailsService.getOne(Condition.getQueryWrapper(orderProductDetailsQuery));

                if (orderProductDetails == null) {
                    logger.info("采购单号" + orderProductDetailsErp.getProductCode() + "缺采购明细信息，重新添加");
                    OrderProductDetails orderProductDetails2 = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);

                    orderProductDetails2.setCreatedDate(new Date());
                    try {
                        iOrderProductDetailsService.save(orderProductDetails2);
                    } catch (Exception e) {
                        logger.error("信息有误，采购erp订单编号" + orderProductDetails2.getPurchaseOrderCode(), e);
                        e.printStackTrace();
                    }
                    logger.info("采购单号" + orderProductDetailsErp.getProductCode() + "重新添加成功");
                    break;
                }

                OrderProductDetails updateOrderProductDetails = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);

                updateOrderProductDetails.setId(orderProductDetails.getId());

                //查询最近一次变更记录
                OrderProductDetailsHistory orderProductDetailsHistoryQuery = new OrderProductDetailsHistory();
                orderProductDetailsHistoryQuery.setPdId(orderProductDetails.getId());
                List<OrderProductDetailsHistory> OrderProductDetailsHistoryList = iOrderProductDetailHistoryService.list(Condition.getQueryWrapper(orderProductDetailsHistoryQuery).lambda().orderByDesc(OrderProductDetailsHistory::getCreatedDate));

                OrderProductDetailsHistory orderProductDetailsHistoryERP = BeanUtil.copy(orderProductDetailsErp, OrderProductDetailsHistory.class);
                OrderProductDetails OrderProductDetailERP = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);


                //非首次变更
                if (!OrderProductDetailsHistoryList.isEmpty()) {
                    //判断具体那个字段发送变更
                    OrderProductDetailsHistory orderProductDetailsHistory = OrderProductDetailsHistoryList.get(0);

                    //对比字段最新的bis数据与采购工作台数据是否产生变更
                    changeList = CompareUtil.compareFields(orderProductDetails, OrderProductDetailERP, new String[]{"taxPrice", "deliveryTime", "purchaseNum", "taxRate"});


                    Boolean falg = changeList.get(CompareUtil.allFalg); //对比变更字段全部相等,则为旧数据无变更
                    String ChangeJson = JSONObject.toJSONString(changeList);
                    if (!falg) {
                        //本次变更列表
                        updateReason = getUpdateReason(changeList);
                        updateCode = getUpdateCode(changeList);
                        //与最新记录对比erp明细变更项是否发送变化
                        orderProductDetailsHistoryERP.setPdId(orderProductDetails.getId());
                        orderProductDetailsHistoryERP.setBeforeChange(ChangeJson);
                        iOrderProductDetailHistoryService.save(orderProductDetailsHistoryERP);
                        operationSign = true;
                    }
                }//首次变更
                else {
                    OrderProductDetails orderProductDetailsNew = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);

                    //第一次变更 对比主体为产品本身
                    changeList = CompareUtil.compareFields(orderProductDetails, orderProductDetailsNew, new String[]{"taxPrice", "deliveryTime", "purchaseNum", "taxRate"});
                    Boolean falg = changeList.get(CompareUtil.allFalg); //1.true对比字段全部相等
                    if (!falg) {
                        String ChangeJson = JSONObject.toJSONString(changeList);
                        //本次变更列表
                        updateReason = getUpdateReason(changeList);
                        updateCode = getUpdateCode(changeList);
                        //与最新记录对比变更项是否发送变化
                        orderProductDetailsHistoryERP.setPdId(orderProductDetails.getId());
                        orderProductDetailsHistoryERP.setBeforeChange(ChangeJson);
                        iOrderProductDetailHistoryService.save(orderProductDetailsHistoryERP);
                        operationSign = true;
                    }
                }
                //更新变化
                logger.info("采购订单编码" + updateOrderProductDetails.getPurchaseOrderCode());
                iOrderProductDetailsService.updateById(updateOrderProductDetails);

            }

            //如果进行了变更，记录变更操作
            if (operationSign) {
                logger.info("变更采购单号=----------" + order.getPurchaseOrderCode());

                //修改订单和变更状态
                updateOrder.setOrderStatus(OrderTypeCode.ORDER_CHANGE.getCode());
                updateOrder.setChangeStatus(OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode());

                //保存操作记录
                OrderOperationLog saveOperationLog = new OrderOperationLog();
                saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE_SYNC.getMessage());
                saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
                saveOperationLog.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
                iOrderOperationLogService.save(saveOperationLog);

                //保存变更记录
                OrderChangeLog saveOrderChangeLog = new OrderChangeLog();

                if (!updateReason.isEmpty()) {
                    String json = JSONArray.toJSONString(updateReason);
                    saveOrderChangeLog.setUpdateTypes(json);
                    //保存变更原因
                    updateOrder.setChangeReason(json);

                }

                if (!updateCode.isEmpty()) {
                    String changeOrderStatus = JSONArray.toJSONString(updateCode);
                    //保存变更原因
                    updateOrder.setChangeOrderStatus(changeOrderStatus);
                    saveOrderChangeLog.setChangeReason(changeOrderStatus);
                }


                saveOrderChangeLog.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
                saveOrderChangeLog.setSource(SourceCode.ERP.getMessage());
                saveOrderChangeLog.setChangeReason(syncRep.getChangeReason());
                iOrderChangeLogService.save(saveOrderChangeLog);


                //是否自动发送操作
                if (syncRep.getIsSend()) {
                    //自动发送
                    SendOrderParam sendOrderParam = new SendOrderParam();
                    sendOrderParam.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
                    sendOrder(sendOrderParam);

                }


            }
            //修改当前订单
            updateById(updateOrder);
        } else {
            //新增订单
            Order saveOrder = BeanUtil.copy(syncRep, Order.class);
            //设置默认状态
            saveOrder.setOrderStatus(OrderTypeCode.ORDER_DRAFT.getCode());
            saveOrder.setChangeStatus(OrderChangeTypeCode.ORDER_NO_CHANGE.getCode());
            saveOrder.setConfirmSource(ConfirmSourceCode.ZC.getCode());
            save(saveOrder);
            //新增项次
            for (OrderProductDetailsErpDTO orderProductDetailsErp : orderProductDetailsErps) {
                OrderProductDetails orderProductDetails = BeanUtil.copy(orderProductDetailsErp, OrderProductDetails.class);

                orderProductDetails.setCreatedDate(new Date());
                orderProductDetails.setPlanNo(numberFactory.buildNumber(NumberType.plan));
                try {
                    iOrderProductDetailsService.save(orderProductDetails);
                } catch (Exception e) {
                    logger.error("信息有误，采购erp订单编号" + orderProductDetails.getPurchaseOrderCode(), e);
                    e.printStackTrace();
                }
            }

            //是否自动发送操作
            if (syncRep.getIsSend()) {
                //自动发送
                SendOrderParam sendOrderParam = new SendOrderParam();
                sendOrderParam.setPurchaseOrderCode(syncRep.getPurchaseOrderCode());
                sendOrder(sendOrderParam);

            }

        }

    }

    public List<String> getUpdateReason(Map<String, Boolean> map) {
        List<String> changeLogList = new ArrayList();
        for (OrderChangeReasonEnum value : OrderChangeReasonEnum.values()) {
            Boolean falg = Optional.ofNullable(map.get(value.getKey())).orElse(true);
            if (!falg) {
                changeLogList.add(value.getReason());
            }

        }


        return changeLogList;
    }

    public List<String> getUpdateCode(Map<String, Boolean> map) {
        List<String> changeLogList = new ArrayList();
        for (OrderChangeReasonEnum value : OrderChangeReasonEnum.values()) {
            Boolean falg = Optional.ofNullable(map.get(value.getKey())).orElse(true);
            if (!falg) {
                changeLogList.add(value.getCode());
            }

        }


        return changeLogList;
    }


    public static Function<OrderErpDTO, String> distinctByKeyFunction() {
        return (OrderErpDTO order) -> order.getPurchaseCode() + "-" + order.getSupplierCode();
    }


    @Override
    public void syncErp(List<OrderErpDTO> orderErpDTOlist) {

        List<String> failSychOrder = new ArrayList<>();
        //调用协同服务接口查询是否开启订单自动发送
        List<CfgParam> cfgParam = getParamAllByCode(CfgParamCode.ORDER_ATUO_SEND).getData();
        //根据采购方和供应商编码去重
        List<OrderErpDTO> supplierAndCodeList = orderErpDTOlist.stream().filter(CollectionUtil.distinctByKey(distinctByKeyFunction())).collect(Collectors.toList());//
        logger.info("本次同步供应商-采购方-----------------");
        int i = 0;
        for (OrderErpDTO orderErpDTO : supplierAndCodeList) {
            i++;
            logger.info(i + "--同步供应商-采购方" + orderErpDTO.getSupplierCode() + "--" + orderErpDTO.getPurchaseCode());

        }
        logger.info("----------------------------------");
        List<OrgIdQuery> orgIdQueryList = BeanUtil.copy(supplierAndCodeList, OrgIdQuery.class);
        //根据采购方和供应商编码批量获取组织id
        R<List<OrgIdDTO>> infos = purchaserFeignClient.infos(orgIdQueryList);


        //供应商去重
        List<OrderErpDTO> disSupplierCode = orderErpDTOlist.stream()
                .filter(item -> !StringUtils.isEmpty(item.getSupplierCode()))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(OrderErpDTO::getSupplierCode))), ArrayList::new));

        List<SupplierDTO> OaSupplierDTOList = new ArrayList<>();
        disSupplierCode.forEach(item -> {
            SupplierDTO oaSupplierDTO = new SupplierDTO();
//			System.out.println(item.getSupplierCode());
            oaSupplierDTO.setCode(item.getSupplierCode());

            OaSupplierDTOList.add(oaSupplierDTO);
        });

        //获取供应商的合作状态
        R<List<SupplierDTO>> supplierByCode = supplierFeignClient.findSupplierByCode(OaSupplierDTOList);
        List<SupplierDTO> supplierList = new ArrayList<>();
        if (supplierByCode.isSuccess()) {
            supplierList = supplierByCode.getData();
            if (supplierList.isEmpty()) {
                throw new ApiException(500, "供应商不存在");
            }
        }

        //拼装订单设置组织id和自动发送
        if (infos.isSuccess()) {
            List<OrgIdDTO> OrgIdDTOList = infos.getData();
            for (OrgIdDTO orgIdDTO : OrgIdDTOList) {
                for (OrderErpDTO orderErpDTO : orderErpDTOlist) {
                    if (orgIdDTO.getSupplierCode().equals(orderErpDTO.getSupplierCode())
                            && orgIdDTO.getPurchaseCode().equals(orderErpDTO.getPurchaseCode())) {
                        //设置组织id
                        orderErpDTO.setOrgId(orgIdDTO.getOrgId());
                        orderErpDTO.setSupplierName(orgIdDTO.getSupplierame());
                        orderErpDTO.setPurchaseName(orgIdDTO.getPurchaseName());
                        CfgParam cfg = cfgParam.stream().filter(item -> item.getOrgId().equals(orgIdDTO.getOrgId())).findAny().orElse(null);
                        if (cfg != null) {
                            orderErpDTO.setIsSend(cfg.getValue().equals("true"));
                        }
                    }
                }

            }

//			List<SupplierDTO> finalSupplierList = supplierList;
//			for (OrderErpDTO syncRep : orderErpDTOlist) {
//				if (syncRep.getOrgId()==null) {
//					logger.info("采购订单"+syncRep.getPurchaseOrderCode()+"--组织不存在");
//					failSychOrder.add(syncRep.getPurchaseOrderCode());
//					continue;
//				}
//				//判断订单的供应商合作状态
//				SupplierDTO oaSupplierDTOOptional = finalSupplierList.stream().filter(item -> item.getCode().equals(syncRep.getSupplierCode())).findAny().orElse(null);
//				if (oaSupplierDTOOptional != null && SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(oaSupplierDTOOptional.getRelevanceStatus())) {
//					try {
//
//						logger.info("采购订单={},供应商编码={},采购编码={},对应美尚组织={}",syncRep.getPurchaseOrderCode(),syncRep.getSupplierCode(),syncRep.getPurchaseCode(),syncRep.getOrgId());
//						syncErpOrder(syncRep);
//					} catch (Exception e) {
//						log.error("同步订单异常：订单号" + syncRep.getPurchaseOrderCode());
//						failSychOrder.add(syncRep.getPurchaseOrderCode());
//						e.printStackTrace();
//					} finally {
//
//					}
//				}
//			}

            List<SupplierDTO> finalSupplierList = supplierList;
            for (OrderErpDTO syncRep : orderErpDTOlist) {
//				if (syncRep.getOrgId()==null) {
//					logger.info("采购订单"+syncRep.getPurchaseOrderCode()+"--组织不存在");
//					failSychOrder.add(syncRep.getPurchaseOrderCode());
//					continue;
//				}
                //判断订单的供应商合作状态
                SupplierDTO oaSupplierDTOOptional = finalSupplierList.stream().filter(item -> item.getCode().equals(syncRep.getSupplierCode())).findAny().orElse(null);
//				if (oaSupplierDTOOptional != null && SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(oaSupplierDTOOptional.getRelevanceStatus())) {
                try {

                    logger.info("采购订单={},供应商编码={},采购编码={},对应美尚组织={}", syncRep.getPurchaseOrderCode(), syncRep.getSupplierCode(), syncRep.getPurchaseCode(), syncRep.getOrgId());
                    syncErpOrder(syncRep);
                } catch (Exception e) {
                    log.error("同步订单异常：订单号" + syncRep.getPurchaseOrderCode());
                    failSychOrder.add(syncRep.getPurchaseOrderCode());
                    e.printStackTrace();
                } finally {

                }
//				}
            }

        }

        //返回失败订单列表


    }


    @Override
//	@Async
    public void syncErpOrder() {
        R<Object> result = purchaseOrderFeignClient.syncOrder();
        Assert.isTrue(result.isSuccess(), result.getMsg());
    }

    @Override
    public void updateRelationOrder(OrderSych orderSych) {
        //查询当前订单状态
        Order orderQuery = new Order();
        orderQuery.setOrgId(null);
        orderQuery.setPurchaseCode(orderSych.getPurchaseCode());

        QueryWrapper<Order> condition = new QueryWrapper<>();
        condition.lambda().select(Order::getId, Order::getSupplierCode).eq(Order::getPurchaseCode, orderSych.getPurchaseCode())
                .in(Order::getSupplierCode, orderSych.getSupplierCode()).isNull(Order::getOrgId);

        List<Order> orderList = list(condition);

        if (!CollectionUtils.isEmpty(orderList)) {

            Map<String, String> supplierCodeNameMap = orderSych.getSupplierCodeNameMap();

            for (Order order : orderList) {
                order.setOrgId(orderSych.getOrgId());
                order.setPurchaseName(orderSych.getPurchaseName());
                String name = supplierCodeNameMap.get(order.getSupplierCode());
                if (StrUtil.isNotBlank(name)) {
                    order.setSupplierName(name);
                }
            }
            saveOrUpdateBatch(orderList);
        }

    }


    @Override
    public boolean sendChangeOrder(List<OrderCommunicateLogParam> sendOrderParam) {
        //查询产品信息
        OrderProductDetails orderProductDetails = iOrderProductDetailsService.getById(sendOrderParam.get(0).getOrderProductDetailsId());
        //查询当前订单状态
        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(sendOrderParam.get(0).getPurchaseOrderCode());
        Order order = getOne(Condition.getQueryWrapper(orderQuery));
        String purchaseOrderCode = order.getPurchaseOrderCode();
        for (int j = 0; j < sendOrderParam.size(); j++) {
            OrderCommunicateLogParam orderCommunicateLogParam = sendOrderParam.get(j);
            orderCommunicateLogParam.setType(OrderSendCode.ORDER_SEND_CHANGE.getCode());
            OrderCommunicateLog orderCommunicateLog = BeanUtil.copy(orderCommunicateLogParam, OrderCommunicateLog.class);
            //作废之前的答交信息
            iOrderCommunicateLogService.update((Wrappers.<OrderCommunicateLog>update().lambda()
                    .set(OrderCommunicateLog::getStatus, CommunicateStatusCode.DIFFERENCE_TYPE.getCode())
                    .eq(OrderCommunicateLog::getPurchaseOrderCode, purchaseOrderCode))
                    .eq(OrderCommunicateLog::getOrderProductDetailsId, orderProductDetails.getId())
            );


            //进行数据设置
            if (orderCommunicateLog.getDeliveryTime() != null) {
                orderCommunicateLog.setDeliveryTimeType(CommunicateCode.UPDATED.getCode());
            } else {
                orderCommunicateLog.setDeliveryTimeType(CommunicateCode.NOT_UPDATED.getCode());
            }
            if (orderCommunicateLog.getPurchaseNum() != null) {
                orderCommunicateLog.setPurchaseNumType(CommunicateCode.UPDATED.getCode());
            } else {
                orderCommunicateLog.setPurchaseNumType(CommunicateCode.NOT_UPDATED.getCode());
            }
            if (orderCommunicateLog.getUnitPrice() != null) {
                orderCommunicateLog.setUnitPriceType(CommunicateCode.UPDATED.getCode());
            } else {
                orderCommunicateLog.setUnitPriceType(CommunicateCode.NOT_UPDATED.getCode());
            }
            orderCommunicateLog.setStatus(CommunicateCode.DIFFERENCE_STATUS_SEND.getCode());
            //设置产品信息
            orderCommunicateLog.setOrderProductDetailsId(orderProductDetails.getTakeOverDepartmentId());
            orderCommunicateLog.setItemCode(orderProductDetails.getItemCode());
            orderCommunicateLog.setPurchaseOrderCode(orderProductDetails.getPurchaseOrderCode());

            if (order.getChangeStatus() == OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode() || order.getChangeStatus() == OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode()) {
                orderCommunicateLog.setType(CommunicateTypeCode.NOT_UPDATED.getCode());
            } else {
                orderCommunicateLog.setType(CommunicateTypeCode.UPDATED.getCode());
            }
            orderCommunicateLog.setStatus(CommunicateStatusCode.NOT_UPDATED.getCode());
            iOrderCommunicateLogService.saveOrUpdate(orderCommunicateLog);
        }


        //修改订单状态 和确认信息
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_DIFFERENCE.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_UNCONFIRMED_CHANGE.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);

        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //发布事件
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        ZcOrderConfirmChangeEvent zcOrderSendEvent = new ZcOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return true;
    }


    @Override
    public boolean saveCommunicateOrder(List<OrderCommunicateLogParam> sendOrderParam) {
        OrderCommunicateLogParam orderCommunicateLogParam = sendOrderParam.get(0);

        //先删除历史的
        //删除保存的日志
        OrderCommunicateLogSave orderCommunicateLogSaveDel = new OrderCommunicateLogSave();
        orderCommunicateLogSaveDel.setOrderProductDetailsId(orderCommunicateLogParam.getId());
        orderCommunicateLogSaveDel.setPurchaseOrderCode(orderCommunicateLogParam.getPurchaseOrderCode());
        orderCommunicateLogSaveDel.setCreatedBy(AuthUtil.getUser().getUserId());
        iOrderCommunicateLogSaveService.remove(Condition.getQueryWrapper(orderCommunicateLogSaveDel));

        OrderCommunicateLogSave orderCommunicateLogSave = new OrderCommunicateLogSave();
        orderCommunicateLogSave.setPurchaseOrderCode(orderCommunicateLogParam.getPurchaseOrderCode());
        orderCommunicateLogSave.setOrderProductDetailsId(orderCommunicateLogParam.getOrderProductDetailsId());
        //把集合转json
        String entityJson = JSON.toJSONString(sendOrderParam);
        orderCommunicateLogSave.setSysType(ConfirmSourceCode.ZC.getCode());
        orderCommunicateLogSave.setEntityJson(entityJson);
        iOrderCommunicateLogSaveService.save(orderCommunicateLogSave);

        return true;
    }

    @Override
    public boolean sendRemindMsg(String purchaseOrderCode) {
        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_MSG.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);

        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(orderQuery));

        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        //发布事件
        ZcOrderSendRemindMsgEvent zcOrderSendEvent = new ZcOrderSendRemindMsgEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return false;
    }

    @Override
    @Transactional
    public boolean confirmOrder(String purchaseOrderCode) {


        Order orderOriginal = new Order();
        orderOriginal.setPurchaseOrderCode(purchaseOrderCode);
        orderOriginal = getOne(Condition.getQueryWrapper(orderOriginal));
        getCurrentSupplierStatus(orderOriginal.getSupplierCode());
        //获取当前登录信息
        BaseUserEntity currentUser = AuthUtil.getUser();


        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .set(Order::getConfirmTime, new Date())
                .set(Order::getConfirmUserId, currentUser.getUserId())
                .set(Order::getConfirmUserName, currentUser.getUserName())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));


        OrderOperationLog saveOperationLog = new OrderOperationLog();
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //如果订单状态为变更中，那么要把变更状态也进行变更
        if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderOriginal.getOrderStatus()) && OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode().equals(orderOriginal.getChangeStatus())) {

            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());
            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderConfirmChangeEvent zcOrderSendEvent = new ZcOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, orderOriginal.getPurchaseCode(), orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);
        } else if

            //如果是待答交状态下
        (OrderTypeCode.ORDER_WAIT.getCode().equals(orderOriginal.getOrderStatus()) && OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(orderOriginal.getChangeStatus())) {
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES.getMessage());

            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderConfirmMsgEvent zcOrderSendEvent = new ZcOrderConfirmMsgEvent(this, loginInfo, saveOperationLogEvent, orderOriginal.getPurchaseCode(), orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);

        }

        //保存操作记录
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);


        return true;
    }

    @Override
    @Transactional
    public boolean confirmOrder_old(String purchaseOrderCode) {


        Order orderOriginal = new Order();
        orderOriginal.setPurchaseOrderCode(purchaseOrderCode);
        orderOriginal = getOne(Condition.getQueryWrapper(orderOriginal));

        //获取当前登录信息
        BaseUserEntity currentUser = AuthUtil.getUser();
        //把未生效的答交日志修改成失效
        iOrderCommunicateLogService.update((Wrappers.<OrderCommunicateLog>update().lambda()
                .set(OrderCommunicateLog::getStatus, CommunicateStatusCode.UPDATED.getCode())
                .eq(OrderCommunicateLog::getPurchaseOrderCode, purchaseOrderCode)
                .eq(OrderCommunicateLog::getStatus, CommunicateStatusCode.NOT_UPDATED.getCode())));

        //进行数据计算生成最后确认金额
        OrderCommunicateLog orderQueryCommunicateLog = new OrderCommunicateLog();
        orderQueryCommunicateLog.setStatus(CommunicateStatusCode.UPDATED.getCode());
        orderQueryCommunicateLog.setPurchaseOrderCode(purchaseOrderCode);
        List<OrderCommunicateLog> orderCommunicateLogList = iOrderCommunicateLogService.list(Condition.getQueryWrapper(orderQueryCommunicateLog));
        //循环计算
        BigDecimal orderConfirmPrice = new BigDecimal("0.00");
        for (OrderCommunicateLog orderCommunicateLog : orderCommunicateLogList) {
            //单价
            BigDecimal unitPrice = orderCommunicateLog.getUnitPrice();
            //数量
            BigDecimal purchaseNum = orderCommunicateLog.getPurchaseNum();
            //总价=单价*数量  保留两位小数
            orderConfirmPrice = orderConfirmPrice.add(SumUtils.multiply(unitPrice, purchaseNum, 2));
        }
        Order order = null;
        if (orderCommunicateLogList.size() == 0) {
            //查询当前订单 如果订单直接确认那么订单金额就是确认金额
            Order queryOrder = new Order();
            queryOrder.setPurchaseOrderCode(purchaseOrderCode);
            order = getOne(Condition.getQueryWrapper(queryOrder));
            orderConfirmPrice = order.getOrderPrice();
        }


        //调用协同服务接口查询是否开启订单网签开关
        CfgParam cfgParam = getParamByCode(CfgParamCode.ORDER_NET_SIGN_SWITCH).getData();
        SignContractCode signContractCode = null;


        if (StringUtils.pathEquals(cfgParam.getValue(), "true")) {
            //调用接口查看公司开关是否开启
            Order queryOrder = new Order();
            queryOrder.setPurchaseOrderCode(purchaseOrderCode);
            SupplierElectricSignatureConfig byCodeAndType = getByCodeAndType(getOne(Condition.getQueryWrapper(queryOrder)).getPurchaseCode(), ElectricSignatureConfigType.ORDER);

            if (!ObjectUtils.isEmpty(byCodeAndType) && byCodeAndType.isEnabled()) {
                //表示开启,如果为空默认不开启
                //--------------------------------------------------------------
                //调用接口获取是否成功签约（接口还未开发，目前使用模拟数据）
                SecureRandom random = new SecureRandom();
                if (random.nextBoolean()) {
                    //签约成功
                    signContractCode = SignContractCode.ORDER_SIGN_CONTRACT_SIGNED;
                } else {
                    //签约失败
                    signContractCode = SignContractCode.ORDER_SIGN_CONTRACT_ABNORMAL;
                }
            } else {
                //未开启网签
                signContractCode = SignContractCode.ORDER_SIGN_CONTRACT_WAIT;
            }
        } else {
            //未开启网签
            signContractCode = SignContractCode.ORDER_SIGN_CONTRACT_WAIT;
        }

        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .set(Order::getConfirmTime, new Date())
                .set(Order::getConfirmUserId, currentUser.getUserId())
                .set(Order::getConfirmUserName, currentUser.getUserName())
                .set(Order::getOrderConfirmPrice, orderConfirmPrice)
                .set(Order::getSignContractStatus, signContractCode.getCode())
                //.set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

        //数据回写到ERP TODO 未完成接口需要调用ERP
        //1.查询出原来的ERP数据
        OrderErp queryOrderErp = new OrderErp();
        queryOrderErp.setPurchaseOrderCode(purchaseOrderCode);
        OrderErp orderErp = iSyncErpService.getOne(Condition.getQueryWrapper(queryOrderErp));
        //1.查询出已经修改后的数据
        Order queryOrderAft = new Order();
        queryOrderAft.setPurchaseOrderCode(purchaseOrderCode);
        Order orderAft = getOne(Condition.getQueryWrapper(queryOrderAft));
        OrderErp updateOrderErp = BeanUtil.copy(orderAft, OrderErp.class);
        updateOrderErp.setId(orderErp.getId());
        iSyncErpService.updateById(updateOrderErp);


        OrderOperationLog saveOperationLog = new OrderOperationLog();
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //如果订单状态为变更中，那么要把变更状态也进行变更
        if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderOriginal.getOrderStatus()) && (OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderOriginal.getChangeStatus()) || OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode().equals(orderOriginal.getChangeStatus()))) {
            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    //.set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());
            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderConfirmChangeEvent zcOrderSendEvent = new ZcOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, orderOriginal.getPurchaseCode(), orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);
        } else if (OrderTypeCode.ORDER_DIFFERENCE.getCode().equals(orderOriginal.getOrderStatus())) {
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES.getMessage());
            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZcOrderConfirmMsgEvent zcOrderSendEvent = new ZcOrderConfirmMsgEvent(this, loginInfo, saveOperationLogEvent, orderOriginal.getPurchaseCode(), orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);

        }

        //保存操作记录
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);


        return true;
    }


    /**
     * 调用接口查看网签公司开关是否开启
     *
     * @param code，type
     * @return net.bncloud.common.api.R<net.bncloud.order.entity.CfgParam>
     * @author wangyifan
     * @date 2021/5/8
     */
    private SupplierElectricSignatureConfig getByCodeAndType(String code, ElectricSignatureConfigType type) {
        R<SupplierElectricSignatureConfig> supplierElectricSignatureConfigR;
        try {
            supplierElectricSignatureConfigR = supplierElectricSignatureConfigResourceFeignClient.getByCodeAndType(code, type);
        } catch (Exception e) {
            log.error("查询订单网签公司开关接口请求失败");
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR);
        }
        if (R.isNotSuccess(supplierElectricSignatureConfigR)) {
            log.error("查询订单网签公司开关接口请求失败");
            throw new ApiException(supplierElectricSignatureConfigR.getCode(), supplierElectricSignatureConfigR.getMsg());
        }
        return supplierElectricSignatureConfigR.getData();
    }

    /**
     * 调用接口查询是否
     *
     * @param cfgParamCode
     * @return net.bncloud.common.api.R<net.bncloud.order.entity.CfgParam>
     * @author wangyifan
     * @date 2021/5/8
     */
    private R<CfgParam> getParamByCode(CfgParamCode cfgParamCode) {
        R<CfgParamDTO> cfgParamR;
        try {
            cfgParamR = cfgParamResourceFeignClient.getParamByCode(cfgParamCode.getCode());
        } catch (Exception e) {
            log.error("查询订单网签开关接口请求失败");
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR);
        }
        if (R.isNotSuccess(cfgParamR)) {
            log.error("查询订单网签开关接口请求失败");
            throw new ApiException(cfgParamR.getCode(), cfgParamR.getMsg());
        }
        CfgParamDTO data = cfgParamR.getData();
        if( data != null){
            CfgParam cfgParam = BeanUtil.copy(data, CfgParam.class);
            return R.data( cfgParam );
        }
        return R.data(null);
    }


    /**
     * 调用接口查询是否
     *
     * @param cfgParamCode
     * @return net.bncloud.common.api.R<net.bncloud.order.entity.CfgParam>
     * @author wangyifan
     * @date 2021/5/8
     */
    private R<List<CfgParam>> getParamAllByCode(CfgParamCode cfgParamCode) {
        R<List<CfgParamDTO>> cfgParamR;
        try {
            cfgParamR = cfgParamResourceFeignClient.findListByCode(cfgParamCode.getCode());
        } catch (Exception e) {
            log.error("查询订单网签开关接口请求失败");
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR);
        }
        if (R.isNotSuccess(cfgParamR)) {
            log.error("查询订单网签开关接口请求失败");
            throw new ApiException(cfgParamR.getCode(), cfgParamR.getMsg());
        }
        List<CfgParamDTO> cfgParamDTOList = cfgParamR.getData();
        if( cfgParamDTOList != null){
            List<CfgParam> cfgParamList = cfgParamDTOList.stream().map(cfgParamDTO -> BeanUtil.copy(cfgParamDTO, CfgParam.class)).collect(Collectors.toList());
            return R.data( cfgParamList );
        }
        return R.data(null);
    }

    @Override
    public boolean confirmChangeOrder(String purchaseOrderCode) {
        //获取当前登录信息
        LoginInfo currentUser = SecurityUtils.getLoginInfo().get();//TODO
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));
        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .set(Order::getConfirmTime, new Date())
                .set(Order::getConfirmUserId, currentUser.getId())
                .set(Order::getConfirmUserName, currentUser.getName())
                .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        //发布事件
        ZcOrderConfirmChangeEvent zcOrderSendEvent = new ZcOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return true;
    }


    @Override
    public boolean stopOrder(String purchaseOrderCode) {
        //查询当前订单
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));

        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_STOP.getCode())
                .set(Order::getBeforeOrderStatus, order.getOrderStatus())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_STOP.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        //发布事件
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        ZcOrderStopEvent zcOrderSendEvent = new ZcOrderStopEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return true;
    }

    @Override
    public boolean startOrder(String purchaseOrderCode) {
        //查询当前订单
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));

        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getOrderStatus, order.getBeforeOrderStatus())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_START.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        //发布事件
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        ZcOrderStartEvent zcOrderSendEvent = new ZcOrderStartEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return true;
    }

    @Override
    public boolean confirmDifferenceOrder(String purchaseOrderCode) {
        //获取当前登录信息
        LoginInfo currentUser = SecurityUtils.getLoginInfo().get();
        //查询当前订单
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));
        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .set(Order::getConfirmTime, new Date())
                .set(Order::getConfirmUserId, currentUser.getId())
                .set(Order::getConfirmUserName, currentUser.getName())
                .set(Order::getChangeStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_DIFFERENCE.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //发布事件
        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
        ZcOrderConfirmDifferenceEvent zcOrderSendEvent = new ZcOrderConfirmDifferenceEvent(this, loginInfo, saveOperationLogEvent, order.getPurchaseCode(), order.getPurchaseName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);
        return true;
    }


    /**
     * 不分页，根据条件查询所有的订单
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<ExportOrderModel> getOrderListByCondition(QueryParam<OrderParam> queryParam) {
        List<Order> orders = baseMapper.selectListPage(null, queryParam);
        if (CollectionUtil.isEmpty(orders)) {
            return null;
        }
        //转换成 ExportOrderModel
        List<ExportOrderModel> modelList = new ArrayList<>();
        // 查询订单的产品明细
        Set<String> orderCodeSet = orders.stream().map(Order::getPurchaseOrderCode).collect(Collectors.toSet());
        List<OrderProductDetails> orderProductDetailsList = orderProductDetailsService.list(Wrappers.<OrderProductDetails>lambdaQuery().in(OrderProductDetails::getPurchaseOrderCode, orderCodeSet));
        orders.forEach(order -> {
            orderProductDetailsList.stream().filter(product -> product.getPurchaseOrderCode().equals(order.getPurchaseOrderCode())).forEach(productDetail -> {
                ExportOrderModel model = BeanUtil.copy(productDetail, ExportOrderModel.class);
                BeanUtil.copyProperties(order, model);
                // //采购日期 时间+8
                // model.setPurchaseTime(model.getPurchaseTime()!=null?DateUtil.plusHours(model.getPurchaseTime(),8L):null);
                // //确认日期 时间+8
                // model.setConfirmTime(model.getConfirmTime()!=null?DateUtil.plusHours(model.getConfirmTime(),8L):null);
                //设置订单状态  code转换成name
                setOrderStatus(model);
                //设置变更状态
                setChangeStatus(model);
                modelList.add(model);
            });
        });
        return modelList;


    }

    /**
     * 订单状态 ：
     * 1草稿
     * 2待答交
     * 3已留置
     * 4答交差异
     * 5退回
     * 6变更中
     * 7已确认
     * 8已完成
     */
    public void setOrderStatus(ExportOrderModel model){
        if (model==null) return;
        switch (model.getOrderStatus()){
            case "1":
                model.setOrderStatus("草稿");
                break;
            case "2":
                model.setOrderStatus("待答交");
                break;
            case "3":
                model.setOrderStatus("已留置");
                break;
            case "4":
                model.setOrderStatus("答交差异");
                break;
            case "5":
                model.setOrderStatus("退回");
                break;
            case "6":
                model.setOrderStatus("变更中");
                break;
            case "7":
                model.setOrderStatus("已确认");
                break;
            case "8":
                model.setOrderStatus("已完成");
                break;
        }

    }


    /**
     * 变更状态 ：
     * 1无变更
     * 2已确认变更
     * 3未确认变更
     * 4待确认变更
     */
    public void setChangeStatus(ExportOrderModel model){
        if (model==null) return;
        switch (model.getChangeStatus()){
            case "1":
                model.setChangeStatus("无变更");
                break;
            case "2":
                model.setChangeStatus("已确认变更");
                break;
            case "3":
                model.setChangeStatus("未确认变更");
                break;
            case "4":
                model.setChangeStatus("待确认变更");
                break;
        }
    }
    @Override
    public void addOrderProductDetailInventoryQuantity(String orderProductDetailSourceId, BigDecimal receiptQuantity) {
        log.info("[addOrderProductDetailInventoryQuantity] params:{},{}",orderProductDetailSourceId,receiptQuantity);
        // 查询订单产品明细记录
        OrderProductDetails orderProductDetailRecord = orderProductDetailsService.getOne(Wrappers.<OrderProductDetails>lambdaQuery().select(OrderProductDetails::getId).eq(OrderProductDetails::getSourceId, orderProductDetailSourceId));
        Assert.notNull(orderProductDetailRecord, String.format("未查找到订单明细信息，orderProductDetailSourceId: %s", orderProductDetailSourceId));

        orderProductDetailsMapper.addInventoryQuantity(orderProductDetailSourceId, receiptQuantity);

    }

    /**
     * mrp按订单送货创建收料通知单回写的更新方法
     * @return
     */
    @Override
    public void updateErpId(Long orderId, Long fId, String fNumber, String code) {
        orderMapper.updateErpId(orderId, fId, fNumber,code);
    }
}
