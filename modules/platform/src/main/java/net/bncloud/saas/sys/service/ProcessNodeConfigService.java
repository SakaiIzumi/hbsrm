package net.bncloud.saas.sys.service;

import net.bncloud.saas.sys.domain.ProcessNodeConfig;
import net.bncloud.saas.sys.repository.ProcessNodeConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/4
 */
@Service
public class ProcessNodeConfigService {

    @Autowired
    private ProcessNodeConfigRepository processNodeConfigRepository;

    public List<ProcessNodeConfig> getProcessNodeConfigList(Long id){
        return processNodeConfigRepository.findByConfigParamId(id);
    }

    public void edit(ProcessNodeConfig processNodeConfig){
        Optional<ProcessNodeConfig> optionalProcessNodeConfig = processNodeConfigRepository.findById(processNodeConfig.getId());
        optionalProcessNodeConfig.ifPresent(s->{
            if (StringUtils.isNotBlank(processNodeConfig.getStatus())){
                s.setStatus(processNodeConfig.getStatus());
            }
            if (StringUtils.isNotBlank(processNodeConfig.getProcessCode())){
                s.setProcessCode(processNodeConfig.getProcessCode());
            }
            processNodeConfigRepository.save(s);
        });

    }
}
