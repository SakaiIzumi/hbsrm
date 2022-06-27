package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.OrganizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRecordRepository extends JpaRepository<OrganizationRecord, Long>, JpaSpecificationExecutor<OrganizationRecord> {


}
