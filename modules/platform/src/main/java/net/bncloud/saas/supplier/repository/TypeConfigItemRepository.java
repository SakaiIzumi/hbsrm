package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.Type;
import net.bncloud.saas.supplier.domain.TypeConfigItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeConfigItemRepository extends JpaRepository<TypeConfigItem, Long>, JpaSpecificationExecutor<TypeConfigItem> {

    List<TypeConfigItem> findAllByTypeAndItemAndIdNot(Type type, String item, Long id);

    List<TypeConfigItem> findAllByType(Type type);

    List<TypeConfigItem> findAllByTypeAndItem(Type type, String item);
}
