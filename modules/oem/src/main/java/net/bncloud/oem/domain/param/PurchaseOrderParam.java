package net.bncloud.oem.domain.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Data
public class PurchaseOrderParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 采购方
     */
    private String purchaser;

    /**
     * 供应方
     */
    private String supplier;

    /**
     * oem供应方
     */
    private String oemSupplier;

    /**
     * 采购单号
     */
    private String purchaseOrderCode;

    /**
     * 物料
     */
    private String material;

    /**
     * 收货状态：0待确认，1已确认，2已退回
     */
    private String receivingStatus;

    /**
     * 收货状态:1待收货，2部分收货，3收货完成，4已结案
     */
    private String takeOverStatus;

    /**
     * 地址编码集合
     */
    private List<String> addressCodeList;
}
