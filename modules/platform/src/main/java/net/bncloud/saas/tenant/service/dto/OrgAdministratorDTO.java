package net.bncloud.saas.tenant.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;

@Getter
@Setter
public class OrgAdministratorDTO extends BaseDTO  {
    private static final long serialVersionUID = 3372738278033518926L;
    private Long id;
    private Long orgId;
    private String managerType;
    private String managerTypeName;
    private Long userId;
    private String name;
    private String mobile;
    private String deptName;
    private String jobNumber;

    private ManagerOperatePermission permissionButton = new ManagerOperatePermission();

}
