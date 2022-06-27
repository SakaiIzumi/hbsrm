package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.SupplierStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SupplierStaffRepository extends JpaRepository<SupplierStaff, Long>, JpaSpecificationExecutor<SupplierStaff> {

    Optional<SupplierStaff> findAllByUser_UserIdAndSupplier_Id(Long userId, Long supplierId);

    Optional<SupplierStaff> findByMobileAndSupplier_Code(String mobile, String supCode);

    List<SupplierStaff> findAllByUser_UserId(Long userId);

    List<SupplierStaff> findAllByUserUserId(Long userId);

    List<SupplierStaff> findAllBySupplier_IdAndIdIn(Long supplierId, List<Long> ids);


}
