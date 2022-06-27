package net.bncloud.saas.user.repository;

import net.bncloud.saas.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {

    Optional<UserInfo> findOneByMobileAndStateCode(String mobile, String stateCode);

    Optional<UserInfo> findOneByMobile(String mobile);

    List<UserInfo> findAllByIdIn(List<Long> ids);
}
