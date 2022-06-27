package net.bncloud.saas.supplier.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class BatchSupplierManagerVO implements Serializable {

    @ApiModelProperty(value = "供应商id")
    @NotBlank(message = "供应商不能为空")
    private List<Long> supplierId;

    @ApiModelProperty(value = "供应商管理员id")
    @NotBlank(message = "供应商管理员不能为空")
    private List<Long> supplierMemberIds;

}
