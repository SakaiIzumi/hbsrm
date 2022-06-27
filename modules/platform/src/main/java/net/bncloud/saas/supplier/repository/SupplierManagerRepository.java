package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierManagerRepository extends JpaRepository<SupplierManager, Long> {

    List<SupplierManager> findAllBySupplierStaff_User_UserId(Long userId);

    List<SupplierManager> findAllBySupplier_IdAndSupplierStaff_User_UserIdIn(Long supplierId, List<Long> userIds);

    List<SupplierManager> findAllBySupplierStaffIn(List<SupplierStaff> staffs);

    Optional<SupplierManager> findFirstBySupplierStaff_Id(Long staffId);
}
