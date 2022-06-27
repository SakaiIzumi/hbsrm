package net.bncloud.saas.sys.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 币种信息表
 * @since 2022/1/5
 */
@Entity
@Table(name = "ss_sys_currency")
@Getter
@Setter
public class Currency extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(name = "币种编码")
    private String currencyCode;
    @ApiModelProperty(name = "币种名称")
    private String currencyName;
    @ApiModelProperty(name = "币种英文名")
    private String englishName;
}
