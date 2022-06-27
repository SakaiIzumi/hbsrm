package net.bncloud.saas.authorize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.user.RoleGroupType;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.security.auth.Subject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "ss_sys_role_group")
@Getter
@Setter
public class RoleGroup extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1506041595369895563L;

    public RoleGroup() {
    }

    public RoleGroup(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 是否是系统内置角色组
     */
    private boolean sysDefault;

//    @Enumerated(EnumType.STRING)
//    private RoleGroupType type;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @JsonIgnoreProperties("group")
    @OneToMany(mappedBy = "group")
    private List<Role> roles;

    private boolean visible;

    @Transient
    public boolean isDeletable() {
        return !sysDefault && !hasRole();
    }

    @Transient
    public boolean hasRole() {
        return roles != null && roles.size() > 0;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
