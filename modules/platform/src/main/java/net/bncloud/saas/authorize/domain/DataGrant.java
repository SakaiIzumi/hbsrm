package net.bncloud.saas.authorize.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 数据维度授权
 */
@Table(name = "ss_sys_data_grant")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DataGrant extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id", columnDefinition = "bigint(20) comment '数据主题ID'")
    private Long subjectId;

    @Column(name = "dimension_type", columnDefinition = "varchar(100) comment '维度分类 权限属性统称'")
    private String dimensionType;

    @Column(name = "dimension_code", columnDefinition = "varchar(100) comment '数据维度编码'")
    private String dimensionCode;

    @Column(name = "dimension_value", columnDefinition = "varchar(100) comment '数据维度值'")
    private String dimensionValue;

    @Column(name = "is_special", columnDefinition = "bit(1) comment '是否是特权'")
    private Boolean isSpecial;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "ss_sys_role_data_grant_rel",
            joinColumns = @JoinColumn(name = "grant_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
