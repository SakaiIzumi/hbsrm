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
@Table(name = "t_tag_config", uniqueConstraints = {
        @UniqueConstraint(name = "unique_idx_org_tag_config", columnNames = {"org_id", "group_name"})
})
@Getter
@Setter
public class Tag extends AbstractAuditingEntity {
    private static final long serialVersionUID = 8576182436879916829L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "group_name")
    private String group;


    @JsonIgnoreProperties("tag")
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<TagConfigItem> tagsList;


    public static Tag of(Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }

}
