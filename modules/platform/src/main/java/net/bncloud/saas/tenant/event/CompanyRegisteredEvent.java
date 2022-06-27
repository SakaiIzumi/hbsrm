package net.bncloud.saas.tenant.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CompanyRegisteredEvent extends ApplicationEvent {
    private static final long serialVersionUID = -5780434362985487387L;
    private final CompanyDTO company;

    public CompanyRegisteredEvent(CompanyDTO company) {
        super(company);
        this.company = company;
    }

    public static CompanyRegisteredEvent of(CompanyDTO company) {
        return new CompanyRegisteredEvent(company);
    }
}
