package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaserAllBoundEvent extends ApplicationEvent {
    private final AllBindPurchaser allBindPurchaser;

    public PurchaserAllBoundEvent(Object source, AllBindPurchaser allBindPurchaser) {
        super(source);
        this.allBindPurchaser = allBindPurchaser;
    }
}
