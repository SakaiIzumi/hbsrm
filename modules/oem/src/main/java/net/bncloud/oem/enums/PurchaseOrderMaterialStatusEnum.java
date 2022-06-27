package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @version 1.0.0
 * @description 订单收货
 * @since 2022/4/24
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderMaterialStatusEnum {
//    收货状态:1待收货，2部分收货，3已收货
    GOODS_TO_BE_RECEIVE("1","待收货"),
    PARTIAL_RECEIPT("2","部分收货"),
    COMPLETE_RECEIVE_GOODS("3","已收货"),
    //HAVE_CLOSE_CASE("4","已结案"),
    ;
    private final String code;
    private final String name;
}
