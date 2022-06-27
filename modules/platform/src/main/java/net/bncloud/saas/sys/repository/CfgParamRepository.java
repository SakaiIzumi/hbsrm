package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.CfgParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CfgParamRepository extends JpaRepository<CfgParam, Long> {
    List<CfgParam> findAllByCompanyId(Long companyId);

    List<CfgParam> findAllByOrgId(Long orgId);

    Optional<CfgParam> findOneByCompanyIdAndCode(Long companyId, String code);

    Optional<CfgParam> findFirstByCode(String code);


    List<CfgParam> findAllByCode(String code);

    CfgParam findFirstByCodeAndOrgId(String code, Long orgId);

    @Modifying
    @Query(value=" update CfgParam c set c.value=?2 where c.code=?1")
    void updateByCode(String code, String configString);

}
