package net.bncloud.delivery.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description 计划排程看板的请求参数
 * @since 2022/5/31
 */
@Data
public class PlanSchedulingBoardParam implements Serializable {

    /**
     * 计划明细状态：1待发布、2待确认、3差异待确认、4已确认
     */
    private String status;
    /**
     * 供应商
     */
    private String supplier;
    ///**
    // * 收货工厂
    // */
    //private String receiptFactory;
    /**
     * 产品
     */
    private String product;
    /**
     * 采购方
     */
    private String purchase;

    private List<String> stateList;


}
