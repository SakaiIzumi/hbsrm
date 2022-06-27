package net.bncloud.saas.event;

import net.bncloud.saas.ding.domain.DingUser;
import org.springframework.context.ApplicationEvent;

public class DingUserCreatedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6287656895140229013L;
    private final DingUser dingUser;
    public DingUserCreatedEvent(Object source, DingUser dingUser) {
        super(source);
        this.dingUser = dingUser;
    }

    public DingUser getDingUser() {
        return dingUser;
    }
}
