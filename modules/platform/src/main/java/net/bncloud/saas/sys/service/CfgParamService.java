package net.bncloud.saas.sys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.bncloud.saas.sys.domain.CfgParam;
import net.bncloud.saas.sys.repository.CfgParamRepository;
import net.bncloud.service.api.platform.sys.dto.MrpCfgDTO;
import net.bncloud.service.api.platform.sys.dto.SetSupplierConfigDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CfgParamService {

    private final CfgParamRepository cfgParamRepository;

    public CfgParamService(CfgParamRepository cfgParamRepository) {
        this.cfgParamRepository = cfgParamRepository;
    }

    @Transactional(readOnly = true)
    public List<CfgParam> gatCompanyAllParam(Long companyId) {
        return cfgParamRepository.findAllByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<CfgParam> gatAllParamByOrgId(Long orgId) {
        return cfgParamRepository.findAllByOrgId(orgId);
    }

    @Transactional
    public void save(CfgParam param) {
        cfgParamRepository.findById(param.getId()).ifPresent(exist -> {
            exist.setValue(param.getValue());
            cfgParamRepository.save(exist);
        });
    }

    @Transactional
    public void create(CfgParam param) {
        cfgParamRepository.save(param);
    }

    @Transactional
    public void createAll(List<CfgParam> params) {
        cfgParamRepository.saveAll(params);
    }


    public CfgParam getParamByCompanyIdAndCode(Long companyId, String code) {
        return cfgParamRepository.findOneByCompanyIdAndCode(companyId, code).orElse(null);
    }

    public CfgParam getParamByCode(String code) {
        return cfgParamRepository.findFirstByCode(code).orElse(null);
    }

    public List<CfgParam> findListByCode(String code) {

        return cfgParamRepository.findAllByCode(code);
    }

    public CfgParam findFirstByCodeAndOrgId(String code, Long orgId) {
        return cfgParamRepository.findFirstByCodeAndOrgId(code,orgId);
    }

    @Transactional
    public void saveConfig(SetSupplierConfigDTO dto) {
        CfgParam cfgParam = cfgParamRepository.findFirstByCode(dto.getCode()).orElse(null);
        String config = cfgParam.getValue();
        JSONObject jsonObject = JSON.parseObject(config);
        jsonObject.put(dto.getSupplierCode(),dto.getOnOff().toString());
        /*JSONArray sup = jsonObject.getJSONArray("sup");

        JSONObject jsonObjectForUpdate = new JSONObject();
        jsonObjectForUpdate.put(dto.getSupplierCode(),"true");
        sup.set(sup.size(),jsonObjectForUpdate);
        jsonObject.put("sup",sup);*/
        String s = JSON.toJSONString(jsonObject);
        cfgParamRepository.updateByCode(dto.getCode(),s);
    }

    public MrpCfgDTO getAllSubscribeConfig() {
        String configFirst="mrp:default_workday";
        String configSecond="mrp:auto_holiday";
        CfgParam defautlWorkday = cfgParamRepository.findFirstByCode(configFirst).orElse(null);
        CfgParam auto = cfgParamRepository.findFirstByCode(configSecond).orElse(null);
        String defautlWorkdayValue = defautlWorkday.getValue();
        String autoValue = auto.getValue();
        MrpCfgDTO mrpCfgParam = new MrpCfgDTO();
        mrpCfgParam.setAutoScribe(autoValue);
        mrpCfgParam.setDefaultWorkday(defautlWorkdayValue);
        return mrpCfgParam;



    }
}
