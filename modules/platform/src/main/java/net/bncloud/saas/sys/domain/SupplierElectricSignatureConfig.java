package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 供应商电子签章配置
 */
@Entity
@Table(name = "ss_supplier_electric_signature_config")
@Getter
@Setter
public class SupplierElectricSignatureConfig extends AbstractAuditingEntity {

    private static final long serialVersionUID = 7741168768873075483L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "supplier_id")),
            @AttributeOverride(name = "code", column = @Column(name = "supplier_code")),
            @AttributeOverride(name = "name", column = @Column(name = "supplier_name"))
    })
    private SupplierVO supplier;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ElectricSignatureConfigType type;
    private boolean enabled;
}
