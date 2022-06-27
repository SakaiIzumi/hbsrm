package net.bncloud.saas.sys.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="ss_sys_dict")
@Getter
@Setter
@NoArgsConstructor
public class Dict extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -224627725119022477L;
    @Id
    @NotBlank
    @ApiModelProperty(value = "字典编码，唯一")
    private String code;

    @ApiModelProperty(value = "描述")
    private String description;

    @Column(length = 1024)
    private String extJson;

    @OneToMany(mappedBy = "dict",cascade={CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<DictItem> items;
}
