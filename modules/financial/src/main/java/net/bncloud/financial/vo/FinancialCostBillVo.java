package net.bncloud.financial.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.financial.entity.FileInfo;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.entity.FinancialCostBillLine;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用单据视图表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
public class FinancialCostBillVo extends FinancialCostBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可操作按钮
     */
    @ApiModelProperty(value = "可操作按钮")
    private Map<String, Boolean> permissionButton;


    /**
     * 附件列表
     */
    @ApiModelProperty(value = "附件列表")
    private List<FileInfo> attachmentList;


    /**
     * 费用单明细列表
     */
    @ApiModelProperty(value = "费用单明细列表")
    private List<FinancialCostBillLine> FinancialCostBillLineList;


}
