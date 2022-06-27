package net.bncloud.saas.supplier.service;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.event.CreateSupplierManager;
import net.bncloud.saas.event.SupplierManagerCreateEvent;
import net.bncloud.saas.supplier.domain.*;
import net.bncloud.saas.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.saas.supplier.repository.SupplierAccountRepository;
import net.bncloud.saas.supplier.repository.SupplierExtRepository;
import net.bncloud.saas.supplier.repository.SupplierLinkManRepository;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.utils.BeanListCopyUtil;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierAccountDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierExtDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierLinkManDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OaSupplierRemoteApiService extends BaseService {
    private final SupplierRepository supplierRepository;
    private final SupplierExtRepository supplierExtRepository;
    private final SupplierLinkManRepository supplierLinkManRepository;
    private final SupplierAccountRepository supplierAccountRepository;

    @Async
    @Transactional
    public void saveOaSupplierRemote(List<OaSupplierDTO> oaSupplierDTOS) {
        for (OaSupplierDTO oaSupplierDTO : oaSupplierDTOS) {
            Optional<Supplier> supplierOptional = supplierRepository.findOneByCode(oaSupplierDTO.getCode());
            try {
                if (supplierOptional.isPresent()) {
                    updateSupplier(oaSupplierDTO, supplierOptional.get());
                } else {
                    createSupplier(oaSupplierDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                //fixme 以后增加补偿记录
            }
        }
    }


    private void createSupplier(OaSupplierDTO oaSupplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setCode(oaSupplierDTO.getCode());
        supplier.setName(oaSupplierDTO.getName());
        supplier.setOaType(oaSupplierDTO.getOaType());
        supplier.setRelevanceStatus(SupplierRelevanceStatusEnum.RELEVANCE.getCode());
        supplier.setCreditCode(oaSupplierDTO.getCreditCode());
        supplier.setSourceType(SupplierSourceType.OA);
        supplier.setSourceId(oaSupplierDTO.getSourceId());
        supplier.setOaCode(oaSupplierDTO.getOaCode());

        if (CollectionUtil.isNotEmpty(oaSupplierDTO.getOaSupplierLinkMen())) {
            supplier.setManagerName(oaSupplierDTO.getOaSupplierLinkMen().get(0).getName());
            supplier.setManagerMobile(oaSupplierDTO.getOaSupplierLinkMen().get(0).getMobile());
        }


        Supplier supplierCreated = supplierRepository.save(supplier);
        createSupplierExt(oaSupplierDTO, supplierCreated);
        createSupplierLinkMan(oaSupplierDTO, supplierCreated);
        createSupplierAccounts(oaSupplierDTO, supplierCreated);
    }

    private void createSupplierAccounts(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        List<OaSupplierAccountDTO> accounts = oaSupplierDTO.getOaSupplierAccounts();
        List<SupplierAccount> supplierAccounts = BeanListCopyUtil.copyListProperties(accounts, SupplierAccount::new);
        for (SupplierAccount supplierAccount : supplierAccounts) {
            supplierAccount.setSupplier(supplier);
        }
        supplierAccountRepository.saveAll(supplierAccounts);


    }

    private void createSupplierExt(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        OaSupplierExtDTO oaSupplierExtDTO = oaSupplierDTO.getOaSupplierExtDTO();
        SupplierExt supplierExt = new SupplierExt();
        BeanUtils.copyProperties(oaSupplierExtDTO, supplierExt);
        supplierExt.setSupplier(supplier);
        supplierExtRepository.save(supplierExt);
    }

    private void createSupplierLinkMan(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        List<OaSupplierLinkManDTO> linkMen = oaSupplierDTO.getOaSupplierLinkMen();
        List<SupplierLinkMan> supplierLinkMenList = BeanListCopyUtil.copyListProperties(linkMen, SupplierLinkMan::new);
        supplierLinkMenList.forEach(supplierLinkMan -> {
            supplierLinkMan.setSourceType(SupplierSourceType.OA);
            supplierLinkMan.setSupplier(supplier);
            supplierLinkMan.setAllowOps(Boolean.FALSE);
        });
        supplier.setLinkMans(supplierLinkMenList);
        supplierLinkManRepository.saveAll(supplierLinkMenList);
        supplierLinkMenList.forEach(man -> {
            CreateSupplierManager of = CreateSupplierManager.of(
                    man.getName(),
                    man.getMobile(),
                    man.getSupplier().getId(),
                    true);
            applicationEventPublisher.publishEvent(new SupplierManagerCreateEvent(this, of));
        });
    }

    private void updateSupplier(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        String name = oaSupplierDTO.getName();
        String creditCode = oaSupplierDTO.getCreditCode();
        String oaType = oaSupplierDTO.getOaType();
        supplier.setName(name);
        supplier.setSourceType(SupplierSourceType.OA);
        supplier.setCreditCode(creditCode);
        supplier.setOaType(oaType);
        supplier.setOaCode(oaSupplierDTO.getOaCode());
        if (CollectionUtil.isNotEmpty(oaSupplierDTO.getOaSupplierLinkMen())) {
            supplier.setManagerName(oaSupplierDTO.getOaSupplierLinkMen().get(0).getName());
            supplier.setManagerMobile(oaSupplierDTO.getOaSupplierLinkMen().get(0).getMobile());
        }
        supplierRepository.save(supplier);
        updateSupplierExt(oaSupplierDTO, supplier);
        updateSupplierLinkMan(oaSupplierDTO, supplier);
        updateSupplierAccounts(oaSupplierDTO, supplier);
    }


    private void updateSupplierExt(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        SupplierExt supplierExt = supplier.getSupplierExt();
        if (supplierExt == null) {
            createSupplierExt(oaSupplierDTO, supplier);
            return;
        }
        OaSupplierExtDTO oaSupplierExtDTO = oaSupplierDTO.getOaSupplierExtDTO();
        BeanUtils.copyProperties(oaSupplierExtDTO, supplierExt);
        supplierExt.setSupplier(supplier);
        supplierExtRepository.save(supplierExt);
    }

    private void updateSupplierAccounts(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        List<SupplierAccount> accounts = supplier.getAccounts();
        List<OaSupplierAccountDTO> accountsDto = oaSupplierDTO.getOaSupplierAccounts();
        if (CollectionUtil.isNotEmpty(accountsDto)) {

            //移除存在的不可编辑的联系人信息
            supplierAccountRepository.deleteAll(accounts);
            supplierAccountRepository.flush();
            accounts = BeanListCopyUtil.copyListProperties(accountsDto, SupplierAccount::new);
            accounts.forEach(supplierAccount -> supplierAccount.setSupplier(supplier));
            supplierAccountRepository.saveAll(accounts);
            supplierAccountRepository.flush();
//            supplierRepository.save(supplier);
        } else {
            createSupplierAccounts(oaSupplierDTO, supplier);
        }

    }


    private void updateSupplierLinkMan(OaSupplierDTO oaSupplierDTO, Supplier supplier) {
        List<SupplierLinkMan> linkMans = supplier.getLinkMans();
        List<OaSupplierLinkManDTO> oaSupplierLinkMen = oaSupplierDTO.getOaSupplierLinkMen();
        if (CollectionUtil.isNotEmpty(linkMans)) { //非空
            List<SupplierLinkMan> supplierLinkManList = linkMans.stream().filter(linkMan -> !linkMan.getAllowOps()).collect(Collectors.toList());
            if (!CollectionUtil.isEmpty(supplierLinkManList)) {
                //移除存在的不可编辑的联系人信息
                supplierLinkManRepository.deleteAll(supplierLinkManList);
                supplierLinkManRepository.flush();
            }
            List<SupplierLinkMan> linkManList = BeanListCopyUtil.copyListProperties(oaSupplierLinkMen, SupplierLinkMan::new);
            linkManList.forEach(man-> {
                man.setSourceType(SupplierSourceType.OA);
                man.setSupplier(supplier);
                man.setAllowOps(Boolean.FALSE);
            });
            supplierLinkManRepository.saveAll(linkManList);
            supplierLinkManRepository.flush();
        } else {
            createSupplierLinkMan(oaSupplierDTO, supplier);
        }
    }
}
