package net.bncloud.saas.authorize.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.repository.DataSubjectRepository;
import net.bncloud.saas.authorize.service.query.DataSubjectQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DataSubjectService {

    private DataSubjectRepository dataSubjectRepository;

    public Object queryPage(DataSubjectQuery query, Pageable pageable) {
        return dataSubjectRepository.findAll();
    }

    private Object get(Long id) {
        return dataSubjectRepository.getOne(id);
    }

    public Object queryAll(DataSubjectQuery query) {
        return dataSubjectRepository.findAll();
    }
}
