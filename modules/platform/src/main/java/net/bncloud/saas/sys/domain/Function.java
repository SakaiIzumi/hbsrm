package net.bncloud.saas.sys.domain;

import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.authorize.domain.Menu;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

//@Entity
//@Table(name = "ss_sys_func")
//@Getter
//@Setter
public class Function extends AbstractAuditingEntity {

    private static final long serialVersionUID = 6980926081586462432L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "ss_sys_func_menu", joinColumns = @JoinColumn(name = "func_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private List<Menu> menus;

    @ElementCollection(targetClass = DependencyFunc.class)
    @CollectionTable(
            name = "ss_sys_func_dependencies",
            joinColumns = {@JoinColumn(name = "func_id", referencedColumnName = "id")}
    )
    private List<DependencyFunc> dependencies;
}
