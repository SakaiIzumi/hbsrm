package net.bncloud.delivery.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.bncloud.common.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddh
 * @description
 * @since 2022/5/19
 */
@Data
@ContentRowHeight(30)  //行高
@ColumnWidth(20)  //列宽
public class ImportFactoryVacationVo implements Serializable {

    /**
     * 采购方编码
     */
    @ExcelProperty("采购方编码")
    private String number;
    /**
     * 日期
     */
    @ExcelProperty("日期")
    @DateTimeFormat
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date vacationDate;
    /**
     * 假期类型
     */
    @ExcelProperty("假期类型")
    private String vacationType;
    /**
     * 采购方名称
     */
    @ExcelProperty("采购方名称")
    private String factoryName;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;


}
