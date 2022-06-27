package net.bncloud.saas.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrgEmployeeCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5243071758783841064L;
    private final CreatedEmployee employee;

    public OrgEmployeeCreatedEvent(Object source, CreatedEmployee employee) {
        super(source);
        this.employee = employee;
    }
}
