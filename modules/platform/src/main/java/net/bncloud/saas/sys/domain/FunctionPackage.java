package net.bncloud.saas.sys.domain;

import net.bncloud.common.domain.AbstractTreeEntity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

//@Entity
//@Table(name = "ss_sys_func_pkg")
//@Getter
//@Setter
public class FunctionPackage extends AbstractTreeEntity<FunctionPackage> {

    private static final long serialVersionUID = 6305315359476093910L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "ss_sys_func_pkc_include_func", joinColumns = @JoinColumn(name = "pkg_id"),
            inverseJoinColumns = @JoinColumn(name = "func_id"))
    private List<Function> functions;

}
