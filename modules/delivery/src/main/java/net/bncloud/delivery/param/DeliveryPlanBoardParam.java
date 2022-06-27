package net.bncloud.delivery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划看板的请求参数
 * @since 2022/1/18
 */
@Data
public class DeliveryPlanBoardParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 产品
     */
    @ApiModelProperty(value = "产品")
    @Length(min = 0, max = 255)
    private String productCondition;

    /**
     * 供应方
     */
    @ApiModelProperty(value = "供应方")
    @Length(min = 0, max = 255)
    private String supplierCondition;

    /**
     * 采购方
     */
    @ApiModelProperty(value = "采购方")
    @Length(min = 0, max = 255)
    private String customerCondition;

    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    @Length(min = 0, max = 255)
    private String planNo;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    @Length(min = 0, max = 255)
    private String billNo;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    @Length(min = 0, max = 255)
    private String deliveryAddress;

    /**
     * 看板跳转带过来的日期
     */
    @ApiModelProperty(value = "日期")
    @NotNull(message = "看板跳转带过来的日期不能为空")
    @Length(min = 0, max = 255)
    private String date;


    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @Length(min = 0, max = 255)
    private String purchaseOrderNo;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    @Length(min = 0, max = 255)
    private String orderType;

}
