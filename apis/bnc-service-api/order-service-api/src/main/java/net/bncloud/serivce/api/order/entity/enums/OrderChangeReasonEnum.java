package net.bncloud.serivce.api.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName SupplierRelevanceStatusEnum
 * @Description: 供应商状态枚举类
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum OrderChangeReasonEnum {
    TAXPRICE("taxPrice","含税单价变更","taxPriceChange"),
    DELIVERYTIME("deliveryTime","到货日期变更","deliveryTimeChange"),
    PURCHASENUM("purchaseNum","采购数量变更","purchaseNumChange"),
    TAXRATE("taxRate","税率变更","taxRateChange")
    ;
    private String key;
    private String reason;
    private String code;



}
