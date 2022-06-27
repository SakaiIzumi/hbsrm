package net.bncloud.delivery.param;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddh
 * @version 1.0.0
 * @description 按计划送货的请求参数
 * @since 2022/1/21
 */
@Data
public class DeliveryAsPlanParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    @Length(min = 0,max = 255)
    private String planNo;
    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    @Length(min = 0,max = 255)
    private String billNo;



    /**
     *订单类型 ERP获取类型
     */
    @ApiModelProperty(value = "订单类型 ERP获取类型")
    @Length(min = 0, max = 255)
    private String orderType;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @Length(min = 0,max = 255)
    private String purchaseOrderNo;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @Length(min = 0,max = 255)
    private String purchaseOrderCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    @Length(min = 0,max = 255)
    private String productName;
    /**
     * 采购方
     */
    @ApiModelProperty(value = "采购方")
    @Length(min = 0,max = 255)
    private String purchaseCode;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String deliveryTimeStart;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String deliveryTimeEnd;

    /**
     * pdi的Id
     */
    @ApiModelProperty(value = "pdi的Id")
    private Long id;

    /**
     * 商家编码（条码）
     */
    private String merchantCode;
}
