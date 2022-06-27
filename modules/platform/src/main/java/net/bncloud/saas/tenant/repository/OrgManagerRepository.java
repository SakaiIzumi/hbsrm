package net.bncloud.saas.tenant.repository;

import net.bncloud.common.constants.ManagerType;
import net.bncloud.saas.tenant.domain.OrgManager;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgManagerRepository extends JpaRepository<OrgManager, Long>, JpaSpecificationExecutor<OrgManager> {
    List<OrgManager> findAllByOrgIdAndManagerType(Long orgId, ManagerType managerType);

    List<OrgManager> findAllByOrgId(Long orgId);

    /**
     * 根据userId查找所在组织的管理员
     *
     * @param userId
     * @return
     */
    List<OrgManager> findAllByUser_Id(Long userId);

    List<OrgManager> findAllByOrg_IdAndUser_IdIn(Long orgId, List<Long> userIds);

}
