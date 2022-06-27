package net.bncloud.saas.authorize.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.repository.DataSubjectDimensionRelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DataSubjectDimensionRelService {


    private final DataSubjectDimensionRelRepository dataSubjectDimensionRelRepository;

    private Object queryAll() {
        return dataSubjectDimensionRelRepository.findAll();
    }



}
