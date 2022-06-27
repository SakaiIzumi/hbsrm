package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaserBoundEvent extends ApplicationEvent {
    private final BindPurchaser bindPurchaser;

    public PurchaserBoundEvent(Object source, BindPurchaser bindPurchaser) {
        super(source);
        this.bindPurchaser = bindPurchaser;
    }
}
