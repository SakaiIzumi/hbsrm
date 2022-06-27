package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.SupplierTagItem;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierTagItemRepository extends JpaRepository<SupplierTagItem, Long>, JpaSpecificationExecutor<SupplierTagItem> {

    List<SupplierTagItem> findAllByTagId(Long id);



}
