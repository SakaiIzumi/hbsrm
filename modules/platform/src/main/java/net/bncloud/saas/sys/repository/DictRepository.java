package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DictRepository extends JpaRepository<Dict, String>, JpaSpecificationExecutor<Dict> {
    void deleteByCodeIn(Set<String> codes);

    Dict findByCode(String code);
}
