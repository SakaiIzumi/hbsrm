package net.bncloud.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.web.jackson.AmountSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * desc: 订单excel导出Vo
 *
 * @author Rao
 * @Date 2022/03/08
 **/
@Setter
@Getter
@EqualsAndHashCode
public class OrderExcelExportVo implements Serializable {
    private static final long serialVersionUID = -1170181572128701014L;

    @ExcelProperty(value = "查看情况")
    private String msgType;

    @ExcelProperty(value = "供应商编码")
    private String supplierCode;

    @ExcelProperty(value = "供应商名称")
    private String supplierName;

    @ExcelProperty(value = "采购方编码")
    private String purchaseCode;

    @ExcelProperty(value = "采购方名称")
    private String purchaseName;

    @ExcelProperty(value = "采购单号")
    private String purchaseOrderCode;

    @ExcelProperty(value = "采购员名称")
    private String purchaseUserName;

    @ExcelProperty(value = "采购日期")
    private Date purchaseTime;

    @ExcelProperty(value = "确认时间")
    private Date confirmTime;

    @ExcelProperty(value = "订单金额")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal orderPrice;

    /**
     * 变更状态
     * 1）无变更
     * 2）已确认并更
     * 3）未确认变更
     * 4）待确认变更
     */
    @ExcelProperty(value = "变更状态")
    private String changeStatus;

    /**
     * 订单状态
     * 1）草稿
     * 2）待答交
     * 3）已留置
     * 4）答交差异
     * 5）退回
     * 6）变更中
     * 7）已确认
     * 8）已完成
     */
    @ExcelProperty(value = "订单状态")
    private String orderStatus;

    @ExcelProperty(value = "产品编码")
    private String productCode;

    @ExcelProperty(value = "产品名称")
    private String productName;

    @ExcelProperty(value = "商家编码")
    private String merchantCode;

    @ExcelProperty(value = "采购数量")
    private BigDecimal purchaseNum;

    @ExcelProperty("入库数量")
    private BigDecimal inventoryQuantity;

}
