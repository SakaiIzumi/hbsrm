package net.bncloud.common.base.service;

import net.bncloud.common.base.domain.QueryParam;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Pageable;

public abstract class BaseService implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}
