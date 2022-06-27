package net.bncloud.saas.supplier.service;

import net.bncloud.common.util.BeanUtil;
import net.bncloud.saas.supplier.domain.SupplierLinkMan;
import net.bncloud.saas.supplier.domain.SupplierSourceType;
import net.bncloud.saas.supplier.repository.SupplierLinkManRepository;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.supplier.service.dto.SupplierLinkManDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SupplierLinkManService {

    private final SupplierLinkManRepository supplierLinkManRepository;
    private final SupplierRepository supplierRepository;

    public SupplierLinkManService(SupplierLinkManRepository supplierLinkManRepository,
                                  SupplierRepository supplierRepository) {
        this.supplierLinkManRepository = supplierLinkManRepository;
        this.supplierRepository = supplierRepository;
    }

    /**
     * 保存供应商联系人
     *
     * @param supplierLinkManDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveSupplierLinkMan(SupplierLinkManDTO supplierLinkManDTO) {
        supplierRepository.findById(supplierLinkManDTO.getSupplierId()).ifPresent(supplier -> {
            SupplierLinkMan supplierLinkMan = new SupplierLinkMan();
            BeanUtil.copyProperties(supplierLinkManDTO, supplierLinkMan);
            supplierLinkMan.setSupplier(supplier);
            supplierLinkMan.setAllowOps(Boolean.TRUE);
            supplierLinkMan.setSourceType(SupplierSourceType.USER_CREATE);
            supplierLinkManRepository.save(supplierLinkMan);
//            supplier.addLinkMans(supplierLinkMan);
//            Supplier supplier1 = supplierRepository.save(supplier);
        });
    }

    /**
     * 删除供应商联系人
     *
     * @param supplierLinkManDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(SupplierLinkManDTO supplierLinkManDTO) {
        supplierRepository.findById(supplierLinkManDTO.getSupplierId()).ifPresent(supplier -> {
            supplier.getLinkMans().removeIf(item -> Objects.equals(item.getId(), supplierLinkManDTO.getId()) && Boolean.TRUE.equals(item.getAllowOps()));
            supplierRepository.save(supplier);
        });
    }

    public void deleteById(long id) {
        supplierLinkManRepository.findById(id).ifPresent(supplierLinkMan -> {
            if (Boolean.TRUE.equals(supplierLinkMan.getAllowOps())) {
                supplierLinkManRepository.deleteById(id);
            }
        });
    }
}
