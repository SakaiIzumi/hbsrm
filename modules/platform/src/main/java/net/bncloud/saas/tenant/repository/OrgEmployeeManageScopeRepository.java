package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.OrgEmployeeMangeScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OrgEmployeeManageScopeRepository extends JpaRepository<OrgEmployeeMangeScope, Long>, JpaSpecificationExecutor<OrgEmployeeMangeScope> {
    OrgEmployeeMangeScope findByEmployeeId(Long employeeId);

    @Query(value="update OrgEmployeeMangeScope set scope = ?2 where employeeId= ?1")
    @Modifying
    void updateByEmployeeIdAndScope(Long employeeId,String scope);
}
