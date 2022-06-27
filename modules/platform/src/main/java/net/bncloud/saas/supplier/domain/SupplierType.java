package net.bncloud.saas.supplier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class SupplierType {

    private Long typeId;

    private String types;

}
