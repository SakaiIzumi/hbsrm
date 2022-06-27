package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgManagerRecordRepository extends JpaRepository<OrgManagerRecord, Long>, JpaSpecificationExecutor<OrgManagerRecord> {


    Optional<OrgManagerRecord> findByUserId(Long userId);

    Optional<OrgManagerRecord> findByMobile(String mobile);

}
