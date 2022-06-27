package net.bncloud.quotation.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Toby
 */
@Getter
@Setter
@EqualsAndHashCode
@HeadRowHeight(20)
@ColumnWidth(25)
@ContentRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 11)
public class QuotationBaseSaleData {

    /**
     * 询价单号
     */
    @ExcelProperty("编号")
    @ApiModelProperty(value = "询价单号")
    private String quotationNo;

    /**
     * 询价标题
     */
    @ExcelProperty("标题")
    @ApiModelProperty(value = "询价标题")
    private String title;




    /**
     * 询价类别
     */
    @ExcelIgnore
    private String quotationType;

    /**
     * 询价类别
     */
    @ExcelProperty("询价类别")
    private String quotationTypeName;




    @ExcelProperty("发布时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 报价截止时间
     */
    @ExcelProperty("报价截止时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "报价截止时间")
    private Date cutOffTime;


    @ExcelProperty("发布人")
    @ApiModelProperty(value = "发布人")
    private String publisher;


    /**
     * 询价单状态
     */
    @ExcelIgnore
    @ApiModelProperty(value = "询价单状态")
    private String quotationStatus;

    /**
     * 询价单状态
     */
    @ExcelProperty("询价单状态")
    @ApiModelProperty(value = "询价单状态")
    private String quotationStatusName;


}
