package net.bncloud.oem.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ddh
 * @description 导入导出地址用
 * @since 2022/4/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ContentRowHeight(30)  //行高
@ColumnWidth(25)  //列宽
public class AddressModule {

    /**
     * 收货地址编码
     */
    @ExcelProperty(value = "收货地址编码")
    private String addressCode;

    /**
     * 收货地址
     */
    @ExcelProperty(value = "收货地址")
    private String addressName;

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
}
