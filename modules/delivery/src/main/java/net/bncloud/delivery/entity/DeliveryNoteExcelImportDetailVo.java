package net.bncloud.delivery.entity;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 送货单导入明细的接收对象  批次、实际送货数量为必填字段；产品编码、条码必须填写一个；
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/3/8
 */
@Data
@ContentRowHeight(30)  //行高
@ColumnWidth(20)  //列宽
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryNoteExcelImportDetailVo  {
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
     * 物料分类编码
     */
    @NotNull(message = "物料分类编码不能为空！")
    @ExcelProperty("物料分类(编码)")
    private String materialType;

    /**
     *批次
     */
    @ExcelProperty(value = "批次")
    private String itemNo;


    /**
     *实际送货数量
     */
    @ExcelProperty(value = "实际送货数量")
    @NumberFormat
    private Long realDeliveryQuantity;


    /**
     * 订单类型编码
     */
    @NotNull(message = "订单类型编码不能为空！")
    @ExcelProperty("订单类型(编码)")
    private String orderType;

    /**
     * 入库仓编码
     */
    @NotNull(message = "入库仓编码不能为空！")
    @ExcelProperty("入库仓(编码)")
    private String warehouse;

    /**
     *备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    public boolean productCodeIsNotNull(){
        return StrUtil.isNotBlank( this.getProductCode() );
    }

}
