package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/5
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long>, JpaSpecificationExecutor<Currency> {


}
