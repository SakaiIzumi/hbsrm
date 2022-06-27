package net.bncloud.saas.authorize.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 前端选择的脱敏分类
 */
@Table(name = "ss_sys_desensitize_type")
@Entity
@Getter
@Setter
public class DesensitizeType extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "dimension_type", columnDefinition = "varchar(100) comment '维度分类 权限属性统称'")
    private String dimensionType;

    @Column(name = "name", columnDefinition = "varchar(100) comment '脱敏名'")
    private String name;

    @Column(name = "code", columnDefinition = "varchar(100) comment '脱敏值'")
    private String code;

}
