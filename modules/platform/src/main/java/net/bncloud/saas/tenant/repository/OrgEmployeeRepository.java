package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.OrgEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgEmployeeRepository extends JpaRepository<OrgEmployee, Long>, JpaSpecificationExecutor<OrgEmployee> {

    @Query(value = "SELECT e.* FROM ss_tenant_org_employee e WHERE e.org_id = ?1 and e.dept_id = ?2", nativeQuery = true)
    List<OrgEmployee> findByOrgIdAndDeptId(Long orgId, Long deptId);

    @Query(value = "SELECT e.* FROM ss_tenant_org_employee e WHERE e.dept_id = ?1 and e.mobile = ?2 and e.org_id = ?3", nativeQuery = true)
    OrgEmployee findByDeptAndMobile(Long deptId, String mobile, Long orgId);

    @Query(value = "SELECT e.* FROM ss_tenant_org_employee e WHERE e.mobile = ?2 and e.org_id = ?1 and e.is_deleted=0 and e.enabled=1", nativeQuery = true)
    List<OrgEmployee> findByOrgAndMobile(Long orgId, String mobile);

    @Query(value = "SELECT e FROM OrgEmployee e WHERE e.user.userId =:userId ")
    Optional<OrgEmployee> findByUserId(@Param("userId") Long userId);

    Optional<OrgEmployee> findByUser_UserIdAndOrg_Id(Long userId, Long orgId);

    List<OrgEmployee> findAllByOrg_IdAndUser_UserIdIn(Long orgId, List<Long> userIds);

    List<OrgEmployee> findAllByOrg_IdAndIdIn(Long orgId, List<Long> ids);

    List<OrgEmployee> findAllByUserUserId(Long userId);

    Optional<OrgEmployee> findByMobileAndOrg_Id(String mobile, Long orgId);
}