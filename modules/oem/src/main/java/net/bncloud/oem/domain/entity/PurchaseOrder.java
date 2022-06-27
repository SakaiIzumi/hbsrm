package net.bncloud.oem.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

 
/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "t_oem_purchase_order")
public class PurchaseOrder extends BaseEntity {


    private static final long serialVersionUID = 5415540040122744904L;
    /**
     * 地址编码
     */
    private String receivingAddressCode;
    /**
    * 采购单号
    */
    @ApiModelProperty(value="采购单号（美尚）")
    private String purchaseOrderCode;

    /**
    * 订单编号
    */
    @ApiModelProperty(value="订单编号")
    private String orderNo;

    /**
    * 采购方编码
    */
    @ApiModelProperty(value="采购方编码")
    private String purchaseCode;

    /**
    * 采购方名称
    */
    @ApiModelProperty(value="采购方名称")
    private String purchaseName;

    /**
    * 供应方编码
    */
    @ApiModelProperty(value="供应方编码")
    private String supplierCode;

    /**
    * 供应方名称
    */
    @ApiModelProperty(value="供应方名称")
    private String supplierName;

    /**
    * oem供应商编码
    */
    @ApiModelProperty(value="oem供应商编码")
    private String oemSupplierCode;

    /**
    * oem供应商名称
    */
    @ApiModelProperty(value="oem供应商名称")
    private String oemSupplierName;

    /**
    * 确认时间
    */
    @ApiModelProperty(value="确认时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil. PATTERN_DATE)
    private LocalDateTime confirmDate;

    /**
    * 采购时间
    */
    @ApiModelProperty(value="采购时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDateTime purchaseDate;

    /**
     * 地址id
     */
   /* @ApiModelProperty(value="地址id")
    private Long receivingAddressId;*/


    /**
     * ERPID
     */
    private String sourceErpId;

    /**
     * 回写erp后的fid
     */
    private String fid;

    /**
     * 单据类型
     */
    private String orderType;

    /**
     * 收货状态:1待收货，2部分收货，3已收货
     * 此状态根据物料(第二层)来判断状态
     */
    private String takeOverStatus;


}