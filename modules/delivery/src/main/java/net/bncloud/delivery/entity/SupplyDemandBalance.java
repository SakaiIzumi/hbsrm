package net.bncloud.delivery.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.delivery.excel.LocalDateStringConverter;

import java.time.LocalDate;


/**
 * @author ddh
 * @description 供需平衡报表
 * @since 2022/4/8
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(30)  //行高
@ColumnWidth(20)  //列宽
@TableName("t_supply_demand_balance")
public class SupplyDemandBalance {

    /**
     * 主键id
     */
    @ExcelIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 序号
     */
    //@ExcelProperty(value = "序号")
    @ExcelIgnore
    private Long itemNo;

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
     * 条码（商家编码）
     */
    @ExcelProperty(value = "商家编码")
    private String merchantCode;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期", converter = LocalDateStringConverter.class)
    private LocalDate date;

    /**
     * 订单需求数量
     */
    @ExcelProperty(value = "订单需求数量")
    private String orderDemandQuantity;

    /**
     * 计划发货数量
     */
    @ExcelProperty(value = "计划发货数量")
    private String planDeliveryQuantity;

    /**
     * 已确认未发货数量
     */
    //@ExcelProperty(value = "已确认未发货数量")
    @ExcelIgnore
    private String confirmedUndeliveryQuantity;

    /**
     * 当前日期+3天的已确认未发货数量的展示值
     */
    @ExcelProperty(value = "已确认未发货数量")
    private String confirmedUndeliveryQuantityShow;

    /**
     * 预计到货数量
     */
    @ExcelProperty(value = "预计到货数量")
    private String estimatedArrivalQuantity;

    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private String receiptQuantity;

    /**
     * 供需结余
     */
    @ExcelProperty(value = "供需结余")
    private String balanceQuantity;

    /**
     * 未完成订单
     */
    @ExcelProperty(value = "未完成订单")
    private String outstandingOrdersQuantity;

}