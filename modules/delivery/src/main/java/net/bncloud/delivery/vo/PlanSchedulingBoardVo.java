package net.bncloud.delivery.vo;

import lombok.Data;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @description 计划排程的看板视图
 * @since 2022/5/31
 */
@Data
public class PlanSchedulingBoardVo implements Serializable {

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 计划明细id
     */
    private Long planDetailId;
    /**
     * 计划编号
     */
    private String planNo;
    /**
     * 计划明细状态
     */
    private String detailStatus;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 条码
     */
    private String merchantCode;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 采购方编码
     */
    private String purchaseCode;
    /**
     * 采购方名称
     */
    private String purchaseName;
    /**
     * mrp计划数量
     */
    private Long mrpPlanQuantity;
    /**
     * 在途数量
     */
    private Long transitQuantity;
    /**
     * 净需求数
     */
    private Long netDemand;
    /**
     * 差异数
     */
    private Long varianceNumber;
    /**
     * 采购方备注
     */
    private String purchaseRemark;
    /**
     * 供应商备注
     */
    private String supplierRemark;

    /**
     * 来源类型：计划排程mrp,计划订单purchaseOrder
     */
    private String sourceType;

    /**
     * mrp运算编号/版号
     */
    private String mrpComputerNo;
    /**
     * 动态数据
     */
    private Map<String, DeliveryPlanDetailItem> dynamicDataMap;

    /**
     * 日期标题头
     */
    private List<String> dateTitle;

}
