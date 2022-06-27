package net.bncloud.service.api.delivery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Toby
 */
@Data
public class DeliveryDetailUpdateDTO implements Serializable {

    private static final long serialVersionUID = -1;


    /**
     * 计划明细项次 ID，t_delivery_plan_detail_item.source_id
     * (更新收货数量的条件字段待产品确认，暂以此字段作为更新条件)
     */
//    private String planDetailItemSourceId;


    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量")
    private BigDecimal receiptQuantity;

//    /**
//     * srmId
//     */
//    @ApiModelProperty(value = "srmId")
//    private Long srmId;

    /**
     * erpId
     */
    @ApiModelProperty(value = "erpId")
    private Long erpId;

    /**
     * 入库日期
     */
    private Date warehouseDate;

    /**
     * 入库单号
     */
    @ApiModelProperty(value = "入库单号")
    private String receiptNo;

}
