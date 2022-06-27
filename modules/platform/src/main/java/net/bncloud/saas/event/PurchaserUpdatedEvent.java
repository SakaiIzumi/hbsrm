package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaserUpdatedEvent extends ApplicationEvent {
    private final UpdatePurchaser updatePurchaser;

    public PurchaserUpdatedEvent(Object source, UpdatePurchaser updatePurchaser) {
        super(source);
        this.updatePurchaser = updatePurchaser;
    }
}
