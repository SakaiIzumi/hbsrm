package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingTalkApp;
import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.pk.DingTalkAppPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DingTalkAppRepository extends JpaRepository<DingTalkApp, DingTalkAppPK> {
    Optional<DingTalkApp> findOneByCorpAndAgentId(DingTalkCorp corp, Long agentId);
}
