package net.bncloud.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.common.api.R;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.order.entity.Order;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.service.IOrderProductDetailsService;
import net.bncloud.order.service.IOrderService;
import net.bncloud.serivce.api.order.dto.*;
import net.bncloud.serivce.api.order.entity.OrderForDeliveryInfoDTO;
import net.bncloud.serivce.api.order.feign.OrderSych;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/zc/order/sych")
public class ErpOrderRemoteApi implements ZcOrderServiceFeignClient {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IOrderProductDetailsService orderProductDetailsService;




    @Override
    @PostMapping("/syncErp")
    public R syncErp(@RequestBody List<OrderErpDTO> orderErp) {
        log.info("syncErp order info:{}", JSON.toJSONString( orderErp) );
        iOrderService.syncErp(orderErp);
        return R.success("异步同步中，可能需要几分钟！");
    }

    /**
     * 关联供应商后同步订单
     * @param orderSych
     * @return
     */
    @Override
    @PostMapping("/syncOrder")
    public R syncOrder(@RequestBody OrderSych orderSych) {
        iOrderService.updateRelationOrder(orderSych);
        return R.success("同步成功");
    }

    @PostMapping("/addOrderProductDetailInventoryQuantity")
    @Override
    public R addOrderProductDetailInventoryQuantity(String orderProductDetailSourceId, BigDecimal receiptQuantity) {
        iOrderService.addOrderProductDetailInventoryQuantity(orderProductDetailSourceId,receiptQuantity);
        return R.success();
    }


    @GetMapping("/syncErpOrder")
    public R syncErpOrder() {
        iOrderService.syncErpOrder();
        return R.success("异步同步中，可能需要几分钟！");
    }

    @Override
    @PostMapping("getMrpDetailById")
    public R<OrderDetailDTO> getMrpDetailById(Long id) {
        OrderProductDetails detail = orderProductDetailsService.getById(id);
        OrderDetailDTO copy=buildReturnEntity(detail);
        return R.data(copy);
    }

    private OrderDetailDTO buildReturnEntity(OrderProductDetails detail) {
        OrderDetailDTO copy = BeanUtil.copyProperties(detail, OrderDetailDTO.class);
        copy.setPurchaseNum(detail.getPurchaseNum().toString());
        copy.setMarkDownNum(detail.getMarkDownNum().toString());
        copy.setUnitPrice(detail.getUnitPrice().toString());
        copy.setProductTotalPrice(detail.getProductTotalPrice().toString());
        copy.setTaxPrice(detail.getTaxPrice().toString());
        copy.setTaxRate(detail.getTaxRate().toString());
        copy.setTaxAmount(detail.getTaxAmount().toString());
        copy.setAllAmount(detail.getAllAmount().toString());
        copy.setInventoryQuantity(detail.getInventoryQuantity().toString());
        copy.setDeliveryAddress(detail.getDeliveryAddress());
        return copy;
    }

    @Override
    @PostMapping("updateMrpDetailById")
    public R<Boolean> updateBatchMrpDetailById(List<OrderDetailDTO> orderDetailDTOList) {
        /*List<Long> ids = orderDetailDTOList.stream().map(item -> item.getId()).collect(Collectors.toList());
        List<OrderProductDetails> orderProductDetails = orderProductDetailsService.getBaseMapper().selectBatchIds(ids);
        List<OrderProductDetails> orderProductDetailsArrayList = new ArrayList<OrderProductDetails>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            OrderProductDetails orderProductDetails = new OrderProductDetails();
            BeanUtil.copy(orderDetailDTO,orderProductDetails);
            orderProductDetailsArrayList.add(orderProductDetails);
        }*/
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            LambdaUpdateWrapper<OrderProductDetails> updateWrapper = Wrappers
                    .<OrderProductDetails>lambdaUpdate()
                    .set(OrderProductDetails::getRemainingQuantity, new BigDecimal(orderDetailDTO.getRemainingQuantity()))
                    .eq(OrderProductDetails::getId, orderDetailDTO.getId());
            orderProductDetailsService.update(updateWrapper);
        }

        return R.success();
    }

    @Override
    @PostMapping("listMrpDetailByIds")
    public R<List<OrderDetailDTO>> listMrpDetailByIds(List<Long> ids) {
        List<OrderProductDetails> orderProductDetails = orderProductDetailsService.getBaseMapper().selectBatchIds(ids);
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        orderProductDetails.forEach(item->{
            OrderDetailDTO copy=buildReturnEntity(item);
            orderDetailDTOS.add(copy);
        });

        return R.data(orderDetailDTOS);
    }

    @Override
    @PostMapping("getPurchaseOrderByCode")
    public R<OrderForDeliveryInfoDTO> getPurchaseOrderByCode(String purchaseOrderCode) {
        Order order = iOrderService.getOne(Wrappers
                .<Order>lambdaQuery()
                .eq(Order::getPurchaseOrderCode, purchaseOrderCode));
        OrderForDeliveryInfoDTO orderDeliveryDTO = new OrderForDeliveryInfoDTO();
        BeanUtils.copyProperties(order, orderDeliveryDTO);
        orderDeliveryDTO.setOrderPrice(order.getOrderPrice().toString());
        //orderDeliveryDTO.setOrderConfirmPrice(order.getOrderConfirmPrice().toString());
        return R.data(orderDeliveryDTO);
    }

    @Override
    @PostMapping("/getPurchaseOrderAndDetailsByDetailIds")
    public R<MrpOrderCreatePurchaseOrderBillDTO> getPurchaseOrderAndDetailsByDetailIds(List<Long> orderDetailIds) {
        //1 首先获取订单对应的所有明细
        List<OrderProductDetails> orderProductDetails = orderProductDetailsService.getBaseMapper().selectBatchIds(orderDetailIds);
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        orderProductDetails.forEach(item->{
            OrderDetailDTO detailDTO=buildReturnEntity(item);
            orderDetailDTOS.add(detailDTO);
        });

        //2 获取明细所属的订单主体
        Order order = iOrderService.getOne(Wrappers
                .<Order>lambdaQuery()
                .eq(Order::getPurchaseOrderCode, orderProductDetails.get(0).getPurchaseOrderCode()));
        OrderForDeliveryInfoDTO orderDeliveryDTO = new OrderForDeliveryInfoDTO();
        BeanUtils.copyProperties(order, orderDeliveryDTO);
        orderDeliveryDTO.setOrderPrice(order.getOrderPrice().toString());

        //设置返回对象
        MrpOrderCreatePurchaseOrderBillDTO returnDTO = new MrpOrderCreatePurchaseOrderBillDTO();
        returnDTO.setOrderDTO(orderDeliveryDTO);
        returnDTO.setDetailListDTO(orderDetailDTOS);
        return R.data(returnDTO);
    }

    @Override
    @PostMapping("/updateMrpOrderAndDetails")
    public R<Boolean> updateMrpOrderAndDetails(MrpOrderCreatePurchaseOrderBillReturnParamDTO dto) {
        //更新采购订单
        iOrderService.updateErpId(dto.getOrderId(), dto.getFId(), dto.getFNumber(), dto.getErpStatus());

        //更新采购订单明细
        List<MrpOrderCreatePurchaseOrderBillReturnParamDTO.returnDetail> returnDetailList = dto.getReturnDetailList();
        for (MrpOrderCreatePurchaseOrderBillReturnParamDTO.returnDetail returnDetail : returnDetailList) {
            orderProductDetailsService.update(Wrappers
                    .<OrderProductDetails>lambdaUpdate()
                    .set(OrderProductDetails::getErpId,returnDetail.getErpId())
                    .eq(OrderProductDetails::getId,returnDetail.getId()));
        }
        return R.data(true);
    }

}
