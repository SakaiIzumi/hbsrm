package net.bncloud.saas.event;

import net.bncloud.saas.supplier.domain.Supplier;
import org.springframework.context.ApplicationEvent;

public class SupplierCreatedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 151398321540147758L;
    private final Supplier supplier;
    public SupplierCreatedEvent(Object source, Supplier supplier) {
        super(source);
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
