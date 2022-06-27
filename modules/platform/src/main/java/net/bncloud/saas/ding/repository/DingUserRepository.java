package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingUser;
import net.bncloud.saas.ding.domain.pk.DingUserPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DingUserRepository extends JpaRepository<DingUser, DingUserPK> {
}
