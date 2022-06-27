package net.bncloud.saas.user.repository;

import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCurrentRepository extends JpaRepository<UserCurrent, Long>, JpaSpecificationExecutor<UserCurrent> {
}