package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {


    Optional<Tag> findByOrgIdAndGroup(Long orgId, String group);

    List<Tag> findAllByOrgId(Long orgId);

}
