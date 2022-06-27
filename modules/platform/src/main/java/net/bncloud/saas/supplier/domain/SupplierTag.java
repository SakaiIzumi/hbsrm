package net.bncloud.saas.supplier.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;


@Embeddable
@Getter
@Setter
public class SupplierTag {

    private Long tagId;

    private String tags;


}
