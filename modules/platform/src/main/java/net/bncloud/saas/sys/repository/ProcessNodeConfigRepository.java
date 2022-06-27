package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.ProcessNodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/4
 */
@Repository
public interface ProcessNodeConfigRepository extends JpaRepository<ProcessNodeConfig, Long> {

    List<ProcessNodeConfig> findByConfigParamId(Long id);


}
