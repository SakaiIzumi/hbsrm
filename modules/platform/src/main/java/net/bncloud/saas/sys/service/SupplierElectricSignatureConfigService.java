package net.bncloud.saas.sys.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.ElectricSignatureConfigType;
import net.bncloud.saas.sys.domain.SupplierElectricSignatureConfig;
import net.bncloud.saas.sys.repository.SupplierElectricSignatureConfigRepository;
import net.bncloud.saas.sys.service.command.EditSignatureConfigCommand;
import net.bncloud.saas.sys.service.query.SupplierElectricSignatureConfigQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Optional;

@Service
public class SupplierElectricSignatureConfigService {

    private final SupplierElectricSignatureConfigRepository supplierElectricSignatureConfigRepository;

    public SupplierElectricSignatureConfigService(SupplierElectricSignatureConfigRepository supplierElectricSignatureConfigRepository) {
        this.supplierElectricSignatureConfigRepository = supplierElectricSignatureConfigRepository;
    }


    public Page<SupplierElectricSignatureConfig> pageQuery(QueryParam<SupplierElectricSignatureConfigQuery> query, Pageable pageable) {
        SupplierElectricSignatureConfigQuery param = query.getParam();
        param.setQs(query.getSearchValue());
        return supplierElectricSignatureConfigRepository.findAll(buildSpecification(param), pageable);
    }

    private Specification<SupplierElectricSignatureConfig> buildSpecification(SupplierElectricSignatureConfigQuery param) {
        return (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            if(param == null){
                return predicate;
            }

            if(param.getType() != null){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("type"),param.getType()));
            }

            if(StringUtils.isNotBlank(param.getQs())){
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(root.get("net/bncloud/service/api/platform/supplier").get("code"), "%" + param.getQs() + "%")
                        , criteriaBuilder.like(root.get("net/bncloud/service/api/platform/supplier").get("name"), "%" + param.getQs() + "%")
                );
                predicate.getExpressions().add(or);

            }

            return predicate;
        };
    }

    public void edit(EditSignatureConfigCommand command) {
        Optional<SupplierElectricSignatureConfig> optional = supplierElectricSignatureConfigRepository.findById(command.getId());
        if(optional.isPresent()){
            SupplierElectricSignatureConfig config = optional.get();
            if(command.getEnabled() != null){
                config.setEnabled(command.getEnabled());
            }

            supplierElectricSignatureConfigRepository.save(config);
        }
    }

    public SupplierElectricSignatureConfig getByCodeAndType(String code,ElectricSignatureConfigType type) {
        SupplierElectricSignatureConfig bySupplierCodeAndType = supplierElectricSignatureConfigRepository.findBySupplierCodeAndType(code, type);
        return bySupplierCodeAndType;

    }
}
