package net.bncloud.saas.purchaser.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.tenant.service.dto.OrganizationDTO;

import java.io.Serializable;
import java.util.List;

@Data
public class PurchaserDTO implements Serializable {
    private static final long serialVersionUID = 2063400707089318437L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "采购方编码")
    private String code;
    @ApiModelProperty(value = "采购方编码")
    private String name;
    @ApiModelProperty(value = "所属法人")
    private String artificialPerson;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @ApiModelProperty(value = "来源ID")
    private String sourceId;
    @ApiModelProperty(value = "来源编码")
    private String sourceCode;
    @ApiModelProperty(value = "公司id")
    private Long companyId;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    private OrganizationDTO organization;

    private List<SupplierDTO> suppliers;

    @ApiModelProperty(value = "供应商Ids")
    private List<Long> supplierIds;


}
