package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 参数配置默认表
 */
@Entity
@Table(name = "ss_sys_config_param_mould")
@Getter
@Setter
public class CfgParamMould extends AbstractAuditingEntity {

    private static final long serialVersionUID = 310445762684701523L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
