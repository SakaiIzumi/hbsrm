package net.bncloud.quotation.event.material;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Toby
 */
@Getter
public class MaterialFormChangeEvent extends ApplicationEvent {

    private Long materialFormId;

    public MaterialFormChangeEvent(Object source , Long materialFormId) {
        super(source);
        this.materialFormId = materialFormId;
    }
}
