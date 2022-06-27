package net.bncloud.event.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class SourcesCompany {

    private String sourcesName;

    private String sourcesCode;
}
