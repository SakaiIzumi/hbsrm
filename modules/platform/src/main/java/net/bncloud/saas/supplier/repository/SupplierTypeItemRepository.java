package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.SupplierTypeItem;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierTypeItemRepository extends JpaRepository<SupplierTypeItem, Long>, JpaSpecificationExecutor<SupplierTypeItem> {

    List<SupplierTypeItem> findAllByTypeId(Long id);
}
