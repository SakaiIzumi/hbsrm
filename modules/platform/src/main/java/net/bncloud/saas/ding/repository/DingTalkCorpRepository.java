package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingTalkCorp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DingTalkCorpRepository extends JpaRepository<DingTalkCorp, String> {
    Optional<DingTalkCorp> findOneByOrgId(Long orgId);
}
