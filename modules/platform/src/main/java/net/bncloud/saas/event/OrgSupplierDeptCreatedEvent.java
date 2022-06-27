package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrgSupplierDeptCreatedEvent extends ApplicationEvent {
    private static final long serialVersionUID = -3285637454501674735L;
    private final CreatedOrgDept createdOrgDept;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param createdOrgDept
     */
    public OrgSupplierDeptCreatedEvent(Object source, CreatedOrgDept createdOrgDept) {
        super(source);
        this.createdOrgDept = createdOrgDept;
    }
}
