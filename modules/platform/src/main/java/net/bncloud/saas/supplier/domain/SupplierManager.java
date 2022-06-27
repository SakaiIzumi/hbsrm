package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "t_supplier_manager")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierManager extends AbstractAuditingEntity {
    private static final long serialVersionUID = 7998724978692632325L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private SupplierStaff supplierStaff;

    private String name;

    private String mobile;

    /**
     * 是否启用
     */
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private ManagerType managerType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;


}
