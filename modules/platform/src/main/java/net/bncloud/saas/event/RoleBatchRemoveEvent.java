package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoleBatchRemoveEvent extends ApplicationEvent {
    private final RoleBatch roleBatch;

    public RoleBatchRemoveEvent(Object source, RoleBatch roleBatch) {
        super(source);
        this.roleBatch = roleBatch;
    }

}
