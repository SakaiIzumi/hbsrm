package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.DictItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictItemRepository extends JpaRepository<DictItem, Long>, JpaSpecificationExecutor<DictItem> {

    Page<DictItem> findByDictCode(String code, Pageable pageable);

    List<DictItem> findByDictCode(String code);
}
