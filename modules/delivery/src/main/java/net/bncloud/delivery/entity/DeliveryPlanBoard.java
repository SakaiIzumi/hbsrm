package net.bncloud.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 计划看板
 * @since 2022/1/18
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "计划看板")
public class DeliveryPlanBoard implements Serializable {

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
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    @JsonIgnore
    private String deliveryQuantity;
    /**
     * 计划送货总数
     */
    @ApiModelProperty(value = "计划送货总数")
    private Long totalPlanDeliveryNum;


}
