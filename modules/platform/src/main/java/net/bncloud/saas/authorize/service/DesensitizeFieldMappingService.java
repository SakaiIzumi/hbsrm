package net.bncloud.saas.authorize.service;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.security.data.DataDesensitizeField;
import net.bncloud.common.security.data.DesensitizeFieldHolder;
import net.bncloud.saas.authorize.domain.DataAppConfig;
import net.bncloud.saas.authorize.domain.DataSubject;
import net.bncloud.saas.authorize.domain.DesensitizeFieldMapping;
import net.bncloud.saas.authorize.repository.DataAppConfigRepository;
import net.bncloud.saas.authorize.repository.DataSubjectRepository;
import net.bncloud.saas.authorize.repository.DesensitizeFieldMappingRepository;
import net.bncloud.saas.authorize.repository.DesensitizeRuleRepository;
import net.bncloud.saas.config.DataSecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DesensitizeFieldMappingService {
    private final DesensitizeFieldHolder desensitizeFieldHolder;
    private final DataSubjectRepository dataSubjectRepository;
    private final DesensitizeFieldMappingRepository desensitizeFieldMappingRepository;
    private final DataAppConfigRepository dataAppConfigRepository;
    private final DesensitizeRuleRepository desensitizeRuleRepository;

    @PostConstruct
    public void init() {
        this.load();
    }

    public void load() {
        List<DataAppConfig> dataAppConfigs = dataAppConfigRepository.findAll();
        for (DataAppConfig app : dataAppConfigs) {
            List<DataSubject> dataSubjects = dataSubjectRepository.findAllByDataAppConfig_Id(app.getId());
            for (DataSubject dataSubject : dataSubjects) {
                List<DesensitizeFieldMapping> fieldMappingList = desensitizeFieldMappingRepository.findAllByDataSubject_Id(dataSubject.getId());
                if (CollectionUtil.isNotEmpty(fieldMappingList)) {
                    List<DataDesensitizeField> dataDesensitizeFields = fieldMappingList.stream().map(f -> DataDesensitizeField.builder()
                            .dimensionCode(f.getDimensionCode())
                            .subjectId(f.getDataSubject().getId().toString())
                            .pattern(f.getDesensitizeRule().getRulePattern())
                            .build()).collect(Collectors.toList());
                    desensitizeFieldHolder.cache(app.getAppCode(),dataDesensitizeFields);
                }
            }
        }
    }
}
