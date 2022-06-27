package net.bncloud.saas.tenant.service;

import lombok.AllArgsConstructor;
import net.bncloud.saas.tenant.domain.OrgManager;
import net.bncloud.saas.tenant.repository.OrgManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrgManagerService {
    private final OrgManagerRepository orgManagerRepository;

    public void save(OrgManager orgManager) {
        orgManagerRepository.save(orgManager);
    }

    public List<OrgManager> findAllByUserId(Long userId) {
        return orgManagerRepository.findAllByUser_Id(userId);
    }
}
