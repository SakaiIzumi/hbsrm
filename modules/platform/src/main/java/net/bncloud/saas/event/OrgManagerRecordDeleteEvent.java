package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrgManagerRecordDeleteEvent extends ApplicationEvent {
    private final DeleteOrgManagerRecord deleteOrgManagerRecord;

    public OrgManagerRecordDeleteEvent(Object source, DeleteOrgManagerRecord deleteOrgManagerRecord) {
        super(source);
        this.deleteOrgManagerRecord = deleteOrgManagerRecord;
    }
}
