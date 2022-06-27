package net.bncloud.saas.supplier.repository;

import net.bncloud.saas.supplier.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {

    Optional<Type> findOneByOrgIdAndGroup(Long orgId, String group);

    List<Type> findAllByOrgId(Long orgId);
}
