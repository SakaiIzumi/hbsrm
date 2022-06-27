package net.bncloud.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.util.*;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.order.constants.*;
import net.bncloud.order.entity.*;
import net.bncloud.order.event.*;
import net.bncloud.order.feign.InformationServiceFeignClient;
import net.bncloud.order.feign.SupplierElectricSignatureConfigResourceFeignClient;
import net.bncloud.order.mapper.OrderMapper;
import net.bncloud.order.param.OrderCommunicateLogParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.service.*;
import net.bncloud.order.service.mapstruct.OrderVoConverter;
import net.bncloud.order.vo.*;
import net.bncloud.order.vo.event.OrderEvent;
import net.bncloud.order.wrapper.OrderWrapper;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
//import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
//import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private IOrderProductDetailsService iOrderProductDetailsService;

    @Autowired
    private IOrderCommunicateLogService iOrderCommunicateLogService;

    @Autowired
    private IOrderOperationLogService iOrderOperationLogService;

    @Autowired
    private DefaultEventPublisher defaultEventPublisher;

    @Autowired
    private OrderVoConverter orderVoConverter;

    @Autowired
    private IOrderErpService iOrderErpService;

    @Autowired
    private IOrderCommunicateLogSaveService iOrderCommunicateLogSaveService;
    @Resource
    private InformationServiceFeignClient informationServiceFeignClient;


    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    @Resource
    private SupplierElectricSignatureConfigResourceFeignClient supplierElectricSignatureConfigResourceFeignClient;
    @Resource
    private SupplierFeignClient supplierFeignClient;
    @Resource
    private IOrderProductDetailsService orderProductDetailsService;

    public void getCurrentSupplierStatus(String code){
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(code);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        if (oneSupplierByCode.isSuccess()) {
            SupplierDTO supplier = oneSupplierByCode.getData();
            if (supplier==null) {
                log.error("供应商"+supplier.getCode()+"不存在");
                throw new ApiException(500,"供应商"+supplier.getCode()+"不存在");
            }

            if (supplier.getRelevanceStatus().equals(SupplierRelevanceStatusEnum.FROZEN.getCode())) {
                log.error("供应商"+supplier.getCode()+"状态"+SupplierRelevanceStatusEnum.FROZEN.getName());
                throw new ApiException(500,"供应商编号"+supplier.getCode()+"状态"+SupplierRelevanceStatusEnum.FROZEN.getName());
            }

        }
    }

    @Override
    public Page<OrderVo> selectListPage(QueryParam<OrderParam> queryParam, Pageable pageable) {

        IPage<Order> page = PageUtils.toPage(pageable);
        OrderParam param = queryParam.getParam();
        param.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        IPage<Order> orderVoIPage = page.setRecords(baseMapper.selectListPage(page, queryParam));
        IPage<OrderVo> pageVO = OrderWrapper.build().pageVO(orderVoIPage);

        pageVO.getRecords().forEach( orderVo->{
            //设置产品明细信息
            List<OrderProductDetails> productDetailsList = orderProductDetailsService.list(Wrappers.<OrderProductDetails>lambdaQuery().eq(OrderProductDetails::getPurchaseOrderCode, orderVo.getPurchaseOrderCode()));
            List<ProductUnderOrderListVo> productLists = BeanUtil.copyProperties(productDetailsList, ProductUnderOrderListVo.class);
            orderVo.setChildren(productLists);
            //设置权限按钮
            PermissionButtonVo permissonButton = getPermissonButton(orderVo);
            orderVo.setPermissionButton(permissonButton);
        });
        return PageUtils.result(pageVO);
    }

    @Override
    public OrderVo getOrderDetails(String purchaseOrderCode) {
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));
        //转换成视图对象
        OrderVo orderVo = OrderWrapper.build().entityVO(order);

        PermissionButtonVo permissionButtonVo = getPermissonButton(orderVo);
        orderVo.setPermissionButton(permissionButtonVo);


        if (ConfirmSourceCode.ZY.getCode() == orderVo.getConfirmSource()&&!OrderTypeCode.ORDER_DRAFT.getCode().equals(orderVo.getOrderStatus())) {
            //设置订单已读状态
            update(Wrappers.<Order>update().lambda()
                    .set(Order::getMsgType, OrderMsgTypeCode.READ.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
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
        if (orderVo.getConfirmSource() != null && ConfirmSourceCode.ZY.getCode() == orderVo.getConfirmSource()) {
            //订单状态在待答交状态、无变更
            if (OrderTypeCode.ORDER_WAIT.getCode().equals(orderVo.getOrderStatus())&&OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(orderVo.getChangeStatus())) {
                permissionButtonVo.setConfirm(true);
            }
            //订单状态在变更中、带确认变更
            if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderVo.getOrderStatus())&&OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderVo.getChangeStatus())) {
                permissionButtonVo.setConfirmChange(true);
            }


        }


        return permissionButtonVo;
    }

    @Override
    @Transactional
    public boolean sendOrder(List<OrderCommunicateLogParam> sendOrderParam) {


        //查询当前订单状态
        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(sendOrderParam.get(0).getPurchaseOrderCode());
        Order order = getOne(Condition.getQueryWrapper(orderQuery));
        String purchaseOrderCode = order.getPurchaseOrderCode();

        //作废之前的答交信息
        iOrderCommunicateLogService.update((Wrappers.<OrderCommunicateLog>update().lambda()
                .set(OrderCommunicateLog::getStatus, CommunicateStatusCode.DIFFERENCE_TYPE.getCode())
                .eq(OrderCommunicateLog::getPurchaseOrderCode, purchaseOrderCode))
        );

        //用于计算项次的集合
        List<CommunicateNumVo> communicateNumVos = new ArrayList<>();

        for (int i = 0; i < sendOrderParam.size(); i++) {
            //用于计算的对象
            CommunicateNumVo communicateNumVo = new CommunicateNumVo();
            //查询产品信息
            OrderProductDetails orderProductDetails = iOrderProductDetailsService.getById(sendOrderParam.get(i).getOrderProductDetailsId());


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


            OrderCommunicateLogParam orderCommunicateLogParam = sendOrderParam.get(i);
            orderCommunicateLogParam.setType(OrderSendCode.ORDER_SEND_COMMUNICATE.getCode());


            //删除保存的日志
            OrderCommunicateLogSave orderCommunicateLogSave = new OrderCommunicateLogSave();
            orderCommunicateLogSave.setOrderProductDetailsId(orderProductDetails.getId());
            orderCommunicateLogSave.setPurchaseOrderCode(purchaseOrderCode);
            orderCommunicateLogSave.setCreatedBy(AuthUtil.getUser().getUserId());
            iOrderCommunicateLogSaveService.remove(Condition.getQueryWrapper(orderCommunicateLogSave));


            OrderCommunicateLog orderCommunicateLog = BeanUtil.copy(orderCommunicateLogParam, OrderCommunicateLog.class);

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
            orderCommunicateLog.setBatch(communicateNumVo.getCount() + 1);
            //orderCommunicateLog.setType(CommunicateCode.DIFFERENCE_TYPE.getCode());
            iOrderCommunicateLogService.save(orderCommunicateLog);
            communicateNumVo.setThisCount(communicateNumVo.getThisCount() + 1);
            communicateNumVos.add(communicateNumVo);
        }


        //修改订单状态 和确认信息
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_DIFFERENCE.getCode())
                .set(Order::getMsgType, OrderMsgTypeCode.UNREAD.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_UNCONFIRMED_CHANGE.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);

        //推送消息 TODO 未完成接口
        //发送变更事件
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);

        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(order.getId());
        ZyOrderSendCommunicateEvent zcOrderSendEvent = new ZyOrderSendCommunicateEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);


        //结束差异订单已发送
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.zy_confirmChangeOrder.getCode());
        handlerMsgParam.setBusinessId(order.getId());
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);


        return true;
    }

    @Override
    @Transactional
    public boolean sendOrderChange(List<OrderCommunicateLogParam> sendOrderParam) {
        //查询产品信息
        OrderProductDetails orderProductDetails = iOrderProductDetailsService.getById(sendOrderParam.get(0).getOrderProductDetailsId());
        //查询当前答交了多少次
        OrderCommunicateLog orderCommunicateLogQuery = new OrderCommunicateLog();
        orderCommunicateLogQuery.setOrderProductDetailsId(orderProductDetails.getId());
        int count = iOrderCommunicateLogService.count(Condition.getQueryWrapper(orderCommunicateLogQuery));
        //查询当前订单状态
        Order orderQuery = new Order();
        orderQuery.setPurchaseOrderCode(sendOrderParam.get(0).getPurchaseOrderCode());
        Order order = getOne(Condition.getQueryWrapper(orderQuery));
        String purchaseOrderCode = order.getPurchaseOrderCode();
        for (int i = 0; i < sendOrderParam.size(); i++) {
            OrderCommunicateLogParam orderCommunicateLogParam = sendOrderParam.get(i);
            orderCommunicateLogParam.setType(OrderSendCode.ORDER_SEND_CHANGE.getCode());
            if (orderCommunicateLogParam.getId() == null) {
                OrderCommunicateLog orderCommunicateLog = BeanUtil.copy(orderCommunicateLogParam, OrderCommunicateLog.class);

                if (orderCommunicateLog == null) {
                    throw new BizException(ResultCode.ENTITY_ERROR);
                }

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
                //设置产品信息
                orderCommunicateLog.setOrderProductDetailsId(orderProductDetails.getTakeOverDepartmentId());
                orderCommunicateLog.setItemCode(orderProductDetails.getItemCode());
                orderCommunicateLog.setPurchaseOrderCode(orderProductDetails.getPurchaseOrderCode());
                orderCommunicateLog.setType(CommunicateCode.DIFFERENCE_TYPE.getCode());
                orderCommunicateLog.setItem(orderProductDetails.getItemCode() + "-" + StringUtil.numberToLetter(count) + (i + 1));

                iOrderCommunicateLogService.save(orderCommunicateLog);

            }
        }
        //修改订单状态 和确认信息
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZC.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_DIFFERENCE.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_UNCONFIRMED_CHANGE.getMessage());
        iOrderOperationLogService.save(saveOperationLog);

        //推送消息 TODO 未完成接口
        //发送变更事件
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);

        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(order.getId());
        ZyOrderConfirmChangeEvent zcOrderSendEvent = new ZyOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);

        return true;
    }
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
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
                .set(Order::getMsgType,1)
                .set(Order::getMsgReadTime, LocalDateTime.now())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));



        OrderOperationLog saveOperationLog = new OrderOperationLog();
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //如果订单状态为变更中，那么要把变更状态也进行变更
        if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderOriginal.getOrderStatus()) && OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderOriginal.getChangeStatus())) {

            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());
            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZyOrderConfirmChangeEvent zyOrderSendEvent = new ZyOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent,orderOriginal.getPurchaseCode(),orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zyOrderSendEvent);
        } else if

            //如果是待答交状态下
        (OrderTypeCode.ORDER_WAIT.getCode().equals(orderOriginal.getOrderStatus())&&OrderChangeTypeCode.ORDER_NO_CHANGE.getCode().equals(orderOriginal.getChangeStatus())) {
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES.getMessage());

            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_NO_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));

            //推送消息 TODO 未完成接口
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZyOrderConfirmMsgEvent zyOrderSendEvent = new ZyOrderConfirmMsgEvent(this, loginInfo, saveOperationLogEvent,orderOriginal.getPurchaseCode(),orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zyOrderSendEvent);

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
        if (orderCommunicateLogList.size() == 0) {
            //查询当前订单 如果订单直接确认那么订单金额就是确认金额
            Order queryOrder = new Order();
            queryOrder.setPurchaseOrderCode(purchaseOrderCode);
            Order order = getOne(Condition.getQueryWrapper(queryOrder));
            orderConfirmPrice = order.getOrderPrice();

            //查询出订单下的产品
            OrderProductDetails orderProductDetailsQuery = new OrderProductDetails();
            orderProductDetailsQuery.setPurchaseOrderCode(purchaseOrderCode);
            List<OrderProductDetails> orderProductDetails = iOrderProductDetailsService.list(Condition.getQueryWrapper(orderProductDetailsQuery));

            for (int i = 0; i < orderProductDetails.size(); i++) {
                OrderProductDetails orderProductDetails1 = orderProductDetails.get(i);
                OrderCommunicateLog orderCommunicateLog = new OrderCommunicateLog();
                orderCommunicateLog.setType(CommunicateTypeCode.UPDATED.getCode());
                orderCommunicateLog.setStatus(CommunicateStatusCode.NOT_UPDATED.getCode());
                orderCommunicateLog.setDeliveryTimeType(CommunicateCode.NOT_UPDATED.getCode());
                orderCommunicateLog.setDeliveryTime(orderProductDetails1.getDeliveryTime());
                orderCommunicateLog.setPurchaseNumType(CommunicateCode.NOT_UPDATED.getCode());
                orderCommunicateLog.setPurchaseNum(orderProductDetails1.getPurchaseNum());
                orderCommunicateLog.setUnitPriceType(CommunicateCode.NOT_UPDATED.getCode());
                orderCommunicateLog.setUnitPrice(orderProductDetails1.getUnitPrice());
                //设置产品信息
                orderCommunicateLog.setOrderProductDetailsId(orderProductDetails1.getId());
                orderCommunicateLog.setItemCode(orderProductDetails1.getItemCode());
                orderCommunicateLog.setPurchaseOrderCode(purchaseOrderCode);
                orderCommunicateLog.setItem(orderProductDetails1.getItemCode() + "-" + StringUtil.numberToLetter(1) + "1");
                orderCommunicateLog.setBatch(1);
                iOrderCommunicateLogService.save(orderCommunicateLog);

            }
        }


        //调用协同服务接口查询是否开启订单网签开关
        CfgParam cfgParam = getParamByCode(CfgParamCode.ORDER_NET_SIGN_SWITCH).getData();
        SignContractCode signContractCode = null;
        if (StringUtils.pathEquals(cfgParam.getValue(), "true")) {
            //调用接口查看公司开关是否开启
            Order queryOrder = new Order();
            queryOrder.setPurchaseOrderCode(purchaseOrderCode);
            SupplierElectricSignatureConfig byCodeAndType = getByCodeAndType(getOne(Condition.getQueryWrapper(queryOrder)).getPurchaseCode(), ElectricSignatureConfigType.ORDER);
            //表示开启,如果为空默认不开启
            if (!ObjectUtils.isEmpty(byCodeAndType) && byCodeAndType.isEnabled()) {
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
        //数据回写到ERP
        //1.查询出原来的ERP数据
        OrderErp queryOrderErp = new OrderErp();
        queryOrderErp.setPurchaseOrderCode(purchaseOrderCode);
        OrderErp orderErp = iOrderErpService.getOne(Condition.getQueryWrapper(queryOrderErp));
        //1.查询出已经修改后的数据
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));
        OrderErp updateOrderErp = BeanUtil.copy(order, OrderErp.class);

        updateOrderErp.setId(orderErp.getId());
        iOrderErpService.updateById(updateOrderErp);

        OrderOperationLog saveOperationLog = new OrderOperationLog();
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        //如果订单状态为变更中，那么要把变更状态也进行变更
        if (OrderTypeCode.ORDER_WAIT.getCode().equals(orderOriginal.getOrderStatus()) && (OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderOriginal.getChangeStatus()) || OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode().equals(orderOriginal.getChangeStatus()))) {
            update(Wrappers.<Order>update().lambda()
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    //.set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
            //设置操作类型
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());

            //推送消息 TODO 未完成接口

            OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(order.getId());
            ZyOrderConfirmChangeEvent zcOrderSendEvent = new ZyOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);

//			//结束已发送差异订单已发送
            HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
            handlerMsgParam.setEventCode(EventCode.zc_sendDifferenceOrder.getCode());
            handlerMsgParam.setBusinessId(order.getId());
            handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
            informationServiceFeignClient.handlerInformation(handlerMsgParam);


        } else if (OrderTypeCode.ORDER_WAIT.getCode().equals(orderOriginal.getOrderStatus())) {
            //设置操作类型
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES.getMessage());
            //推送消息 TODO 未完成接口

            OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(order.getId());
            ZyOrderConfirmMsgEvent zcOrderSendEvent = new ZyOrderConfirmMsgEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
            defaultEventPublisher.publishEvent(zcOrderSendEvent);

            //接收订单已发送代办
            HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
            handlerMsgParam.setEventCode(EventCode.zc_SendOrder.getCode());
            handlerMsgParam.setBusinessId(order.getId());
            handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
            informationServiceFeignClient.handlerInformation(handlerMsgParam);

        }

        //保存操作记录

        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
    //    throw new ApiException(500, "测试异常");
      return true;
    }

    /**
     * 调用接口查看网签总开关是否开启
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
        orderCommunicateLogSave.setEntityJson(entityJson);
        orderCommunicateLogSave.setSysType(ConfirmSourceCode.ZY.getCode());
        iOrderCommunicateLogSaveService.save(orderCommunicateLogSave);

        return true;
    }

    @Override
    public boolean confirmChangeOrder(String purchaseOrderCode) {

        Order orderOriginal = new Order();
        orderOriginal.setPurchaseOrderCode(purchaseOrderCode);
        orderOriginal = getOne(Condition.getQueryWrapper(orderOriginal));
        getCurrentSupplierStatus(orderOriginal.getSupplierCode());
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);

        //获取当前登录信息
        BaseUserEntity currentUser = AuthUtil.getUser();


        if (OrderTypeCode.ORDER_CHANGE.getCode().equals(orderOriginal.getOrderStatus()) && OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode().equals(orderOriginal.getChangeStatus())) {
            //保存操作记录
            OrderOperationLog saveOperationLog = new OrderOperationLog();
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());
            saveOperationLog.setCreatedName(currentUser.getUserName());
            saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
            saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_CHANGE.getMessage());

            update(Wrappers.<Order>update().lambda()
                    .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                    .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                    .set(Order::getConfirmTime, new Date())
                    .set(Order::getConfirmUserId, currentUser.getUserId())
                    .set(Order::getConfirmUserName, currentUser.getUserName())
                    .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_YES_CHANGE.getCode())
                    .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
            iOrderOperationLogService.save(saveOperationLog);
            //发布事件
            OrderEvent saveOperationLogEvent = BeanUtil.copy(orderOriginal, OrderEvent.class);
            saveOperationLogEvent.setBusinessId(saveOperationLogEvent.getId());
            ZyOrderConfirmChangeEvent zyOrderSendEvent = new ZyOrderConfirmChangeEvent(this, loginInfo, saveOperationLogEvent,orderOriginal.getPurchaseCode(),orderOriginal.getPurchaseName());
            defaultEventPublisher.publishEvent(zyOrderSendEvent);
        }






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
        BaseUserEntity currentUser = AuthUtil.getUser();
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_STOP.getMessage());
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
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
        BaseUserEntity currentUser = AuthUtil.getUser();
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_START.getMessage());
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        return true;
    }

    @Override
    public boolean confirmDifferenceOrder(String purchaseOrderCode) {
        //获取当前登录信息
        BaseUserEntity currentUser = AuthUtil.getUser();
        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .set(Order::getConfirmTime, new Date())
                .set(Order::getConfirmUserId, currentUser.getUserId())
                .set(Order::getConfirmUserName, currentUser.getUserName())
                .set(Order::getChangeStatus, OrderTypeCode.ORDER_CONFIRM.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_YES_DIFFERENCE.getMessage());
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        return true;
    }


    @Override
    public boolean goBackOrder(String purchaseOrderCode) {
        //把订单改成退回状态
        //获取当前登录信息
        BaseUserEntity currentUser = AuthUtil.getUser();
        //修改订单状态
        update(Wrappers.<Order>update().lambda()
                .set(Order::getConfirmSource, ConfirmSourceCode.ZY.getCode())
                .set(Order::getOrderStatus, OrderTypeCode.ORDER_BACK.getCode())
//				.set(Order::getConfirmTime, new Date())
//				.set(Order::getConfirmUserId, currentUser.getUserId())
//				.set(Order::getConfirmUserName, currentUser.getUserName())
                .set(Order::getChangeStatus, OrderChangeTypeCode.ORDER_NO_CHANGE.getCode())
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        //保存操作记录
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_OG_BACK.getMessage());
        saveOperationLog.setCreatedName(currentUser.getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);

        //查询当前订单
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));

        //回写数据到ERP TODO 未完成接口
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);

        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(order.getId());

        GoBackOrderStartEvent zcOrderSendEvent = new GoBackOrderStartEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);

        //接收订单已发送代办
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.zc_SendOrder.getCode());
        handlerMsgParam.setBusinessId(order.getId());
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);


        return false;
    }

    @Override
    public MsgCountVo getMsgCount() {
        MsgCountVo msgCountVo = new MsgCountVo();
        //差异等处理数
        Order queryDifferenceMsgCount = new Order();
        queryDifferenceMsgCount.setOrderStatus(OrderTypeCode.ORDER_DIFFERENCE.getCode());
        queryDifferenceMsgCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        int differenceMsgCount = count(Condition.getQueryWrapper(queryDifferenceMsgCount));
        msgCountVo.setDifferenceMsgCount(differenceMsgCount);
        //待答交
        Order queryWaitingForAnswersCount = new Order();
        queryWaitingForAnswersCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryWaitingForAnswersCount.setOrderStatus(OrderTypeCode.ORDER_WAIT.getCode());
        int waitingForAnswersCount = count(Condition.getQueryWrapper(queryWaitingForAnswersCount));
        msgCountVo.setWaitingForAnswersCount(waitingForAnswersCount);
        //未确认变更
        Order queryUnconfirmedChangeCount = new Order();
        queryUnconfirmedChangeCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryUnconfirmedChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_UNCONFIRMED_CHANGE.getCode());
        int unconfirmedChangeCount = count(Condition.getQueryWrapper(queryUnconfirmedChangeCount));
        msgCountVo.setUnconfirmedChangeCount(unconfirmedChangeCount);
        //待确认变更
        Order queryWaitForUnconfirmedChangeCount = new Order();
        queryWaitForUnconfirmedChangeCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryWaitForUnconfirmedChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_WAIT_CHANGE.getCode());
        int waitForUnconfirmedChangeCount = count(Condition.getQueryWrapper(queryWaitForUnconfirmedChangeCount));
        msgCountVo.setWaitForUnconfirmedChangeCount(waitForUnconfirmedChangeCount);
        //退回设置
        Order queryReturnCount = new Order();
        queryReturnCount.setChangeStatus(OrderTypeCode.ORDER_BACK.getCode());
        queryReturnCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());

        int returnCount = count(Condition.getQueryWrapper(queryReturnCount));
        msgCountVo.setReturnCount(returnCount);
        //未完成订单
        LambdaQueryWrapper<Order> ne = Wrappers.<Order>query().lambda().ne(Order::getOrderStatus, OrderTypeCode.ORDER_COMPLETE.getCode()).eq(Order::getSupplierCode, AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        int notFinishedCount = count(ne);
        msgCountVo.setNotFinishedCount(notFinishedCount);
        //暂停执行
        Order queryStopCount = new Order();
        queryStopCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryStopCount.setOrderStatus(OrderTypeCode.ORDER_STOP.getCode());
        int stopCount = count(Condition.getQueryWrapper(queryStopCount));
        msgCountVo.setStopCount(stopCount);
        //确认变更
        Order queryConfirmChangeCount = new Order();
        queryConfirmChangeCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryConfirmChangeCount.setChangeStatus(OrderChangeTypeCode.ORDER_YES_CHANGE.getCode());
        int confirmChangeCount = count(Condition.getQueryWrapper(queryConfirmChangeCount));
        msgCountVo.setConfirmChangeCount(confirmChangeCount);
        //草稿
        Order queryDraftCount = new Order();
        queryDraftCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryDraftCount.setOrderStatus(OrderTypeCode.ORDER_DRAFT.getCode());
        int draftCount = count(Condition.getQueryWrapper(queryDraftCount));
        msgCountVo.setDraftCount(draftCount);
        //确认
        Order queryConfirmCount = new Order();
        queryConfirmCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryConfirmCount.setOrderStatus(OrderTypeCode.ORDER_CONFIRM.getCode());
        int confirmCount = count(Condition.getQueryWrapper(queryConfirmCount));
        msgCountVo.setConfirmCount(confirmCount);
        //完成
        Order queryCompleteCount = new Order();
        queryCompleteCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryCompleteCount.setOrderStatus(OrderTypeCode.ORDER_COMPLETE.getCode());
        int completeCount = count(Condition.getQueryWrapper(queryCompleteCount));
        msgCountVo.setCompleteCount(completeCount);
        //变更
        Order queryChangeCount = new Order();
        queryChangeCount.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? null : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        queryChangeCount.setOrderStatus(OrderTypeCode.ORDER_CHANGE.getCode());
        int changeCount = count(Condition.getQueryWrapper(queryChangeCount));
        msgCountVo.setChangeCount(changeCount);
        return msgCountVo;
    }

    @Override
    public Boolean sendRemindMsg(String purchaseOrderCode) {

        //保存操作日志
        OrderOperationLog saveOperationLog = new OrderOperationLog();
        saveOperationLog.setContent(OrderOperationCode.ORDER_OPERATION_MSG.getMessage());
        saveOperationLog.setCreatedName(AuthUtil.getUser().getUserName());
        saveOperationLog.setPurchaseOrderCode(purchaseOrderCode);
        iOrderOperationLogService.save(saveOperationLog);
        Order queryOrder = new Order();
        queryOrder.setPurchaseOrderCode(purchaseOrderCode);
        Order order = getOne(Condition.getQueryWrapper(queryOrder));

        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);

        OrderEvent saveOperationLogEvent = BeanUtil.copy(order, OrderEvent.class);
        saveOperationLogEvent.setBusinessId(order.getId());
        //发布事件
        ZyOrderSendRemindMsgEvent zcOrderSendEvent = new ZyOrderSendRemindMsgEvent(this, loginInfo, saveOperationLogEvent, saveOperationLogEvent.getSupplierCode(), saveOperationLogEvent.getSupplierName());
        defaultEventPublisher.publishEvent(zcOrderSendEvent);

        return false;
    }

    @Override
    public List<ExportOrderModel> getOrderListByCondition(QueryParam<OrderParam> queryParam) {
        queryParam.getParam().setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
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
                // //确认时间 时间+8
                // model.setConfirmTime(model.getConfirmTime()!=null?DateUtil.plusHours(model.getConfirmTime(),8L):null);
                //设置订单状态 code转换成name
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
}
