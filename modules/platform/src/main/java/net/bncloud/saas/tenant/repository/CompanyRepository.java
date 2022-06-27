package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findOneByCreditCode(String creditCode);

    Optional<Company> findCompanyByCode(String code);

    Optional<Company> findFirstByNameIsNotNull();
}
