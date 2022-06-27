package net.bncloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ddh
 * @version 1.0.0
 * @description 订单列表下的产品信息
 * @since 2022/3/11
 */
@Data
public class ProductUnderOrderListVo {
    /**
     * 产品明细id
     */
    private Long id;

    /**
     * 商家编码（条码）
     */
    @ApiModelProperty(value = "商家编码（条码）")
    private String merchantCode;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private BigDecimal purchaseNum;

    /**
     * 入库数量
     */
    private BigDecimal inventoryQuantity;



}
