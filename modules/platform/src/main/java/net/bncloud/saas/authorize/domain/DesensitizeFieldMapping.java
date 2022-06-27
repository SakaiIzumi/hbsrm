package net.bncloud.saas.authorize.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 脱敏字段映射表
 */
@Table(name = "ss_sys_desensitize_field_mapping")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DesensitizeFieldMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private DataSubject dataSubject;

    @Column(name = "name", columnDefinition = "varchar(20) comment '脱敏字段名'")
    private String name;

    /**
     * bean filedName
     */
    @Column(name = "dimension_code", columnDefinition = "varchar(20) comment '脱敏字段维度'")
    private String dimensionCode;

    @ManyToOne
    @JoinColumn(name = "rule_id", columnDefinition = "bigint(20) comment '规则id'")
    private DesensitizeRule desensitizeRule;
}
