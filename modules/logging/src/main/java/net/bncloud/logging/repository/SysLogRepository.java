package net.bncloud.logging.repository;

import net.bncloud.logging.domain.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {
}
