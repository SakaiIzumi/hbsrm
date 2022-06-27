package net.bncloud.financial.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bncloud.financial.entity.FileInfo;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.entity.FinancialCostBillLine;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FinancialCostBillSaveParam extends FinancialCostBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件列表
     */
    @Valid
    @ApiModelProperty(value = "附件列表")
    private List<FileInfo> attachmentList;

    @ApiModelProperty(value = "费用明细列表")
    private List<FinancialCostBillLine> costBillLineList;

}
