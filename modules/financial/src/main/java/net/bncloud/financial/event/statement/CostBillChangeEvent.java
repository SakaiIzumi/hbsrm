package net.bncloud.financial.event.statement;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Toby
 */
@Getter
public class CostBillChangeEvent extends ApplicationEvent {

    private Long billId;

    public CostBillChangeEvent(Object source, Long billId) {
        super(source);
        this.billId = billId;
    }
}
