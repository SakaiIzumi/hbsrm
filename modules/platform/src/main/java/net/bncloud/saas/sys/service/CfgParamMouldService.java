package net.bncloud.saas.sys.service;

import lombok.AllArgsConstructor;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.saas.sys.domain.CfgParamMould;
import net.bncloud.saas.sys.repository.CfgParamMouldRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CfgParamMouldService extends BaseService {

    private final CfgParamMouldRepository cfgParamMouldRepository;

    public List<CfgParamMould> queryAll() {
        return cfgParamMouldRepository.findAll();
    }


}
