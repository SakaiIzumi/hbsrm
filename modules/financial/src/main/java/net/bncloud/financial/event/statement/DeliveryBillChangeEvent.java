package net.bncloud.financial.event.statement;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Toby
 */
@Getter
public class DeliveryBillChangeEvent extends ApplicationEvent {

    private Long billId;

    public DeliveryBillChangeEvent(Object source, Long billId) {
        super(source);
        this.billId = billId;
    }
}
