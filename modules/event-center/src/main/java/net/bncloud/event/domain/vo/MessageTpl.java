package net.bncloud.event.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class MessageTpl {
    private String tag;
    private String name;
}
