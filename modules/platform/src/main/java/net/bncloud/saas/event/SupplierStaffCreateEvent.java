package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SupplierStaffCreateEvent extends ApplicationEvent {
    private final CreateSupplierStaff createSupplierStaff;

    public SupplierStaffCreateEvent(Object source, CreateSupplierStaff createSupplierStaff) {
        super(source);
        this.createSupplierStaff = createSupplierStaff;
    }
}
