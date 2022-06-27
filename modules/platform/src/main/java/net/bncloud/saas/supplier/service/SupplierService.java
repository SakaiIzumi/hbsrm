package net.bncloud.saas.supplier.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.bis.service.api.feign.SupplierFeignClient;
import net.bncloud.common.api.IResultCode;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.CheckException;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.saas.authorize.domain.QRole;
import net.bncloud.saas.event.CreateSupplierOpsLog;
import net.bncloud.saas.event.SupplierCreatedEvent;
import net.bncloud.saas.event.SupplierOpsLogCreatedEvent;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.domain.QPurchaser;
import net.bncloud.saas.supplier.domain.*;
import net.bncloud.saas.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.saas.supplier.enums.SupplierRelevanceStatusOperateRel;
import net.bncloud.saas.supplier.repository.*;
import net.bncloud.saas.supplier.service.command.CreateSupplierCommand;
import net.bncloud.saas.supplier.service.command.ManualSyncCommand;
import net.bncloud.saas.supplier.service.dto.SupplierArchiveDTO;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.supplier.service.mapper.SupplierArchiveMapper;
import net.bncloud.saas.supplier.service.mapper.SupplierMapper;
import net.bncloud.saas.supplier.service.query.SupplierQuery;
import net.bncloud.saas.supplier.web.vm.SupplierTags;
import net.bncloud.saas.supplier.web.vm.SupplierTypes;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.QOrgEmployee;
import net.bncloud.saas.tenant.domain.QOrganization;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.utils.pageable.PageUtils;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierService implements ApplicationEventPublisherAware {
    private final SupplierFeignClient supplierFeignClient;
    private final JPAQueryFactory queryFactory;
    private ApplicationEventPublisher applicationEventPublisher;
    private final SupplierRepository supplierRepository;
    private final SupplierExtRepository supplierExtRepository;
    private final TagRepository tagRepository;
    private final TagConfigItemRepository tagConfigItemRepository;
    private final TypeRepository typeRepository;
    private final TypeConfigItemRepository typeConfigItemRepository;

    private final SupplierMapper supplierMapper;
    private final SupplierArchiveMapper supplierArchiveMapper;
    private final OrganizationRepository organizationRepository;

    private final QSupplier qSupplier = QSupplier.supplier;

    @Transactional(rollbackFor = Exception.class)
    public Supplier createSupplier(CreateSupplierCommand command) {
        net.bncloud.common.security.Org currOrg = SecurityUtils.getCurrentOrg().orElseThrow(() -> new ApiException(404, "查无该组织"));
        Organization organization = organizationRepository.findById(currOrg.getId()).orElseThrow(() -> new ApiException(404, "查无该组织"));
        Supplier supplierCommand = command.createSupplier();
        Optional<Supplier> supplierOptional = supplierRepository.findOneByCodeOrName(supplierCommand.getCode(), supplierCommand.getName());
        Supplier supplier;
        if (supplierOptional.isPresent()) {
            supplier = supplierOptional.get();
            List<Organization> organizations = supplier.getOrganizations();
            if (organizations.stream().map(Organization::getId).anyMatch(id -> id.equals(currOrg.getId()))) {
                throw new IllegalArgumentException("供应商信息已存在");
            } else {
                organizations = Lists.newArrayList(organization);
                supplier.setOrganizations(organizations);
                supplierRepository.save(supplier);
            }
        } else {
            supplierCommand.setRelevanceStatus(SupplierRelevanceStatusEnum.RELEVANCE.getCode());
            List<Organization> organizations = Lists.newArrayList(organization);
            supplierCommand.setOrganizations(organizations);
            supplier = supplierRepository.save(supplierCommand);
            SupplierExt supplierExt = new SupplierExt();
            supplierExt.setSupplier(supplier);
            SupplierExt savedSupplierExt = supplierExtRepository.save(supplierExt);
            supplier.setSupplierExt(savedSupplierExt);
        }
        applicationEventPublisher.publishEvent(new SupplierCreatedEvent(this, supplier));
        return supplier;
    }


    /**
     * 供应商列表分页查询
     */
    public Page<SupplierDTO> pageQuery(SupplierQuery param, Pageable pageable) {
        return supplierRepository.findAll((Specification<Supplier>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getSupplierCode())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("code"), "%" + param.getSupplierCode() + "%"));
            }
            if (StringUtils.isNotBlank(param.getSupplierName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getSupplierName() + "%"));
            }
            if (CollectionUtil.isNotEmpty(param.getSupplierTagsList())) {
                Join<Supplier, Tag> join = root.join("tag", JoinType.LEFT);
                predicate.getExpressions().add(criteriaBuilder.in(join.get("tags")).value(param.getSupplierTagsList()));
            }
            if (CollectionUtil.isNotEmpty(param.getSupplierTypesList())) {
                Join<Supplier, Type> join = root.join("type", JoinType.LEFT);
                predicate.getExpressions().add(criteriaBuilder.in(join.get("types")).value(param.getSupplierTypesList()));
            }
            if (param.getInviteEndDate() != null && param.getInviteStartDate() != null) {
                predicate.getExpressions().add(criteriaBuilder.between(root.<Date>get("inviteDate"), param.getInviteEndDate(), param.getInviteStartDate()));
            }
            if (StringUtils.isBlank(param.getSupplierCode()) && StringUtils.isBlank(param.getSupplierName()) && StringUtils.isNotBlank(param.getQs())) {
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(root.get("code"), "%" + param.getQs() + "%"),
                        criteriaBuilder.like(root.get("name"), "%" + param.getQs() + "%"));
                return or;
            }
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return predicate;
        }, pageable).map(supplierMapper::toDto).map(supplierDto -> {
            supplierDto.setPermissionButton(SupplierRelevanceStatusOperateRel.operations(supplierDto.getRelevanceStatus()));
            return supplierDto;
        });
    }


    /**
     * 供应商列表查询
     */
    public List<SupplierDTO> allQuery(SupplierQuery param) {
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        String qs = param.getQs();
        if (StrUtil.isNotEmpty(qs)) {
            predicate = ExpressionUtils.or(predicate, qSupplier.code.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qSupplier.name.like("%" + qs + "%"));
        }
        Long orgId = param.getOrgId();
        if (orgId != null) {
            Organization organization = new Organization();
            organization.setId(param.getOrgId());
            qSupplier.organizations.contains(organization);
        }

        return queryFactory.select(
                Projections.bean(
                        SupplierDTO.class,
                        qSupplier.id,
                        qSupplier.code,
                        qSupplier.name
                )
        ).from(qSupplier).where(predicate).fetch();
    }


    /**
     * 获取供应商详情
     *
     * @param id
     * @return
     */
    public SupplierDTO getById(Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);
        supplierDTO.setAccounts(supplier.getAccounts());
        supplierDTO.setPermissionButton(SupplierRelevanceStatusOperateRel.operations(supplier.getRelevanceStatus()));
        return supplierDTO;
    }

    /**
     * 获取供应商详情根据编号
     *
     * @param code
     * @return
     */
    public Supplier getByCode(String code) {
        Optional<Supplier> supplier = supplierRepository.findOneByCode(code);

        return supplier.orElse(null);
    }

    /**
     * 获取供应商的对账信息
     * @param code 供应商的编码
     * @return 供应商的对账信息
     */
    public SupplierExt queryFinancialInfoOfSupplier(String code) {
        Optional<SupplierExt> supplierExt = supplierRepository.queryFinancialInfoOfSupplier(code);
        return supplierExt.orElse(new SupplierExt());
    }

    /**
     * 获取供应商的银行信息
     * @param code 供应商的编码
     * @return 供应商所有银行的信息
     */
    public List<SupplierAccount> querySupplierAccountInfo(String code) {
        List<SupplierAccount> accounts = supplierRepository.querySupplierAccountInfo(code);
        return accounts;
    }

    /**
     * 获取供应商信息接口
     * @param id 供应商id
     * @return
     */
    public SuppliersDTO querySupplierInformation(Long id) {
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();

        if (id != null) {
            predicate = ExpressionUtils.or(predicate, qSupplier.id.eq(id));
        }

        return queryFactory.select(
                Projections.bean(
                        SuppliersDTO.class,
                        qSupplier.id,
                        qSupplier.code,
                        qSupplier.name,
                        qSupplier.managerMobile,
                        qSupplier.managerName
                )
        ).from(qSupplier).where(predicate).fetchOne();
    }

    /**
     * 获取所有供应商信息
     */
    public List<SuppliersDTO> getSupplierInfoAll() {
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        return queryFactory.select(
                Projections.bean(
                        SuppliersDTO.class,
                        qSupplier.id,
                        qSupplier.code,
                        qSupplier.name,
                        qSupplier.managerMobile,
                        qSupplier.managerName
                )
        ).from(qSupplier).where(predicate).fetch();
    }

    /**
     * 获取所有标签
     *
     * @return
     */
    public List<Tag> getAllTag() {
        Optional<net.bncloud.common.security.Org> orgOptional = SecurityUtils.getCurrentOrg();
        if (orgOptional.isPresent()) {
            return tagRepository.findAllByOrgId(orgOptional.get().getId());
        }
        return Lists.newArrayList();
    }

    /**
     * 获取所有类型
     *
     * @return
     */
    public List<Type> getAllType() {
        Optional<net.bncloud.common.security.Org> orgOptional = SecurityUtils.getCurrentOrg();
        if (orgOptional.isPresent()) {
            return typeRepository.findAllByOrgId(orgOptional.get().getId());
        }
        return Lists.newArrayList();
    }

    /**
     * 关联状态
     *
     * @param id
     */
    public void relevance(Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setRelevanceStatus(SupplierRelevanceStatusEnum.RELEVANCE.getCode());
        supplierRepository.save(supplier);
        publishOpsEvent(supplier.getId(), SupplierRelevanceStatusEnum.RELEVANCE.getName(), "");
    }

    /**
     * 暂停合作状态
     *
     * @param id
     */
    public void suspendCooperation(Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setRelevanceStatus(SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getCode());
        supplierRepository.save(supplier);
        publishOpsEvent(supplier.getId(), SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getName(), "");
    }

    /**
     * 冻结
     *
     * @param id
     */
    public void frozen(Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setRelevanceStatus(SupplierRelevanceStatusEnum.FROZEN.getCode());
        supplierRepository.save(supplier);
        publishOpsEvent(supplier.getId(), SupplierRelevanceStatusEnum.FROZEN.getName(), "");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveSupplierTags(SupplierTags supplierTags) {
        Long supplierId = supplierTags.getSupplierId();
        supplierRepository.findById(supplierId).ifPresent(supplier -> {
            String tagNames = null;
            List<SupplierTag> supplierTagList = supplierTags.getSupplierTagList();
            if (CollectionUtil.isNotEmpty(supplierTagList)) {
                List<TagConfigItem> tags = supplier.getTags();
                if (CollectionUtil.isNotEmpty(tags)) {
                    supplier.getTags().removeAll(tags);
                }
                List<Long> tagIds = supplierTagList.stream().map(SupplierTag::getTagId).collect(Collectors.toList());
                List<TagConfigItem> tagConfigItems = tagConfigItemRepository.findAllById(tagIds);
                supplier.setTags(tagConfigItems);
                tagNames = tagConfigItems.stream().map(TagConfigItem::getItem).collect(Collectors.joining(","));
                publishOpsEvent(supplier.getId(), tagNames, "");
            } else {
                List<TagConfigItem> tags = supplier.getTags();
                tagNames = tags.stream().map(TagConfigItem::getItem).collect(Collectors.joining(","));
                supplier.getTags().removeAll(tags);
                publishOpsEvent(supplier.getId(), "清除标签:" + tagNames, "");
            }
            supplierRepository.save(supplier);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSupplierTypes(SupplierTypes supplierTypes) {
        Long supplierId = supplierTypes.getSupplierId();
        supplierRepository.findById(supplierId).ifPresent(supplier -> {
            String typeNames = null;
            List<SupplierType> supplierTypeList = supplierTypes.getSupplierTypeList();
            if (CollectionUtil.isNotEmpty(supplierTypeList)) {
                List<TypeConfigItem> configItems = supplier.getTypes();
                if (CollectionUtil.isNotEmpty(configItems)) {
                    supplier.getTypes().removeAll(configItems);
                }
                List<Long> typeIds = supplierTypeList.stream().map(SupplierType::getTypeId).collect(Collectors.toList());
                List<TypeConfigItem> typeConfigItems = typeConfigItemRepository.findAllById(typeIds);
                supplier.setTypes(typeConfigItems);
                //变更 类型 typeConfigItems
                typeNames = typeConfigItems.stream().map(TypeConfigItem::getItem).collect(Collectors.joining(","));
                publishOpsEvent(supplier.getId(), typeNames, "");
            } else {
                List<TypeConfigItem> types = supplier.getTypes();
                supplier.getTypes().removeAll(types);
                typeNames = types.stream().map(TypeConfigItem::getItem).collect(Collectors.joining(","));
                publishOpsEvent(supplier.getId(), "清除类型:" + typeNames, "");
            }
            supplierRepository.save(supplier);
        });
    }


    public Page<SupplierArchiveDTO> pageQuery(QueryParam<SupplierQuery> queryParam, Pageable pageable) {
        String searchValue = queryParam.getSearchValue();
        SupplierQuery param = queryParam.getParam();
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        QTagConfigItem qTag = QTagConfigItem.tagConfigItem;
        QTypeConfigItem qTypeConfigItem = QTypeConfigItem.typeConfigItem;
        boolean hasTagIds = false;
        boolean hasTypeIds = false;
        boolean hasOrgId = false;
        boolean hasPurId = false;

        if (StrUtil.isNotEmpty(searchValue)) {
            predicate = ExpressionUtils.or(predicate, qSupplier.code.like("%" + searchValue + "%"));
            predicate = ExpressionUtils.or(predicate, qSupplier.name.like("%" + searchValue + "%"));
        } else {
            if (!Objects.isNull(param)) {
                predicate = StrUtil.isBlank(param.getSupplierName()) ? predicate : ExpressionUtils.and(predicate, qSupplier.name.like("%" + param.getSupplierName() + "%"));

                //合作中的供应商
                if (ObjectUtil.isNotEmpty(param.getRelevanceStatus())){
                    predicate = ExpressionUtils.and(predicate, qSupplier.relevanceStatus.eq(param.getRelevanceStatus()));
                }

                if (CollectionUtil.isNotEmpty(param.getTagIds())) {
                    hasTagIds = true;
                }
                if (CollectionUtil.isNotEmpty(param.getTypeIds())) {
                    hasTypeIds = true;
                }
                predicate = !hasTagIds ? predicate : ExpressionUtils.and(predicate, qTag.id.in(param.getTagIds()));
                predicate = !hasTypeIds ? predicate : ExpressionUtils.and(predicate, qTypeConfigItem.id.in(param.getTypeIds()));

                if (ObjectUtil.isNotEmpty(param.getOrgId())) {
                    hasOrgId = true;
                }
                if (ObjectUtil.isNotEmpty(param.getPurId())) {
                    hasPurId = true;
                }
                predicate = !hasOrgId ? predicate : ExpressionUtils.and(predicate, qSupplier.organizations.contains(new Organization(param.getOrgId())));
                predicate = !hasPurId ? predicate : ExpressionUtils.and(predicate, qSupplier.purchasers.contains(Purchaser.of(param.getPurId())));


            }
        }
        QueryResults<Supplier> supplierQueryResults;
        JPAQuery<Supplier> fromSupplier = queryFactory.select(qSupplier).from(qSupplier);
        if (hasTagIds) {
            fromSupplier = fromSupplier.innerJoin(qSupplier.tags, qTag);
        }

        if (hasTypeIds) {
            fromSupplier = fromSupplier.innerJoin(qSupplier.types, qTypeConfigItem);
        }

        if (hasOrgId) {
            QOrganization qOrganization = QOrganization.organization;
            fromSupplier = fromSupplier.innerJoin(qSupplier.organizations, qOrganization);
        }

        if (hasPurId) {
            QPurchaser qPurchaser = QPurchaser.purchaser;
            fromSupplier = fromSupplier.innerJoin(qSupplier.purchasers, qPurchaser);
        }
        supplierQueryResults = fromSupplier.where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetchResults();
        List<Supplier> results = supplierQueryResults.getResults();
        if (CollectionUtil.isNotEmpty(results)) {
            List<SupplierArchiveDTO> supplierDTOS = supplierArchiveMapper.toDto(results);
            supplierDTOS.forEach(supplierArchiveDTO -> {
                supplierArchiveDTO.setPermissionButton(SupplierRelevanceStatusOperateRel.operations(supplierArchiveDTO.getRelevanceStatus()));
            });
            return PageUtils.of(supplierDTOS, pageable, supplierQueryResults.getTotal());
        }
        return (Page<SupplierArchiveDTO>) PageUtils.empty();
    }

    public R<Void> checkSupplier(Supplier supplier) {
        Optional<Supplier> exist = null;
        net.bncloud.common.security.Org currOrg = SecurityUtils.getCurrentOrg().orElseThrow(() -> new ApiException(404, "查无该组织"));

        exist = supplierRepository.findOneByCodeOrName(supplier.getCode(), supplier.getName());

        if (exist.isPresent()) {
            Supplier checkSupplier = exist.get();
            List<Organization> organizations = checkSupplier.getOrganizations();
            if (checkSupplier != null) {
                if (checkSupplier.getName().equals(supplier.getName())) {

                    if (organizations.stream().map(Organization::getId).anyMatch(id -> id.equals(currOrg.getId()))) {
                        throw new CheckException(new IResultCode() {
                            @Override
                            public String getMessage() {
                                return "供应商" + checkSupplier.getName() + "已存在";
                            }

                            @Override
                            public int getCode() {
                                return ResultCode.SUCCESS.getCode();
                            }

                        }, false);
                    }
                } else if (checkSupplier.getCode().equals(supplier.getCode())) {


                    if (organizations.stream().map(Organization::getId).anyMatch(id -> id.equals(currOrg.getId()))) {

                        throw new CheckException(new IResultCode() {
                            @Override
                            public String getMessage() {
                                return "供应商" + checkSupplier.getCode() + "已存在";
                            }

                            @Override
                            public int getCode() {
                                return ResultCode.SUCCESS.getCode();
                            }
                        }, false);
                    }
                }

            }


        }

        return R.success("检验合法合法");
    }

    public void uploadAvatarUrl(Supplier supplier) {
        if (supplier.getId() == null) {
            throw new IllegalArgumentException("供应商参数缺失");
        }
        supplierRepository.findById(supplier.getId()).ifPresent(supplierExist -> {
            supplierExist.setAvatarUrl(supplier.getAvatarUrl());
            supplierRepository.save(supplierExist);
        });
    }

    public List<Supplier> queryRelateSuppliers() {
        List<Supplier> suppliers = Lists.newArrayList();
        SecurityUtils.getCurrentOrg().ifPresent(organization -> {
            QSupplier qSupplier = QSupplier.supplier;
            Organization org = Organization.of(organization.getId());
            List<Supplier> fetch = queryFactory.select(qSupplier)
                    .from(qSupplier)
                    .where(qSupplier.organizations.contains(org)).fetch();
            suppliers.addAll(fetch);
        });
        return suppliers;
    }

    public List<Supplier> queryRelateSuppliersByName(String supplierName) {
        List<Supplier> suppliers = new ArrayList<>();
        SecurityUtils.getCurrentOrg().ifPresent(organization -> {
            QSupplier qSupplier = QSupplier.supplier;
            Organization org = Organization.of(organization.getId());
            ArrayList<BooleanExpression> wherePredicate = Lists.newArrayList();
            wherePredicate.add(qSupplier.organizations.contains(org));
            if (StringUtil.isNotBlank(supplierName)){
                wherePredicate.add(qSupplier.name.like("%"+supplierName+"%"));
            }
            BooleanExpression[] expressions = wherePredicate.toArray(new BooleanExpression[0]);
            List<Supplier> fetch = queryFactory.select(qSupplier)
                    .from(qSupplier)
                    .where(expressions)
                    .fetch();
            suppliers.addAll(fetch);
        });
        return suppliers;
    }

    public List<Supplier> findAllById(List<Long> ids) {
        return supplierRepository.findAllById(ids);
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public List<Supplier> findAllByIdsAndOrgId(List<Long> ids, Long orgId) {
        return queryFactory.select(qSupplier)
                .from(qSupplier)
                .where(qSupplier.organizations.contains(Organization.of(orgId)).and(qSupplier.id.in(ids))).fetch();
    }


    public List<SupplierStaff> getRoleBySupplier(Supplier supplier, List<Long> roleIdList) {
        QSupplierStaff supplierStaff = QSupplierStaff.supplierStaff;
        QRole role = QRole.role;
        List<SupplierStaff> supplierStaffs = queryFactory.select(supplierStaff)
                .from(supplierStaff).innerJoin(supplierStaff.roles, role)
                .where(role.id.in(roleIdList).and(supplierStaff.supplier.code.eq(supplier.getCode()))).distinct().fetch();
        return supplierStaffs;
    }

    public List<OrgEmployee> getRoleByOrganization(Organization organization, List<Long> roleIdList) {
        QOrgEmployee orgEmployee = QOrgEmployee.orgEmployee;
        QRole role = QRole.role;
        return queryFactory.select(orgEmployee)
                .from(orgEmployee).innerJoin(orgEmployee.roles, role)
                .where(role.id.in(roleIdList).and(orgEmployee.org.id.eq(organization.getId()))).distinct().fetch();
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<Supplier> suppliers) {
        supplierRepository.saveAll(suppliers);
    }

    public void flush() {
        supplierRepository.flush();
    }


    public void disable(Long id) {
    }

    public void enable(Long id) {
    }

    private void publishOpsEvent(Long supplierId, String content, String remark) {
        applicationEventPublisher.publishEvent(new SupplierOpsLogCreatedEvent(this, CreateSupplierOpsLog.of(supplierId, content, remark, SecurityUtils.getCurrentUser().get().getName())));
    }


    public void manualSyncCommand(ManualSyncCommand manualSyncCommand) {
        supplierFeignClient.syncSupplierInfoWithTable(manualSyncCommand.getIds());
    }


}
