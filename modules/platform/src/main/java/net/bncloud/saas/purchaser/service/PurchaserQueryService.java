package net.bncloud.saas.purchaser.service;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.domain.QPurchaser;
import net.bncloud.saas.purchaser.repository.PurchaserRepository;
import net.bncloud.saas.purchaser.service.dto.PurchaserDTO;
import net.bncloud.saas.purchaser.service.mapstruct.PurchaserMapper;
import net.bncloud.saas.supplier.domain.QSupplier;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.QOrganization;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class PurchaserQueryService {
    private final JPAQueryFactory queryFactory;
    private final QPurchaser qPurchaser = QPurchaser.purchaser;
    private final QSupplier qSupplier = QSupplier.supplier;
    private final QOrganization qOrganization = QOrganization.organization;


    private final PurchaserRepository purchaserRepository;

    private final PurchaserMapper purchaserMapper;


    public List<Purchaser> findAllByIdIn(List<Long> ids) {
        return purchaserRepository.findAllById(ids);
    }

    public Purchaser findByCode(String code) {
        return purchaserRepository.findByCode(code).orElse(null);
    }

    public Purchaser getOne(Long id) {
        return purchaserRepository.getOne(id);
    }

    public PurchaserDTO edit(Long id) {
        Purchaser purchaser = purchaserRepository.findById(id).orElseThrow(() -> new ApiException(404, "查无对象"));
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(purchaser);
        if (CollectionUtil.isNotEmpty(purchaserDTO.getSuppliers())) {
            List<Long> supplierIds = purchaserDTO.getSuppliers().stream().map(SupplierDTO::getId).collect(Collectors.toList());
            purchaserDTO.setSupplierIds(supplierIds);
        }
        return purchaserDTO;
    }


    public OrgIdDTO info(OrgIdQuery query) {

        Tuple allBySupCodeAndPurCode = purchaserRepository.findAllBySupCodeAndPurCode(query.getPurchaseCode(), query.getSupplierCode());
        if (allBySupCodeAndPurCode==null) {
            return null;
        }
        String purchaseCode = allBySupCodeAndPurCode.get(0, String.class);
        String supplierCode = allBySupCodeAndPurCode.get(1, String.class);
        BigInteger orgId = allBySupCodeAndPurCode.get(2, BigInteger.class);
        String supplierName = allBySupCodeAndPurCode.get(3, String.class);
        String purchaseName = allBySupCodeAndPurCode.get(4, String.class);



        OrgIdDTO orgIdDTO = new OrgIdDTO();
        if (orgId == null) {
            return null;
        }
        orgIdDTO.setOrgId(orgId.longValue());
        orgIdDTO.setPurchaseCode(purchaseCode);
        orgIdDTO.setSupplierCode(supplierCode);
        orgIdDTO.setSupplierame(supplierName);
        orgIdDTO.setPurchaseName(purchaseName);
        return orgIdDTO;
    }

    public List<OrgIdDTO> infos(List<OrgIdQuery> orgInfoQueries) {
        List<OrgIdDTO>  list = Lists.newArrayList();
        orgInfoQueries.forEach(item->{
            OrgIdDTO info = info(item);
            if (info !=null) {
                list.add(info);
            }
        });

        return list;
    }

    public List<PurchaserVo> listByCode(Collection<?> purchaserCodeColl) {
        Optional<List<Purchaser>> purchaserListOpt = purchaserRepository.findAllByCodeInAndEnabledIsTrue(purchaserCodeColl);
        return purchaserListOpt.orElse(new ArrayList<>()).stream().map(purchaser -> BeanUtil.copy( purchaser,PurchaserVo.class )).collect(Collectors.toList());
    }

    /**
     * 获取所有采购方 根据 组织ID
     * @param ordId
     * @return
     */
    public List<PurchaserVo> getAllPurchaserByOrgId(Long ordId) {
        return purchaserRepository.findAllByOrganizationAndEnabledIsTrue(Organization.of(ordId)).orElse(new ArrayList<>()).stream().map(purchaser -> BeanUtil.copy( purchaser,PurchaserVo.class )).collect(Collectors.toList());

    }

    /**
     * 获取所有采购方
     * @return
     */
    public List<PurchaserVo> getAllPurchaserInfo() {
        List<Purchaser> all = purchaserRepository.findAll();
        List<PurchaserVo> collect = all.stream().map(item -> BeanUtil.copy(item, PurchaserVo.class)).collect(Collectors.toList());
        return collect;

    }
}
