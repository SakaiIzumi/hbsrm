package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailRepository extends JpaRepository<ClientDetail, String> {
}
