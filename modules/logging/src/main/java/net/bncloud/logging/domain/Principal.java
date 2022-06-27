package net.bncloud.logging.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Principal {
    private Long userId;
    private String name;
    private String login;
    private String mobile;
}
