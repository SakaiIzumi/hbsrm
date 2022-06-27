package net.bncloud.saas.sys.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_sys_dict_item")
@Getter
@Setter
public class DictItem extends AbstractAuditingEntity {
    private static final long serialVersionUID = -4803917777447516288L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    @Column(name = "order_num")
    private int order;

    @Column(length = 1024)
    private String extJson;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "code")
    @ApiModelProperty(value = "字典", hidden = true)
    private Dict dict;
}
