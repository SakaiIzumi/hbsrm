package net.bncloud.order.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import net.bncloud.order.excel.DateConverter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ddh
 * @version 1.0.0
 * @description 导出订单模型
 * @since 2022/3/9
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(30)
@ColumnWidth(20)
public class ExportOrderModel {

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 供应商编码
     */
    @ExcelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 采购方编码
     */
    @ExcelProperty(value = "采购方编码")
    private String purchaseCode;
    /**
     * 采购方名称
     */
    @ExcelProperty(value = "采购方名称")
    private String purchaseName;

    /**
     * 采购单号
     */
    @ExcelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 采购员名称
     */
    @ExcelProperty(value = "采购员名称")
    private String purchaseUserName;
    /**
     * 采购日期
     */
    @ExcelProperty(value = "采购日期")
    @DateTimeFormat(value = DateUtil.PATTERN_DATE)
    private Date purchaseTime;

    /**
     * 确认时间
     */
    @ExcelProperty(value = "确认时间",converter = DateConverter.class)
    @DateTimeFormat(value = DateUtil.PATTERN_DATETIME)
    private Date confirmTime;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    @NumberFormat
    private BigDecimal orderPrice;

    /**
     * 变更状态 ：
     * 1无变更
     * 2已确认并更
     * 3未确认变更
     * 4待确认变更
     */
    @ExcelProperty(value = "变更状态")
    private String changeStatus;

    /**
     * 订单状态 ：
     * 1草稿
     * 2待答交
     * 3已留置
     * 4答交差异
     * 5退回
     * 6变更中
     * 7已确认
     * 8已完成
     */
    @ExcelProperty(value = "订单状态")
    private String orderStatus;

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
     * 商家编码（条码）
     */
    @ExcelProperty(value = "商家编码")
    private String merchantCode;

    /**
     * 采购数量
     */
    @ExcelProperty(value = "采购数量")
    @NumberFormat
    private BigDecimal purchaseNum;
    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    @NumberFormat
    private BigDecimal inventoryQuantity;
}
