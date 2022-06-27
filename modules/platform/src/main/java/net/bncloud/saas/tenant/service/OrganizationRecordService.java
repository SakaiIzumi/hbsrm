package net.bncloud.saas.tenant.service;

import lombok.AllArgsConstructor;
import net.bncloud.saas.tenant.domain.OrganizationRecord;
import net.bncloud.saas.tenant.repository.OrganizationRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrganizationRecordService {

    private final OrganizationRecordRepository organizationRecordRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(OrganizationRecord organizationRecord) {
        organizationRecordRepository.save(organizationRecord);
    }
}
