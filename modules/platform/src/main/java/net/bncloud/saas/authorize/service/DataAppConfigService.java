package net.bncloud.saas.authorize.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.common.security.data.DataDimension;
import net.bncloud.common.security.data.DataSubjectHolder;
import net.bncloud.saas.authorize.domain.DataAppConfig;
import net.bncloud.saas.authorize.domain.DataSubject;
import net.bncloud.saas.authorize.domain.DataSubjectDimensionRel;
import net.bncloud.saas.authorize.repository.DataAppConfigRepository;
import net.bncloud.saas.authorize.repository.DataDimensionRepository;
import net.bncloud.saas.authorize.repository.DataSubjectDimensionRelRepository;
import net.bncloud.saas.authorize.repository.DataSubjectRepository;
import net.bncloud.saas.authorize.service.query.DataAppConfigQuery;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class DataAppConfigService implements CommandLineRunner {

    private final DataSubjectHolder dataSubjectHolder;

    private final DataAppConfigRepository dataAppConfigRepository;

    private final DataSubjectRepository dataSubjectRepository;

    private final DataSubjectDimensionRelRepository dataSubjectDimensionRelRepository;

    public Object queryAll(DataAppConfigQuery appConfigQuery) {
        return dataAppConfigRepository.findAll();
    }

    public Object queryPage(DataAppConfigQuery appConfigQuery, Pageable pageable) {
        return null;
    }


    @PostConstruct
    public void init(){
        this.loadDataAppConfig();
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Transactional(rollbackFor = Exception.class)
    public void create(DataAppConfig resources) {
        String appCode = resources.getAppCode();
        Optional<DataAppConfig> dataAppConfigOptional = dataAppConfigRepository.findByAppCode(appCode);
        if (dataAppConfigOptional.isPresent()) {
            throw new IllegalArgumentException("应用配置已经存在");// TODO 规范异常
        }
        dataAppConfigRepository.save(resources);
    }


    public void loadDataAppConfig() {
        List<DataAppConfig> dataAppConfigs = dataAppConfigRepository.findAll();
        dataAppConfigs.forEach(app -> {
            String appCode = app.getAppCode();
            List<DataSubject> dataSubjects = dataSubjectRepository.findAllByDataAppConfig_Id(app.getId());
            ArrayList<net.bncloud.common.security.data.DataSubject> securityDataSubjects = Lists.newArrayList();
            if (CollectionUtil.isNotEmpty(dataSubjects)) {
                List<net.bncloud.common.security.data.DataSubject> collect = dataSubjects.stream().map(this::buildDataSubject).collect(Collectors.toList());
                securityDataSubjects.addAll(collect);
            }
            dataSubjectHolder.cache(appCode, securityDataSubjects);
        });

    }

    private net.bncloud.common.security.data.DataSubject buildDataSubject(DataSubject dataSubject) {
        net.bncloud.common.security.data.DataSubject result = new net.bncloud.common.security.data.DataSubject();
        List<DataSubjectDimensionRel> dataSubjectDimensionRels = dataSubjectDimensionRelRepository.findAllByDataSubject_Id(dataSubject.getId());
        List<DataDimension> dimensions = dataSubjectDimensionRels.stream().map(this::buildDataDimension).collect(Collectors.toList());
        result.setId(StrUtil.toString(dataSubject.getId()));
        result.setKey(dataSubject.getKeyValue());
        result.setDimensions(dimensions);
        return result;
    }

    private net.bncloud.common.security.data.DataDimension buildDataDimension(DataSubjectDimensionRel rel) {
        DataDimension dataDimension = new DataDimension();
        dataDimension.setCode(rel.getDimensionCode());
        dataDimension.setAlias(rel.getAlias());
        dataDimension.setUseDefault(false);
        return dataDimension;
    }


}
