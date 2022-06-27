package net.bncloud.saas.tenant.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ss_tenant_position")
@Getter
@Setter
public class Position extends AbstractAuditingEntity {
    private static final long serialVersionUID = -8255751827657003037L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean disabled;
    @Column(name = "order_num")
    private int order;
}
