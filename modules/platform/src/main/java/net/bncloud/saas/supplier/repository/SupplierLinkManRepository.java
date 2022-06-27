package net.bncloud.saas.supplier.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.saas.supplier.domain.SupplierLinkMan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierLinkManRepository extends BaseRepository<SupplierLinkMan, Long>, JpaSpecificationExecutor<SupplierLinkMan> {

    List<SupplierLinkMan> findAllBySupplier_IdAndAllowOps(Long supplierId, boolean allowOps);
}
