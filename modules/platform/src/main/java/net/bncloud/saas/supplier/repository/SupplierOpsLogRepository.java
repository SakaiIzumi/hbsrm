package net.bncloud.saas.supplier.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.saas.supplier.domain.SupplierOpsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierOpsLogRepository extends BaseRepository<SupplierOpsLog, Long>, JpaSpecificationExecutor<SupplierOpsLog> {

}
