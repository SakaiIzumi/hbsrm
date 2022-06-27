package net.bncloud.quotation.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.util.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
public class QuotationBaseData {

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
     * 询价范围，open 公开，specified 指定
     */
    @ExcelIgnore
    @ApiModelProperty(value = "询价范围，open 公开，specified 指定")
    private String quotationScope;

    /**
     * 询价范围名称，open 公开，specified 指定
     */
    @ExcelProperty("询价范围")
    @ApiModelProperty(value = "询价范围，open 公开，specified 指定")
    private String quotationScopeName;

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

    /**
     * 报价截止时间
     */
    @ExcelProperty("报价截止时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "报价截止时间")
    private Date cutOffTime;


    @ExcelProperty("发布时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ExcelProperty("发布人")
    @ApiModelProperty(value = "发布人")
    private String publisher;

    /**
     * 响应的供应商数量
     */
    @ExcelProperty("收到邀约响应")
    @ApiModelProperty(value = "响应的供应商数量")
    private Integer responseNum;


    /**
     * 报价的供应商数量
     */
    @ExcelProperty("收到邀约报价")
    @ApiModelProperty(value = "报价的供应商数量")
    private Integer biddingNum;

    /**
     * 供应商数量
     */
    @ExcelProperty("供应商数量")
    @ApiModelProperty(value = "供应商数量")
    private Integer supplierNum;

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
