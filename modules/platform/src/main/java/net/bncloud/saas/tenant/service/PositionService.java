package net.bncloud.saas.tenant.service;

import net.bncloud.saas.tenant.domain.Position;
import net.bncloud.saas.tenant.repository.PositionRepository;
import net.bncloud.saas.tenant.service.dto.PositionDTO;
import net.bncloud.saas.tenant.service.query.PositionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Page<Position> pageQuery(PositionQuery param, Pageable pageable) {
        return positionRepository.findAll((Specification<Position>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            return predicate;
        }, pageable);
    }

    public void save(PositionDTO dto) {
        if (dto.getId() != null) {
            positionRepository.findById(dto.getId()).ifPresent(post -> {
                post.setName(dto.getName());
                if (dto.getDisabled() != null) {
                    post.setDisabled(dto.getDisabled());
                }
                if (dto.getOrder() != null) {
                    post.setOrder(dto.getOrder());
                }
                positionRepository.save(post);
            });
        } else {
            final Position position = dto.create();
            positionRepository.save(position);
        }
    }

    public Optional<Position> findById(Long id) {
        return positionRepository.findById(id);
    }

    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    public void switchStatus(Long id) {
        positionRepository.findById(id).ifPresent(p -> {
            p.setDisabled(!p.isDisabled());
            positionRepository.save(p);
        });
    }
}
