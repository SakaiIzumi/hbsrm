package net.bncloud.saas.event;

import org.springframework.context.ApplicationEvent;

public class UserInfoCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -8094186707276722558L;
    private final CreatedUser user;

    public UserInfoCreatedEvent(Object source, CreatedUser user) {
        super(source);
        this.user = user;
    }

    public CreatedUser getUser() {
        return user;
    }
}
