package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DimensionPerm;
import net.bncloud.saas.authorize.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DimensionPermRepository extends JpaRepository<DimensionPerm, Long>, JpaSpecificationExecutor<DimensionPerm> {
    void deleteByRolesIn(List<Role> roles);

    List<DimensionPerm> findAllByRolesIn(List<Role> roles);


    @Modifying
    @Query(value = "delete from ss_sys_role_dimension_perm_rel where role_id in(?1) ", nativeQuery = true)
    void deleteByRoleIdsNative(List<Long> roleIds);

    @Modifying
    @Query(value = "delete from ss_sys_role_dimension_perm_rel where role_id =?1 ", nativeQuery = true)
    void deleteByRoleIdNative(Long roleId);
}
