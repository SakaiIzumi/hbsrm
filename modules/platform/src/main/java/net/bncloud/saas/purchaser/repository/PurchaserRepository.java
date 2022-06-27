package net.bncloud.saas.purchaser.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.tenant.domain.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaserRepository extends BaseRepository<Purchaser, Long>, JpaSpecificationExecutor<Purchaser> {
    Optional<Purchaser> findByCode(String s);

    @Query(value = "SELECT stp.code as purchaseCode,sti.code AS supplierCode,stp.org_id AS orgId,sti.name as supplierName,stp.name as purchaseName FROM ss_tenant_purchaser stp\n" +
            "JOIN ss_tenant_purchaser_supplier_ref spsr ON stp.id = spsr.pur_id JOIN t_supplier_info sti ON sti.id = spsr.sup_id \n" +
            "WHERE stp.CODE = :purchaseCode  AND sti.CODE = :supplierCode  limit 1", nativeQuery = true)
    Tuple findAllBySupCodeAndPurCode(@Param("purchaseCode") String purchaseCode, @Param("supplierCode") String supplierCode);

    @Modifying
    @Query(value = "DELETE FROM ss_tenant_purchaser_supplier_ref WHERE pur_id = :purId and sup_id in (:supIds)", nativeQuery = true)
    void cancelBound(@Param("purId") Long purId, @Param("supIds") List<Long> supIds);

    @Modifying
    @Query(value = "DELETE FROM ss_tenant_purchaser_supplier_ref WHERE pur_id = :id", nativeQuery = true)
    void cancelBindAllToPurchaser(@Param("id") Long id);

    Optional<List<Purchaser>> findAllByEnabled(boolean enabled);

    /**
     * 查询采购方集合，根据
     * @param purchaserCodeColl
     * @return
     */
    Optional<List<Purchaser>> findAllByCodeInAndEnabledIsTrue(Collection<?> purchaserCodeColl);

    /**
     * 查询所有采购方
     * @return
     */
    Optional<List<Purchaser>> findAllByOrganizationAndEnabledIsTrue(Organization organization );

}
