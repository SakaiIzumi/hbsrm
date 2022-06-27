package net.bncloud.saas.tenant.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.vo.OrgType;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateOrganizationCommand {

    private String orgName;
    private String description;
    private OrgType orgType;
    private String zltId;

    public static CreateOrganizationCommand of(String orgName, String description, OrgType orgType, String zltId) {
        return new CreateOrganizationCommand(orgName, description, orgType, zltId);
    }

    public static CreateOrganizationCommand of(String orgName, String description, OrgType orgType) {
        return new CreateOrganizationCommand(orgName, description, orgType, null);
    }

    public Organization create() {
        Organization org = new Organization();
        org.setName(orgName);
        org.setDescription(description);
        org.setOrgType(orgType);
        return org;
    }
}
