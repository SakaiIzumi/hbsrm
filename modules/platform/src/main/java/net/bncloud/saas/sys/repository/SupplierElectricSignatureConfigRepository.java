package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.ElectricSignatureConfigType;
import net.bncloud.saas.sys.domain.SupplierElectricSignatureConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierElectricSignatureConfigRepository extends JpaRepository<SupplierElectricSignatureConfig, Long>,
        JpaSpecificationExecutor<SupplierElectricSignatureConfig> {

    public SupplierElectricSignatureConfig findBySupplierCodeAndType(String code,ElectricSignatureConfigType type);

}
