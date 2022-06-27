package net.bncloud.saas.tenant.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSupplierDeptResultDTO {

    private Long orgId;
    private Long deptId;
    private String deptName;
}
