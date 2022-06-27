package net.bncloud.quotation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * 定价选择供应商后点击确定跳转页面Vo
 *
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
@Data
public class QuotationLineExtListBySupplierIdsVo implements Serializable {

    /**
     * 询价单id
     */
    @ApiModelProperty(value = "询价单号")
    private String quotationId;

    /**
     * 供应商ids
     */
    @ApiModelProperty(value = "供应商ids")
    List<String> supplierIds=new ArrayList<>();


}
