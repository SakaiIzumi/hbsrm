package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRelateInfoUpdateEvent extends ApplicationEvent {
    private UserRelateInfoUpdate userRelateInfoUpdate;

    public UserRelateInfoUpdateEvent(Object source, UserRelateInfoUpdate userRelateInfoUpdate) {
        super(source);
        this.userRelateInfoUpdate = userRelateInfoUpdate;
    }
}
