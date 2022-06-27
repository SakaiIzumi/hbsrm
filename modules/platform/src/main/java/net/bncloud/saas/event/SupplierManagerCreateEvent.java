package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SupplierManagerCreateEvent extends ApplicationEvent {
    private final CreateSupplierManager createSupplierManager;

    public SupplierManagerCreateEvent(Object source, CreateSupplierManager createSupplierManager) {
        super(source);
        this.createSupplierManager = createSupplierManager;
    }

}
