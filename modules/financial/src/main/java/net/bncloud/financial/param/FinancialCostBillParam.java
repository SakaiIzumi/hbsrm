package net.bncloud.financial.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.financial.entity.FinancialCostBill;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * <p>
 * 费用单据信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FinancialCostBillParam extends FinancialCostBill implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "tab栏分类")
    @NotBlank(message = "tab栏分类不能为空")
    private String tabCategory;


}
