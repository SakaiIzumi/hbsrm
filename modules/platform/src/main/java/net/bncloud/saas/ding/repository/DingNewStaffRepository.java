package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingNewStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingNewStaffRepository extends JpaRepository<DingNewStaff, Long> {
    @Query(value = "SELECT d.* FROM ss_ding_new_staff d WHERE d.employee_ids like concat('%',:employeeId,'%')", nativeQuery = true)
    DingNewStaff findLikeEmployeeId(@Param("employeeId")String employeeId);

    @Query(value = "SELECT d.* FROM ss_ding_new_staff d WHERE d.ddept_ids like concat('%',:ddepartId,'%')", nativeQuery = true)
    List<DingNewStaff> findLikeDdepartId(@Param("ddepartId")String ddepartId);

    @Query(value = "SELECT d.* FROM ss_ding_new_staff d WHERE d.org_id = ?1 and d.dd_mobile = ?2 limit 1", nativeQuery = true)
    DingNewStaff findByOrgAndMobile(Long orgId, String mobile);
}
