package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrgManagerRecordCreateEvent extends ApplicationEvent {
    private final CreateOrgManagerRecord createOrgManagerRecord;

    public OrgManagerRecordCreateEvent(Object source, CreateOrgManagerRecord createOrgManagerRecord) {
        super(source);
        this.createOrgManagerRecord = createOrgManagerRecord;
    }
}
