package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long>, JpaSpecificationExecutor<Api> {
}
