package net.bncloud.serivce.api.order.feign;

import net.bncloud.common.api.R;

import net.bncloud.security.client.AuthorizedFeignClient;

import net.bncloud.serivce.api.order.dto.*;
import net.bncloud.serivce.api.order.entity.OrderForDeliveryInfoDTO;
import net.bncloud.serivce.api.order.feign.fallback.ZcOrderServiceOldFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ZcOrderServiceFeignClient
 * @Description: 订单服务feign
 * @Author Administrator
 * @Date 2021/4/12
 * @Version V1.0
 **/
@AuthorizedFeignClient(name = "zc-order",contextId = "zcSychOrderClient" ,fallbackFactory = ZcOrderServiceOldFeignClientFallbackFactory.class)
public interface ZcOrderServiceFeignClient {

    /**
     * 同步ERP
     * @param orderErp
     * @return
     */
    @PostMapping("/zc/order/sych/syncErp")
    R syncErp(@RequestBody List<OrderErpDTO> orderErp);

    /**
     * 根据供应商
     * @param orderSych
     * @return
     */
    @PostMapping("/zc/order/sych/syncOrder")
    R syncOrder(@RequestBody OrderSych orderSych);

    /**
     * 添加订单产品明细入库数量
     * @param orderProductDetailSourceId
     * @param receiptQuantity
     * @return
     */
    @PostMapping("/zc/order/sych/addOrderProductDetailInventoryQuantity")
    R addOrderProductDetailInventoryQuantity(@RequestParam(name = "orderProductDetailSourceId")String orderProductDetailSourceId, @RequestParam("receiptQuantity")BigDecimal receiptQuantity);

    /**
     * mrp通过id查询对应订单下的明细
     * @param id
     * @return
     */
    @PostMapping("/zc/order/sych/getMrpDetailById")
    R<OrderDetailDTO> getMrpDetailById(@RequestParam(name = "id")Long id);

    /**
     * mrp通过ids'批量查询对应订单下的明细
     * @param ids
     * @return
     */
    @PostMapping("/zc/order/sych/listMrpDetailByIds")
    R<List<OrderDetailDTO>> listMrpDetailByIds(@RequestParam(name = "ids")List<Long> ids);

    /**
     * mrp通过id查询对应订单下的明细
     * @return
     */
    @PostMapping("/zc/order/sych/updateMrpDetailById")
    R<Boolean> updateBatchMrpDetailById(@RequestBody List<OrderDetailDTO> orderDetailDTOList);

    /**
     * mrp通过订单号获取对应订单实体
     * @return
     */
    @PostMapping("/zc/order/sych/getPurchaseOrderByCode")
    R<OrderForDeliveryInfoDTO> getPurchaseOrderByCode(@RequestParam(name = "purchaseOrderCode")String purchaseOrderCode);

    /**
     * mrp通过明细id获取明细和明细所属的订单
     * @return
     */
    @PostMapping("/zc/order/sych/getPurchaseOrderAndDetailsByDetailIds")
    R<MrpOrderCreatePurchaseOrderBillDTO> getPurchaseOrderAndDetailsByDetailIds(@RequestParam(name = "orderDetailIds")List<Long> orderDetailIds);

    /**
     * mrp收料通知单创建后回写采购订单方法
     * @return
     */
    @PostMapping("/zc/order/sych/updateMrpOrderAndDetails")
    R<Boolean> updateMrpOrderAndDetails(@RequestBody MrpOrderCreatePurchaseOrderBillReturnParamDTO dto);
}
