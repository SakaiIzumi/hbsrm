package net.bncloud.oem.service.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * desc: 甲供收货物料订单
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@Data
public class OemPurchaseOrderVo implements Serializable {
    private static final long serialVersionUID = 6594324229061561721L;

    /**
     * ERPID
     */
    private String sourceErpId;

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
     * 订单编号（美尚）
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
    private LocalDateTime confirmDate;

    /**
     * 采购时间
     */
    @ApiModelProperty(value="采购时间")
    private LocalDateTime purchaseDate;

    /**
     * oem供应商地址
     */
    private String oemSupplierAddress;

    /**
     * 单据类型
     */
    private String orderType;

    /**
     * 订单明细物料信息
     */
    private List<PurchaseOrderMaterial> purchaseOrderMaterialList;

    @Data
    public static class PurchaseOrderMaterial implements Serializable {

        private static final long serialVersionUID = -1920982280689773477L;

        /**
         * ERPID
         */
        private String sourceErpId;

        /**
         * 采购订单id
         */
        @ApiModelProperty(value="采购订单id")
        private Long purchaseOrderId;

        /**
         * 物料编码
         */
        @ApiModelProperty(value="物料编码")
        private String materialCode;

        /**
         * 物料名称
         */
        @ApiModelProperty(value="物料名称")
        private String materialName;

        /**
         * 交货日期
         */
        @ApiModelProperty(value="交货日期")
        private LocalDateTime deliveryDate;

        /**
         * 交货方式
         */
        @ApiModelProperty(value="交货方式")
        private String deliveryType;

        /**
         * 答交状态
         */
        @ApiModelProperty(value="答交状态")
        private Integer answerStatus;

        /**
         * 采购数量
         */
        @ApiModelProperty(value="采购数量")
        private String purchaseQuantity;

        /**
         * 条码
         */
        @ApiModelProperty(value="条码")
        private String barCode;

        /**
         * 货主类型 BD_OwnerOrg、BD_Supplier、BD_Customer
         */
        @ApiModelProperty(value="货主类型")
        private String ownerTypeid;

        /**
         * 入库仓
         */
        @ApiModelProperty(value="入库仓")
        private String warehouse;

        /**
         * 含税单价
         */
        @ApiModelProperty(value="含税单价")
        private String taxPrice;

        /**
         * 收货状态:1待收货，2部分收货，3收货完成，4已结案
         */
        @ApiModelProperty(value="收货状态:1待收货，2部分收货，3收货完成，4已结案")
        private String takeOverStatus;



    }

}
