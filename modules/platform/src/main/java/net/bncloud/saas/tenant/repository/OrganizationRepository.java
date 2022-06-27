package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByName(String orgName);

    Optional<Organization> findOneByCompanyAndName(Company company, String orgName);

    @Query("select  o from Organization o inner join OrgEmployee e on o.id= e.org.id  where e.user.userId =:userId")
    List<Organization> findAllByUserId(@Param("userId") Long userId);


}
