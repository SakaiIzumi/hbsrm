package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MrpDeliveryNoteTypeEnum {
    NORMAL_DELIVERY("0","普通送货单"),
    MRP_DELIVERY("1","按订单送货的送货单"),
    ;

    private String code;
    private String name;
}
