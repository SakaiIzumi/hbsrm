package net.bncloud.saas.authorize.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ss_sys_user")
@Getter
@Setter
public class User extends AbstractAuditingEntity {
    private static final long serialVersionUID = 3876808062442570019L;
    @Id
    private Long userId;
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ApiModelProperty(value = "用户角色")
    @JoinTable(name = "ss_sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn (name = "role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }
}
