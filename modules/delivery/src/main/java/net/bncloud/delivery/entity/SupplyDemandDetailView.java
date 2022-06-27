package net.bncloud.delivery.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.delivery.excel.LocalDateStringConverter;

import java.time.LocalDate;


/**
 * @author ddh
 * @since 2022/4/8
 * @description 报表明细
 */
@Data
@Accessors(chain = true)
@ContentRowHeight(30)  //行高
@ColumnWidth(20)  //列宽
public class SupplyDemandDetailView{

    /**
     * 产品编码
     */
    @ExcelProperty(value = "产品编码")
    private String productCode;
    /**
     *产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String productName;
    /**
     *条码
     */
    @ExcelProperty(value = "条码")
    private String merchantCode;
    /**
     *日期
     */
    @ExcelProperty(value = "日期",converter = LocalDateStringConverter.class)
    private LocalDate date;
    /**
     *单据类型
     * 0订单，1送货计划，2送货单，3入库单  字典：supply_demand_bill_type
     */
    @ExcelProperty(value = "单据类型")
    private String billType;
    /**
     *单据号
     */
    @ExcelProperty(value = "单据号")
    private String billCode;
    /**
     *单据状态
     */
    @ExcelProperty(value = "单据状态")
    private String billStatus;
    /**
     *采购单号
     */
    @ExcelProperty(value = "采购单号")
    private String purchaseOrderCode;
    /**
     *供应商编码
     */
    @ExcelProperty(value = "供应商编码")
    private String supplierCode;
    /**
     *供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String supplierName;
    /**
     *数量
     */
    @ExcelProperty(value = "数量")
    private String quantity;

    /**
     * 剩余可发货数量（已确认未发货数量）
     */
    @ExcelIgnore
    @JsonIgnore
    private String remainingQuantity;
}