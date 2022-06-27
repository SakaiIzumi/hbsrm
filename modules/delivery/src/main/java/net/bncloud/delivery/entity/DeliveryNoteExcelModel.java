package net.bncloud.delivery.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/3/10
 */
@Data
@Accessors(chain = true)
@ColumnWidth(20)
@ContentRowHeight(30)
public class DeliveryNoteExcelModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商编码
     */
    @ExcelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 采购方编码
     */
    @ExcelProperty(value = "采购方编码")
    private String customerCode;

    /**
     * 采购方名称
     */
    @ExcelProperty(value = "采购方名称")
    private String customerName;

    /**
     * 送货单号
     */
    @ExcelProperty(value = "送货单号")
    private String deliveryNo;


    /**
     * 收料通知单号
     */
    @ExcelProperty(value = "收料通知单号")
    private String fNumber;

    /**
     * 预计到厂时间
     */
    @ExcelProperty(value = "预计到厂时间")
    @DateTimeFormat(value = DateUtil.PATTERN_DATE)
    private Date estimatedTime;

    /**
     * 签收时间
     */
    @ExcelProperty(value = "签收时间")
    @DateTimeFormat(value = DateUtil.PATTERN_DATE)
    private Date signingTime;

    /**
     * 运输方式 由字典编码delivery_transport_method定义
     */
    @ExcelProperty(value = "运输方式")
    private String transportMethod;

    /**
     * 送货方式 由字典编码delivery_method定义
     */
    @ExcelProperty(value = "送货方式")
    private String deliveryMethod;

    /**
     * 送货状态 由字典delivery_status_code定义
     */
    @ExcelProperty(value = "送货状态")
    private String deliveryStatusCode;

    /**
     * ERP签收状态
     */
    @ExcelProperty(value = "ERP签收状态")
    private String erpSigningStatus;

    /**
     * 产品编码
     */
    @ExcelProperty(value = "产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String productName;

    /**
     * (商家编码)条码
     */
    @ExcelProperty(value = "商家编码")
    private String barCode;

    /**
     * 实际送货数量（交货数量）
     */
    @ExcelProperty(value = "交货数量")
    private BigDecimal realDeliveryQuantity;


    /**
     * 计划单位/送货单位
     * delivery_unit_name
     */
    @ExcelProperty(value = "单位")
    private String deliveryUnitName;

    /**
     * 入货仓
     */
    @ExcelProperty(value = "仓库")
    private String warehouse;

    /**
     * 是否有COA附件：Y有，N没有
     */
    @ExcelProperty(value = "是否有COA附件")
    private String attachment;


}
