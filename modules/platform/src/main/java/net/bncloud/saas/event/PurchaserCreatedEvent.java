package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaserCreatedEvent extends ApplicationEvent {
    private final CreatePurchaser createPurchaser;

    public PurchaserCreatedEvent(Object source, CreatePurchaser createPurchaser) {
        super(source);
        this.createPurchaser = createPurchaser;
    }
}
