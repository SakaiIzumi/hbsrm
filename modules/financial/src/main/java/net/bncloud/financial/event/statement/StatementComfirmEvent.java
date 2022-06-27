package net.bncloud.financial.event.statement;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Toby
 */
@Getter
public class StatementComfirmEvent extends ApplicationEvent {

    private Long statementId;

    public StatementComfirmEvent(Object source, Long statementId) {
        super(source);
        this.statementId = statementId;
    }
}
