package net.bncloud.serivce.api.order.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.serivce.api.order.dto.*;
import net.bncloud.serivce.api.order.entity.OrderForDeliveryInfoDTO;
import net.bncloud.serivce.api.order.feign.OrderSych;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName FileServiceFeignClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/4/7
 * @Version V1.0
 **/
@Slf4j
@Component
public  class ZcOrderServiceOldFeignClientFallbackFactory implements FallbackFactory<ZcOrderServiceFeignClient> {

    @Override
    public ZcOrderServiceFeignClient create(Throwable throwable) {
        log.error("ZcOrderServiceFeignClient feign error, msg:{} 。",throwable.getMessage() , throwable );
       return new ZcOrderServiceFeignClient() {
           @Override
           public R syncErp(List<OrderErpDTO> orderErp) {
               return R.fail("订单服务调用失败!");
           }

           @Override
           public R syncOrder(OrderSych orderSych) {
               return R.fail("订单服务调用失败!");
           }

           @Override
           public R addOrderProductDetailInventoryQuantity(String orderProductDetailSourceId, BigDecimal receiptQuantity) {
               return R.fail("订单服务调用失败!");
           }

           @Override
           public R<OrderDetailDTO> getMrpDetailById(Long id) {
               return R.fail("mrp通过id远程调用获取采购订单明细失败!");

           }

           @Override
           public R<List<OrderDetailDTO>> listMrpDetailByIds(List<Long> ids) {
               return R.fail("mrp通过ids远程批量获取采购订单明细失败!");
           }

           @Override
           public R<Boolean> updateBatchMrpDetailById(List<OrderDetailDTO> orderDetailDTOList) {
               return R.fail("mrp通过id远程更新采购订单明细失败");
           }

           @Override
           public R<OrderForDeliveryInfoDTO> getPurchaseOrderByCode(String purchaseOrderCode) {
               return R.fail("mrp通过采购订单号远程获取采购订单失败");
           }

           @Override
           public R<MrpOrderCreatePurchaseOrderBillDTO> getPurchaseOrderAndDetailsByDetailIds(List<Long> orderDetailIds) {
               return R.remoteFail();
           }

           @Override
           public R<Boolean> updateMrpOrderAndDetails(MrpOrderCreatePurchaseOrderBillReturnParamDTO dto) {
               return R.remoteFail();
           }

       };
    }
}
