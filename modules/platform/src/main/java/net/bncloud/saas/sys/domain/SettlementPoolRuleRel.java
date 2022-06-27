package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Getter
@Setter
@Entity
@Table(name = "ss_sys_settlement_pool_rule_rel")
public class SettlementPoolRuleRel extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long settlementPoolRuleId;

    private Integer billStatus;

    private String satisfyValue;

    private Integer satisfyUnit;

    private String bill_prefix;
}
