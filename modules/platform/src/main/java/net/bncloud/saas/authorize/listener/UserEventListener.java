package net.bncloud.saas.authorize.listener;

import net.bncloud.saas.authorize.service.UserService;
import net.bncloud.saas.event.UserInfoCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final UserService userService;

    public UserEventListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener(UserInfoCreatedEvent.class)
    public void userCreatedEventListener(UserInfoCreatedEvent event) {
        userService.createUser(event.getUser());
    }
}
