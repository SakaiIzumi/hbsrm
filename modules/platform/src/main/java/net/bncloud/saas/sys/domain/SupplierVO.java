package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class SupplierVO {
    private Long id;
    private String code;
    private String name;
}
