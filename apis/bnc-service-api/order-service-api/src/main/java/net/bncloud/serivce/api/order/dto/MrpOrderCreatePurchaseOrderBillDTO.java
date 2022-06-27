package net.bncloud.serivce.api.order.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.serivce.api.order.entity.OrderForDeliveryInfoDTO;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MrpOrderCreatePurchaseOrderBillDTO implements Serializable {
    /**
     * 订单主体信息
     */
    private OrderForDeliveryInfoDTO orderDTO;

    /**
     * 订单明细信息
     */
    private List<OrderDetailDTO> detailListDTO;

}
