package net.bncloud.financial.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Toby
 */
@Data
public class FinancialSourceBillParam implements Serializable {

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    @NotBlank(message = "单据类型不能为空")
    private String billType;

    /**
     * 来源单据ID
     */
    @ApiModelProperty(value = "来源单据ID")
    @NotNull(message = "来源单据ID不能为空")
    private Long sourceBillId;
}
