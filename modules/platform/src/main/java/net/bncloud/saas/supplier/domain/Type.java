package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_type_config", uniqueConstraints = {
        @UniqueConstraint(name = "unique_idx_org_type_config", columnNames = {"org_id", "group_name"})
})
@Getter
@Setter
public class Type extends AbstractAuditingEntity {

    private static final long serialVersionUID = -6147320817321708350L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "group_name")
    private String group;


    @JsonIgnoreProperties("type")
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<TypeConfigItem> typesList;

    public static Type of(Long id) {
        Type type = new Type();
        type.setId(id);
        return type;
    }
}
