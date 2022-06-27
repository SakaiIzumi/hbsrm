package net.bncloud.api.feign.saas.sys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
public class ParamFilter {

    @Enumerated(EnumType.STRING)
    private ParamFilterType filterType;
    private String filterValue;
}
