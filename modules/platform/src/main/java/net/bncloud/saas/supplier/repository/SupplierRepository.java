package net.bncloud.saas.supplier.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierAccount;
import net.bncloud.saas.supplier.domain.SupplierExt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends BaseRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    Optional<Supplier> findOneByCode(String code);

    Optional<Supplier> findOneByCodeOrName(String code, String name);

    @Query("select  s from Supplier s join SupplierStaff st on s.id = st.supplier.id where st.user.userId=:userId ")
    List<Supplier> findAllByUserId(@Param("userId") Long userId);

    @Query("select ext from SupplierExt ext join Supplier s on  ext.supplierId=s.id where s.code=:code")
    Optional<SupplierExt> queryFinancialInfoOfSupplier(@Param("code") String code);

    @Query("select sa from SupplierAccount sa where sa.supplier.code=:code")
    List<SupplierAccount> querySupplierAccountInfo(String code);
}
