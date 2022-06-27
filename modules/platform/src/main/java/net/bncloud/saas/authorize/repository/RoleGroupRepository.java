package net.bncloud.saas.authorize.repository;

import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.saas.authorize.domain.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

    /**
     * 根据名称查询
     *
     * @param name /
     * @return /
     */
    RoleGroup findOneByName(String name);

    RoleGroup findOneByNameAndSubjectType(String name,SubjectType subjectType);

    List<RoleGroup> findAllBySubjectTypeAndVisible(SubjectType subjectType,boolean visible);

    List<RoleGroup> findAllBySubjectTypeIn(List<SubjectType> subjectTypes);
}
