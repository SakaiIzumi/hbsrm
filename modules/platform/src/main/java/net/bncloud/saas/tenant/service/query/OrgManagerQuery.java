package net.bncloud.saas.tenant.service.query;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;

@Getter
@Setter
public class OrgManagerQuery {
    private Long orgId;
    private String name;
    private String userCode;
    private String mobile;
    private ManagerType managerType;
}
