package net.bncloud.saas.authorize.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ss_sys_api")
public class Api extends AbstractAuditingEntity {

    private static final long serialVersionUID = -8809337119237471675L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String moduleName;
    private String uri;
    private String perms;
    private String method;
    private Integer orderNum;

    /*@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "t_sys_privilege_api", joinColumns = @JoinColumn(name = "privilege_id"),
            inverseJoinColumns = @JoinColumn(name = "api_id"))
    private List<Privilege> privileges;*/

}
