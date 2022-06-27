package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoleBatchGrantEvent extends ApplicationEvent {
    private final RoleBatch roleBatch;

    public RoleBatchGrantEvent(Object source, RoleBatch roleBatch) {
        super(source);
        this.roleBatch = roleBatch;
    }

}
