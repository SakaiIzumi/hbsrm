package net.bncloud.order.vo;


import lombok.Data;

@Data
public class ColorChange {

    //含税金额变更
    private Boolean  taxPrice;

    //税率
    private Boolean  taxRate;

    //采购数量
    private Boolean  purchaseNum;

    //交互日期
    private Boolean  deliveryTime;






}
