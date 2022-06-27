package net.bncloud.saas.sys.domain.vo;

import lombok.Data;
import net.bncloud.saas.sys.domain.SettlementPoolRule;
import net.bncloud.saas.sys.domain.SettlementPoolRuleRel;

import java.util.List;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Data
public class SettlementPoolRuleVO extends SettlementPoolRule {

    private List<SettlementPoolRuleRel> list;
}
