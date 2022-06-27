package net.bncloud.saas.supplier.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class SupplierStaffVO implements Serializable {

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

//    @ApiModelProperty(value = "供应商id")
//    private Long supplierId;

    @ApiModelProperty(value = "供应商成员名称")
    @NotBlank(message = "供应商成员名称不能为空")
    private String name;

    @ApiModelProperty(value = "供应商成员手机")
    private String mobile;

    @ApiModelProperty(value = "供应商成员状态")
    private Boolean enabled;

    @ApiModelProperty(value = "供应商成员角色")
    private List<Long> roleIds;

}
