package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.CfgParamMould;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CfgParamMouldRepository extends JpaRepository<CfgParamMould, Long> {


}
