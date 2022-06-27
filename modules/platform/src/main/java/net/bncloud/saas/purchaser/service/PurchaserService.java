package net.bncloud.saas.purchaser.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.event.*;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.domain.QPurchaser;
import net.bncloud.saas.purchaser.repository.PurchaserRepository;
import net.bncloud.saas.purchaser.service.command.*;
import net.bncloud.saas.purchaser.service.dto.PurchaserDTO;
import net.bncloud.saas.purchaser.service.mapstruct.PurchaserMapper;
import net.bncloud.saas.purchaser.service.query.PurchaserQuery;
import net.bncloud.saas.purchaser.service.query.PurchaserSmallQuery;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.supplier.service.mapper.SupplierMapper;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class PurchaserService extends BaseService {
    private final JPAQueryFactory queryFactory;
    private final PurchaserRepository purchaserRepository;
    private final PurchaserMapper purchaserMapper;
    private final SupplierMapper supplierMapper;
    private final QPurchaser qPurchaser = QPurchaser.purchaser;

    @Transactional(rollbackFor = Exception.class)
    public void create(CreatePurchaserCommand command) {
        SecurityUtils.getCurrentOrg().ifPresent(org -> {
            Long orgId = org.getId();
            Optional<Purchaser> purchaserOptional = purchaserRepository.findByCode(command.getCode());
            if (purchaserOptional.isPresent()) {
                throw new ApiException(400, "采购方已经存在");
            }
            applicationEventPublisher.publishEvent(new PurchaserCreatedEvent(this, CreatePurchaser.of(command.getCode(), command.getName(), command.getArtificialPerson(), command.getDescription(), command.getEnabled(), orgId)));
        });
    }


    public Page page(QueryParam<PurchaserQuery> queryParam, Pageable pageable) {
        String searchValue = queryParam.getSearchValue();
        PurchaserQuery param = queryParam.getParam();
        Predicate predicate = ExpressionUtils.anyOf();

        if (StrUtil.isNotBlank(searchValue)) {
            predicate = ExpressionUtils.or(predicate, qPurchaser.code.like("%" + searchValue + "%"));
            predicate = ExpressionUtils.or(predicate, qPurchaser.name.like("%" + searchValue + "%"));

        } else {
            predicate = StrUtil.isBlank(param.getCode()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.code.like("%" + param.getCode() + "%"));
            predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.name.like("%" + param.getName() + "%"));
        }
        QueryResults<PurchaserDTO> purchaserDTOQueryResults = queryFactory.select(
                Projections.bean(
                        PurchaserDTO.class,
                        qPurchaser.id,
                        qPurchaser.code,
                        qPurchaser.name,
                        qPurchaser.artificialPerson,
                        qPurchaser.description,
                        qPurchaser.enabled,
                        qPurchaser.sourceId,
                        qPurchaser.sourceCode,
                        qPurchaser.companyId,
                        qPurchaser.companyCode,
                        qPurchaser.companyName
                )
        ).from(qPurchaser).where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return PageUtils.of(purchaserDTOQueryResults, pageable);
    }

    public Page pageQuery(PurchaserSmallQuery query, Pageable pageable) {
        String qs = query.getQs();
        Predicate predicate = ExpressionUtils.anyOf();
        if (StrUtil.isNotBlank(qs)) {
            predicate = ExpressionUtils.or(predicate, qPurchaser.code.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qPurchaser.name.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qPurchaser.companyCode.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qPurchaser.companyName.like("%" + qs + "%"));
        }
        predicate = StrUtil.isBlank(query.getCode()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.code.eq(query.getCode()));
        predicate = StrUtil.isBlank(query.getName()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.name.eq(query.getName()));
        predicate = StrUtil.isBlank(query.getCompanyCode()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.companyCode.eq(query.getCompanyCode()));
        predicate = StrUtil.isBlank(query.getCompanyName()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.companyName.eq(query.getCompanyName()));
        predicate = ObjectUtil.isEmpty(query.getOrgId()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.organization.id.eq(query.getOrgId()));

        predicate =  ExpressionUtils.and(predicate, qPurchaser.enabled.eq(Boolean.TRUE));
        // 过滤供应商条件
        predicate = ObjectUtil.isEmpty(query.getSupId()) ? predicate : ExpressionUtils.and(predicate, qPurchaser.suppliers.contains(Supplier.of(query.getSupId())));
        QueryResults<Purchaser> purchaserQueryResults = queryFactory.select(qPurchaser).from(qPurchaser)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(predicate).fetchResults();
        List<Purchaser> results = purchaserQueryResults.getResults();
        List<PurchaserDTO> purchaserDTOS = purchaserMapper.toDto(results);
        return PageUtils.of(purchaserDTOS, pageable, purchaserQueryResults.getTotal());
    }

    public List<PurchaserDTO> smallQuery(QueryParam<PurchaserSmallQuery> queryParam) {
        String qs = queryParam.getSearchValue();
        PurchaserSmallQuery param = queryParam.getParam();
        Predicate predicate = ExpressionUtils.anyOf();
        if (StrUtil.isNotBlank(qs)) {
            predicate = ExpressionUtils.or(predicate, qPurchaser.code.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qPurchaser.name.like("%" + qs + "%"));
        }
        predicate = ExpressionUtils.and(predicate,qPurchaser.enabled.eq(Boolean.TRUE));
        if (param.getOrgId() != null) {
            qPurchaser.organization.id.eq(param.getOrgId());
        }
        return queryFactory.select(
                Projections.bean(
                        PurchaserDTO.class,
                        qPurchaser.id,
                        qPurchaser.code,
                        qPurchaser.name,
                        qPurchaser.sourceId,
                        qPurchaser.sourceCode,
                        qPurchaser.companyId,
                        qPurchaser.companyCode,
                        qPurchaser.companyName
                )
        ).from(qPurchaser).where(predicate).fetch();
    }

    public List<String> getAllPurchaserCode(){
        Optional<List<Purchaser>> optionalList = purchaserRepository.findAllByEnabled(true);
        return optionalList.map(purchasers -> purchasers.stream().map(Purchaser::getCode).collect(Collectors.toList())).orElse(null);
    }

    public Purchaser findByCode(String code) {
        return purchaserRepository.findByCode(code).orElseThrow(() -> new ApiException(404, "无相关对象信息"));
    }

    public Purchaser findById(Long id) {
        return purchaserRepository.findById(id).orElseThrow(() -> new ApiException(404, "无相关对象信息"));
    }

    public Purchaser getOne(Long id) {
        return purchaserRepository.getOne(id);
    }


    public List<Purchaser> queryRelatePurchasers() {
        List<Purchaser> purchasers = Lists.newArrayList();
        SecurityUtils.getCurrentOrg().ifPresent(organization -> {
            List<Purchaser> fetch = queryFactory.select(qPurchaser)
                    .from(qPurchaser)
                    .where(qPurchaser.organization.id.eq(organization.getId())).fetch();
            purchasers.addAll(fetch);
        });
        return purchasers;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Purchaser purchaser) {
        purchaserRepository.saveAndFlush(purchaser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(UpdatePurchaserCommand command) {
        applicationEventPublisher.publishEvent(new PurchaserUpdatedEvent(this, UpdatePurchaser.of(command.getId(), command.getCode(), command.getName(), command.getArtificialPerson(), command.getDescription(), command.getEnabled())));
    }


    @Transactional(rollbackFor = Exception.class)
    public void bindSupplierCommand(BindSupplierCommand command) {
        Long id = command.getId();
        Long orgId = command.getOrgId();
        List<Long> supplierIds = command.getSupplierIds();
        applicationEventPublisher.publishEvent(new PurchaserBoundEvent(this, BindPurchaser.of(id, orgId, supplierIds)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelBindSupplierCommand(CancelBindSupplierCommand command) {
        Long id = command.getId();
        Long orgId = command.getOrgId();
        List<Long> supplierIds = command.getSupplierIds();
        applicationEventPublisher.publishEvent(new PurchaserCancelBoundEvent(this, BindPurchaser.of(id, orgId, supplierIds)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindAllToPurchaserCommand(BindAllSupplierCommand command) {
        Long id = command.getId();
        Long orgId = command.getOrgId();
        Purchaser purchaser = purchaserRepository.getOne(id);
        applicationEventPublisher.publishEvent(new PurchaserAllBoundEvent(this, AllBindPurchaser.of(purchaser.getId(), purchaser.getOrganization().getId())));
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelBindAllToPurchaserCommand(CancelBindAllSupplierCommand command) {
        Long id = command.getId();
        Purchaser purchaser = purchaserRepository.findById(id).orElseThrow(() -> new ApiException(404, "无法找到实体"));
        purchaserRepository.cancelBindAllToPurchaser(purchaser.getId());
    }


    public List<SupplierDTO> relateSuppliers(Long id) {
        Purchaser purchaser = purchaserRepository.findById(id).orElseThrow(() -> new ApiException(404, "无法找到实体"));
        List<Supplier> suppliers = purchaser.getSuppliers();
        return supplierMapper.toDto(suppliers);
    }


    @Transactional(rollbackFor = Exception.class)
    public void cancelBound(Long id, List<Long> supplierIds) {
        purchaserRepository.cancelBound(id, supplierIds);
    }
}
