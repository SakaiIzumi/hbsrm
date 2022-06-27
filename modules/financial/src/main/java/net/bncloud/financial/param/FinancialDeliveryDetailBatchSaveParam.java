package net.bncloud.financial.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 出货明细表信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
public class FinancialDeliveryDetailBatchSaveParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货单id
     */
    @ApiModelProperty(value = "送货单id")
    @NotEmpty(message = "送货单id不能为空")
    private List<Long> deliveryIdList;

    /**
     * 对账单ID
     */
    @ApiModelProperty(value = "对账单ID")
    @NotNull(message = "对账单ID不能为空")
    private Long statementId;


}
