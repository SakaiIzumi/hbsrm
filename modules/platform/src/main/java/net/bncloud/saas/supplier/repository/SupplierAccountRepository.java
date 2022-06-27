package net.bncloud.saas.supplier.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.saas.supplier.domain.SupplierAccount;
import net.bncloud.saas.supplier.domain.SupplierLinkMan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierAccountRepository extends BaseRepository<SupplierAccount, Long>, JpaSpecificationExecutor<SupplierAccount> {

}
