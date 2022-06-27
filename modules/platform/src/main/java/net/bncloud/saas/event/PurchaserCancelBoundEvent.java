package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaserCancelBoundEvent extends ApplicationEvent {
    private final BindPurchaser bindPurchaser;

    public PurchaserCancelBoundEvent(Object source, BindPurchaser bindPurchaser) {
        super(source);
        this.bindPurchaser = bindPurchaser;
    }
}
