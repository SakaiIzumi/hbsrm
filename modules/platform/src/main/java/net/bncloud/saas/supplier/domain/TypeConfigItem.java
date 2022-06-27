package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_type_config_item")
@Getter
@Setter
public class TypeConfigItem {
    private static final long serialVersionUID = 8576182436879916829L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_config_id", insertable = false, updatable = false)
    private Long typeConfigId; // parentId

    @Column(name = "item")
    private String item; //name

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "type_config_id")
    private Type type;

    public static TypeConfigItem of(Long id) {
        TypeConfigItem typeConfigItem = new TypeConfigItem();
        typeConfigItem.setId(id);
        return typeConfigItem;
    }
}
