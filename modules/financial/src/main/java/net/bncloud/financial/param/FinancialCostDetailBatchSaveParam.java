package net.bncloud.financial.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 费用信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
public class FinancialCostDetailBatchSaveParam implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    @NotEmpty(message = "费用id不能为空")
    private List<Long> costIdList;

    /**
     * 对账单ID
     */
    @ApiModelProperty(value = "对账单ID")
    private long statementId;


}
