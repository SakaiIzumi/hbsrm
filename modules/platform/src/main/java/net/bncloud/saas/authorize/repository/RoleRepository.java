package net.bncloud.saas.authorize.repository;

import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findByName(String name);

    List<Role> findAllByNameAndGroup(String roleName, RoleGroup roleGroup);

    // 系统默认配置的角色
    List<Role> findAllBySubjectTypeAndCoped(SubjectType subjectType, boolean aTrue);

    List<Role> findAllBySubjectTypeAndIdInOrSourceRoleIdIn(SubjectType org, Set<Long> roleIdList, Set<Long> roleIdList2);
}
