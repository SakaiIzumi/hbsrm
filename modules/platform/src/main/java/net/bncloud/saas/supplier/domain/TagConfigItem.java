package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_tag_config_item")
@Getter
@Setter
public class TagConfigItem implements Serializable {
    private static final long serialVersionUID = 8576182436879916829L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_config_id", insertable = false, updatable = false)
    private Long tagConfigId;

    @Column(name = "item")
    private String item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_config_id")
    private Tag tag;


    public static TagConfigItem of(Long id) {
        TagConfigItem tagConfigItem = new TagConfigItem();
        tagConfigItem.setId(id);
        return tagConfigItem;
    }


}
