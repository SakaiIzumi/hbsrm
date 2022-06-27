package net.bncloud.saas.tenant.web.payload;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.tenant.domain.vo.OrgType;
import net.bncloud.saas.tenant.service.command.CreateOrganizationCommand;

@Getter
@Setter
public class CreateOrgPayload {
    private String orgName;
    private String description;
    private OrgType orgType;

    public CreateOrganizationCommand toCommand() {
        return CreateOrganizationCommand.of(orgName, description, orgType);
    }
}
