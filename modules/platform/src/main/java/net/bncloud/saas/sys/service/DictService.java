package net.bncloud.saas.sys.service;

import net.bncloud.saas.sys.domain.Dict;
import net.bncloud.saas.sys.repository.DictRepository;
import net.bncloud.saas.sys.service.dto.DictDTO;
import net.bncloud.saas.sys.service.mapstruct.DictMapper;
import net.bncloud.saas.sys.service.query.DictQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DictService {

    private final DictRepository dictRepository;
    private final DictMapper dictMapper;

    public DictService(DictRepository dictRepository, DictMapper dictMapper) {
        this.dictRepository = dictRepository;
        this.dictMapper = dictMapper;
    }

    public Page<DictDTO> pageQuery(DictQuery dict, Pageable pageable){
        Page<Dict> page = dictRepository.findAll(buildDictQuerySpecification(dict), pageable);
        System.out.println(page.getContent());
        return dictRepository.findAll(buildDictQuerySpecification(dict), pageable)
               .map(dictMapper::toDto);
    }
    public DictDTO getById(String id){
        return dictRepository.findById(id).map(dictMapper::toDto).get();
    }
    public List<DictDTO> findAll(DictQuery dict) {
        List<Dict> list = dictRepository.findAll(buildDictQuerySpecification(dict));
        return dictMapper.toDto(list);
    }

    private Specification<Dict> buildDictQuerySpecification(DictQuery dict) {
        return new Specification<Dict>() {
            private static final long serialVersionUID = 6100258546575874666L;

            @Override
            public Predicate toPredicate(Root<Dict> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (dict == null) {
                    return predicate;
                }
                if (StringUtils.isNotBlank(dict.getCode())) {
                    predicate.getExpressions().add(cb.equal(root.get("code"), dict.getCode()));
                }
                if (StringUtils.isNotBlank(dict.getDescription())) {
                    predicate.getExpressions().add(cb.like(root.get("description"), "%" + StringUtils.trim(dict.getDescription()) + "%"));
                }
                if (StringUtils.isNotBlank(dict.getQs())) {
                    List<Predicate> orList = new ArrayList<>();
                    orList.add(cb.like(root.get("code"), "%" + dict.getQs() + "%"));
                    orList.add(cb.like(root.get("description"), "%" + dict.getQs() + "%"));
                    predicate.getExpressions().add(cb.or(orList.toArray(new Predicate[0])));
                }
                return predicate;
            }
        };
    }

    public DictDTO getDictByCode(String code){return dictMapper.toDto(dictRepository.findByCode(code));}

    @Transactional(rollbackFor = Exception.class)
    public void create(Dict resources) {
        Optional<Dict> dictOptional = dictRepository.findById(resources.getCode());
        if (dictOptional.isPresent()) {
            throw new IllegalArgumentException("字典" + resources.getCode() + " 已存在"); //TODO
        }
        dictRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        if (!dictRepository.findById(resources.getCode()).isPresent()) {
            throw new IllegalArgumentException("字典" + resources.getCode() + "不存在");
        }
        dictRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<String> codes) {
        dictRepository.deleteByCodeIn(codes);
    }

    public DictDTO findByCode(String code) {
        return dictRepository.findById(code).map(dictMapper::toDto).orElse(null);
    }
}
