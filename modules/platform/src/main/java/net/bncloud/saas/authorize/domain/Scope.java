package net.bncloud.saas.authorize.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Scope {
    private String scope;
}
