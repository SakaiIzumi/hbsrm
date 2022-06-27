package net.bncloud.saas.authorize.domain;

import lombok.*;
import net.bncloud.saas.authorize.domain.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 存角色与数据权限分类关系
 */
@Table(name = "ss_sys_dimension_perm")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DimensionPerm implements Serializable {

    private static final long serialVersionUID = -5172520023748026107L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dimension_type", columnDefinition = "varchar(100) comment '维度分类 权限属性统称'")
    private String dimensionType;

    @Column(name = "perm_name", columnDefinition = "varchar(100) comment '数据权限名'")
    private String permName;

    @Column(name = "perm_value", columnDefinition = "varchar(100) comment '数据权限值'")
    private String permValue;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "ss_sys_role_dimension_perm_rel",
            joinColumns = @JoinColumn(name = "perm_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "perm_role_uni", columnNames = {"perm_id", "role_id"})
            }
    )
    private List<Role> roles;
}
