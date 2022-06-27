package net.bncloud.saas.event;

import lombok.Getter;
import net.bncloud.saas.tenant.service.command.CreatedOrgManagerCommand;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrgManagerCreatedEvent extends ApplicationEvent {

    private final CreatedOrgManagerCommand createdOrgManager;

    public OrgManagerCreatedEvent(Object source, CreatedOrgManagerCommand createdOrgManager) {
        super(source);
        this.createdOrgManager = createdOrgManager;
    }

}