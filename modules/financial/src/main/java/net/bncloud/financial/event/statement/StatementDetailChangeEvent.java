package net.bncloud.financial.event.statement;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Toby
 */
@Getter
public class StatementDetailChangeEvent extends ApplicationEvent {

    private Long statementId;

    public StatementDetailChangeEvent(Object source, Long statementId) {
        super(source);
        this.statementId = statementId;
    }
}
