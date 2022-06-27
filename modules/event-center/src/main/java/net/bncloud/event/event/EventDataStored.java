package net.bncloud.event.event;

import net.bncloud.event.domain.EventDetail;
import net.bncloud.event.domain.vo.SourcesCompany;
import org.springframework.context.ApplicationEvent;

public class EventDataStored extends ApplicationEvent {

    private static final long serialVersionUID = 5863147486578204961L;

    private final EventDetail eventDetail;
    private SourcesCompany sources;
    public EventDataStored(Object source, EventDetail eventDetail) {
        super(source);
        this.eventDetail = eventDetail;
    }
    public EventDataStored(Object source, EventDetail eventDetail, SourcesCompany sources) {
        super(source);
        this.eventDetail = eventDetail;
        this.sources = sources;
    }

    public EventDetail getEventDetail() {
        return eventDetail;
    }

    public SourcesCompany getSources() {
        return sources;
    }


}
