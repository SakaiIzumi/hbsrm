package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DataAppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataAppConfigRepository extends JpaRepository<DataAppConfig, Long>, JpaSpecificationExecutor<DataAppConfig> {

    Optional<DataAppConfig> findByAppCode(String code);
}
