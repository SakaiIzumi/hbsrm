package net.bncloud.saas.authorize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ss_sys_role", uniqueConstraints = {
        @UniqueConstraint(name = "unique_idx_role_name_group", columnNames = {"name", "group_id"})
})
@Getter
@Setter
public class Role extends AbstractAuditingEntity {

    private static final long serialVersionUID = 987131151861439415L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    /**
     * 是否启用
     */
    private boolean enabled;

    @JsonIgnoreProperties("roles")
    @ManyToOne
    @JoinColumn(name = "group_id")
    @ApiModelProperty(value = "角色组", hidden = true)
    private RoleGroup group;


    /**
     * 是否是复制值
     */
    private boolean coped;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    private Long supId;

    private Long orgId;

    private Long sourceRoleId;

    /**
     * 是否是系统内置角色
     */
    private boolean sysDefault;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ss_sys_role_menu", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = {@UniqueConstraint(name = "role_menu_uni", columnNames = {"role_id", "menu_id"})}
    )
    private List<Menu> menus;


    //    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    @ApiModelProperty(value = "用户", hidden = true)
    @Transient
    private List<User> users;

    public boolean getCoped() {
        return coped;
    }

    public void setCoped(boolean coped) {
        this.coped = coped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
