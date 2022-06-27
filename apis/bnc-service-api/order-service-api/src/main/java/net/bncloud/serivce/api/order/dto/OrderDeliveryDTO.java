package net.bncloud.serivce.api.order.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.serivce.api.order.entity.OrderErpInfo;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDeliveryDTO extends OrderErpInfo implements Serializable {

    private Long id;

}
