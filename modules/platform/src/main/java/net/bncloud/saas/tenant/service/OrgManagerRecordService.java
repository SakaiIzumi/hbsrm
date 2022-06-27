package net.bncloud.saas.tenant.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.MobileUtils;
import net.bncloud.saas.event.CreateOrgManagerRecord;
import net.bncloud.saas.event.DeleteOrgManagerRecord;
import net.bncloud.saas.event.OrgManagerRecordCreateEvent;
import net.bncloud.saas.event.OrgManagerRecordDeleteEvent;
import net.bncloud.saas.tenant.domain.OrgManager;
import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import net.bncloud.saas.tenant.domain.OrganizationRecord;
import net.bncloud.saas.tenant.domain.QOrgManagerRecord;
import net.bncloud.saas.tenant.repository.OrgManagerRecordRepository;
import net.bncloud.saas.tenant.repository.OrgManagerRepository;
import net.bncloud.saas.tenant.service.command.CreatedOrgManagerCommand;
import net.bncloud.saas.tenant.service.dto.OrgManagerRecordDTO;
import net.bncloud.saas.tenant.service.query.OrgManagerQuery;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrgManagerRecordService extends BaseService {
    private final JPAQueryFactory queryFactory;
    private final OrgManagerRecordRepository orgManagerRecordRepository;
    private final OrgManagerRepository orgManagerRepository;


    public OrgManagerRecord findByMobile(String mobile) {
        return orgManagerRecordRepository.findByMobile(mobile).orElse(null);
    }

    public Object allOrgManagerTable(QueryParam<OrgManagerQuery> queryParam, Pageable pageable) {

        QOrgManagerRecord orgManagerRecord = QOrgManagerRecord.orgManagerRecord;

        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        String searchValue = queryParam.getSearchValue();
        if (StrUtil.isNotBlank(searchValue)) {
            searchValue = "%" + searchValue + "%";
            predicate = ExpressionUtils.or(predicate, orgManagerRecord.code.like(searchValue));
            predicate = ExpressionUtils.or(predicate, orgManagerRecord.mobile.like(searchValue));
            predicate = ExpressionUtils.or(predicate, orgManagerRecord.user.name.like(searchValue));
        }

        QueryResults<OrgManagerRecord> queryResults = queryFactory.select(orgManagerRecord).from(orgManagerRecord).offset(pageable.getOffset())
                .where(predicate)
                .limit(pageable.getPageSize())
                .fetchResults();
        List<OrgManagerRecord> results = queryResults.getResults();
        if (CollectionUtil.isNotEmpty(results)) {
            List<OrgManagerRecordDTO> collect = results.stream().map(manager -> {
                List<OrganizationRecord> organizationRecords = manager.getOrganizationRecords();
                List<String> orgNames = organizationRecords.stream().map(OrganizationRecord::getOrgName).collect(Collectors.toList());
                return OrgManagerRecordDTO.builder()
                        .id(manager.getUser().getId())
                        .name(manager.getUser().getName())
                        .code(manager.getCode())
                        .mobile(manager.getMobile())
                        .managerId(manager.getId())
                        .managerType(manager.getManagerType())
                        .enabled(manager.getEnabled())
                        .orgNames(orgNames)
                        .build();
            }).collect(Collectors.toList());
            return new PageImpl<>(collect, pageable, queryResults.getTotal());
        }
        return PageUtils.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(OrgManagerRecord orgManagerRecord) {
        orgManagerRecordRepository.save(orgManagerRecord);
    }

    /**
     * 创建协助组织管理员记录
     *
     * @param command
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrgManager(CreatedOrgManagerCommand command) {
        if (!MobileUtils.checkPhone(command.getMobile())) {
            throw new ApiException(400, "手机号码格式有误!");
        }
        OrgManagerRecord orgManagerRecord = this.findByMobile(command.getMobile());
        if (orgManagerRecord != null) {
            throw new ApiException(400, "管理员已存在!");
        }
        applicationEventPublisher.publishEvent(new OrgManagerRecordCreateEvent(this, CreateOrgManagerRecord.of(command.getName(), command.getMobile())));
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteOrganizationManager(Long managerId) {
        Optional<OrgManagerRecord> orgManagerRecordOptional = orgManagerRecordRepository.findById(managerId);
        if (orgManagerRecordOptional.isPresent()) {
            OrgManagerRecord orgManagerRecord = orgManagerRecordOptional.get();
            // 删除所有组织管理员的权限
            Long userId = orgManagerRecord.getUser().getId();
            List<OrgManager> orgManagers = orgManagerRepository.findAllByUser_Id(userId);
            if (CollectionUtil.isNotEmpty(orgManagers)) {
                throw new ApiException(403, "禁止活动状态的组织管理员");
            }
            orgManagerRecordRepository.delete(orgManagerRecord);
            applicationEventPublisher.publishEvent(new OrgManagerRecordDeleteEvent(this, DeleteOrgManagerRecord.of(userId)));
        }
    }
}
