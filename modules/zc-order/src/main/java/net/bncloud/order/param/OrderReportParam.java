package net.bncloud.order.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ddh
 * @version 1.0.0
 * @description  订单对账报表的请求参数
 * @since 2022/2/24
 */
@Data
public class OrderReportParam implements Serializable {

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 美尚 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 采购方
     */
    @ApiModelProperty(value = "采购方")
    private String purchaser;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String supplier;

    /**
     * 产品
     */
    @ApiModelProperty(value = "产品")
    private String product;

    /**
     * 采购日期-开始区间
     */
    @ApiModelProperty(value = "采购日期-开始区间")
    private LocalDateTime purchaseStartDate;

    /**
     * 采购日期-结束时间
     */
    @ApiModelProperty(value = "采购日期-结束时间")
    private LocalDateTime purchaseEndDate;

}
