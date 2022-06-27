package net.bncloud.financial.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bncloud.financial.entity.FinancialDeliveryBill;
import net.bncloud.financial.entity.FinancialDeliveryBillLine;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FinancialDeliveryBillSaveParam extends FinancialDeliveryBill implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "送货明细列表")
    private List<FinancialDeliveryBillLine> deliveryBillLineList;

}
