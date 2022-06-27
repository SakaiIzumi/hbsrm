package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.Tag;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagConfigItemRepository extends JpaRepository<TagConfigItem, Long>, JpaSpecificationExecutor<TagConfigItem> {

    List<TagConfigItem> findAllByTagAndItemAndIdNot(Tag tag, String item, Long id);

    List<TagConfigItem> findAllByTag(Tag tag);

    List<TagConfigItem> findAllByTagAndItem(Tag tag, String item);
}
