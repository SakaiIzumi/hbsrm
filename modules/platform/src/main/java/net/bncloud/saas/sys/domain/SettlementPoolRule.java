package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Getter
@Setter
@Entity
@Table(name = "ss_sys_settlement_pool_rule")
@Accessors(chain = true)
public class SettlementPoolRule extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleCode;

    private String ruleName;

    private String billType;

    private String status;


}
