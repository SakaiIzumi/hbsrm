package net.bncloud.saas.ding.service;

import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.DingTalkOrgIntegrationConfig;
import net.bncloud.saas.ding.repository.DingTalkCorpRepository;
import net.bncloud.saas.ding.repository.DingTalkOrgIntegrationConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DingTalkCorpService {

    private final DingTalkCorpRepository dingTalkCorpRepository;
    private final DingTalkOrgIntegrationConfigRepository dingTalkOrgIntegrationConfigRepository;

    public DingTalkCorpService(DingTalkCorpRepository dingTalkCorpRepository,
                               DingTalkOrgIntegrationConfigRepository dingTalkOrgIntegrationConfigRepository) {
        this.dingTalkCorpRepository = dingTalkCorpRepository;
        this.dingTalkOrgIntegrationConfigRepository = dingTalkOrgIntegrationConfigRepository;
    }

    public DingTalkCorp bind(Long orgId, String orgName, String corpId) {
        Optional<DingTalkCorp> corpOptional = dingTalkCorpRepository.findById(corpId);
        if (corpOptional.isPresent()) {
            DingTalkCorp dingTalkCorp = corpOptional.get();
            if (dingTalkCorp.getOrgId() != null && !dingTalkCorp.getOrgId().equals(orgId)) {
                throw new IllegalArgumentException("该组织已被" + dingTalkCorp.getOrgName() + "绑定");
            }
            return dingTalkCorp;
        }
        DingTalkCorp dingTalkCorp = new DingTalkCorp();
        dingTalkCorp.setOrgId(orgId);
        dingTalkCorp.setOrgName(orgName);
        dingTalkCorp.setCorpId(corpId);
        return dingTalkCorpRepository.save(dingTalkCorp);
    }

    public DingTalkOrgIntegrationConfig configIntegrationApp(String corpId, Long agentId, String appKey, String appSecret) {
        Optional<DingTalkCorp> corpOptional = dingTalkCorpRepository.findById(corpId);
        corpOptional.ifPresent(corp -> {
            Optional<DingTalkOrgIntegrationConfig> configOptional = dingTalkOrgIntegrationConfigRepository.findById(corpId);
            if (configOptional.isPresent()) {
                configOptional.ifPresent(config -> {
                    config.setAgentId(agentId);
                    config.setAppKey(appKey);
                    config.setAppSecret(appSecret);
                    config.setIsOrg(1);
                    config.setOrgId(corp.getOrgId());
                    dingTalkOrgIntegrationConfigRepository.save(config);
                });
            } else {
                DingTalkOrgIntegrationConfig config = new DingTalkOrgIntegrationConfig();
                config.setCorp(corp);
                config.setAgentId(agentId);
                config.setAppKey(appKey);
                config.setAppSecret(appSecret);
                config.setIsOrg(1);
                config.setOrgId(corp.getOrgId());
                dingTalkOrgIntegrationConfigRepository.save(config);
            }
        });
        return dingTalkOrgIntegrationConfigRepository.findById(corpId).get();
    }

    public DingTalkCorp getOrgCorp(Long orgId) {
        return dingTalkCorpRepository.findOneByOrgId(orgId).orElse(null);
    }
}
