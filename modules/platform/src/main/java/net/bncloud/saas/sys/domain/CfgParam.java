package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ss_sys_config_param")
@Getter
@Setter
public class CfgParam extends AbstractAuditingEntity {

    private static final long serialVersionUID = 310445762684701523L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orgId;

    private Long companyId;

    private String code;

    @Enumerated(EnumType.STRING)
    private ParamType type;
    private String value;
    private String remark;
    @Embedded
    private ParamFilter paramFilter;
    @Embedded
    private ParamCategory category;
}
