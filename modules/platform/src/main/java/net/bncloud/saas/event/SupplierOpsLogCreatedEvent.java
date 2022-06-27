package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SupplierOpsLogCreatedEvent extends ApplicationEvent {

    private final CreateSupplierOpsLog createSupplierOpsLog;

    public SupplierOpsLogCreatedEvent(Object source, CreateSupplierOpsLog createSupplierOpsLog) {
        super(source);
        this.createSupplierOpsLog = createSupplierOpsLog;
    }
}
