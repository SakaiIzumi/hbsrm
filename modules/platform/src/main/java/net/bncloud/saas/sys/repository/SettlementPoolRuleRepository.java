package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.SettlementPoolRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Repository
public interface SettlementPoolRuleRepository extends JpaRepository<SettlementPoolRule, Long>, JpaSpecificationExecutor<SettlementPoolRule> {



}
