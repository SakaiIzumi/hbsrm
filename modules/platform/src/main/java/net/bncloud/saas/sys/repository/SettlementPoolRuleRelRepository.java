package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.SettlementPoolRuleRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Repository
public interface SettlementPoolRuleRelRepository extends JpaRepository<SettlementPoolRuleRel, Long> {

    List<SettlementPoolRuleRel> findBySettlementPoolRuleId(Long ruleId);
}
