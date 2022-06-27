package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DesensitizeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface DesensitizeRuleRepository extends JpaRepository<DesensitizeRule, Long>, JpaSpecificationExecutor<DesensitizeRule> {
}
