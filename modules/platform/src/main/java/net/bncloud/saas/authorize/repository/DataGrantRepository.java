package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DataGrant;
import net.bncloud.saas.authorize.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataGrantRepository extends JpaRepository<DataGrant, Long>, JpaSpecificationExecutor<DataGrant> {



    List<DataGrant> findAllByRolesIn(List<Role> roles);

    @Modifying
    @Query(value = "delete from ss_sys_role_data_grant_rel where role_id =?1 ", nativeQuery = true)
    void deleteByRoleIdNative(Long roleId);
}
