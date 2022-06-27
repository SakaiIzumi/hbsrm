package net.bncloud.saas.tenant.service.command;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.tenant.domain.OrgDepartment;

@Getter
@Setter
public class CreateDeptCommand {
    private Long parentId;
    private String name;

    public OrgDepartment create() {
        OrgDepartment department = new OrgDepartment();
        department.setParentId(parentId);
        department.setName(name);
        return department;
    }
}
