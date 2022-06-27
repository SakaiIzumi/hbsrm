package net.bncloud.order.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderAsPlanParam implements Serializable {
    /**
     * 采购订单
     * */
    private String purchaseOrderCode;
    /**
     * 订单编号
     * */
    private String purchaseOrderNo;
    /**
     * 产品名称
     * */
    private String productName;
    /**
     * 产品编码
     * */
    private String purchaseCode;
    /**
     * 供应商编码   过滤当前登录的供应商可以查询的订单计划
     * */
    private String supplierCode;
    /**
     * 送货日期
     * */
    private String deliveryTimeStart;
    /**
     * 送货日期
     * */
    private String deliveryTimeEnd;
    /**
     * 入库仓
     * */
    private String warehouse;
    /**
     * 过滤当前登录的供应商可以查询的订单计划
     * */
//    private String supplierCode;



}
