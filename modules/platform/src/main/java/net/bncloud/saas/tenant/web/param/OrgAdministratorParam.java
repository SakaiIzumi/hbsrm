package net.bncloud.saas.tenant.web.param;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;

@Getter
@Setter
public class OrgAdministratorParam {
    private Long orgId;
    private ManagerType managerType;
    private Long userId;
    private String name;
}
