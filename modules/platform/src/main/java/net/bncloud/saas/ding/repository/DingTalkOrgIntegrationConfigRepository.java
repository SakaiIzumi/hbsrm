package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingTalkOrgIntegrationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DingTalkOrgIntegrationConfigRepository extends JpaRepository<DingTalkOrgIntegrationConfig, String> {
    @Query(value = "SELECT c.* FROM ss_ding_integration_config c WHERE c.is_deleted = 0 and c.org_id = ?1 and c.is_org = ?2 order by c.agent_id asc limit 1", nativeQuery = true)
    DingTalkOrgIntegrationConfig findByOrgId(Long orgId, int isOrg);
}