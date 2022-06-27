package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.tenant.domain.vo.UserId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_supplier_staff")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierStaff extends AbstractAuditingEntity {
    private static final long serialVersionUID = 340097846024831199L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String code;

    private String name;

    @Embedded
    private UserId user;

    /**
     * 工号
     */
    private String jobNo;
    /**
     * 职位
     */
    private String position;
    /**
     * 手机
     */
    private String mobile;
    /**
     * QQ
     */
    private String qq;

    /**
     * 是否启用
     */
    private boolean enabled;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "t_supplier_staff_role",
            joinColumns = {@JoinColumn(name = "supplier_staff_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

//    @OneToOne
//    private SupplierManager supplierManager;
}
