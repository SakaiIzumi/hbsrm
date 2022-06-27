package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrganizationCreateEvent extends ApplicationEvent {

    private final CreateOrganization createOrganization;

    public OrganizationCreateEvent(CreateOrganization createOrganization) {
        super(createOrganization);
        this.createOrganization = createOrganization;
    }

    public OrganizationCreateEvent of(CreateOrganization createOrganization) {
        return new OrganizationCreateEvent(createOrganization);
    }
}
