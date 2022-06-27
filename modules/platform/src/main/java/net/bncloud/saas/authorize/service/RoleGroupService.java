package net.bncloud.saas.authorize.service;

import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.repository.RoleGroupRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleGroupService {
    private final RoleGroupRepository roleGroupRepository;

    public RoleGroup findOneByNameAndSubjectType(String name, SubjectType subjectType) {
        return roleGroupRepository.findOneByNameAndSubjectType(name, subjectType);
    }
}
