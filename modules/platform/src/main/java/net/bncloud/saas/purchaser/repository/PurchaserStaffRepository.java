package net.bncloud.saas.purchaser.repository;

import net.bncloud.saas.purchaser.domain.PurchaserStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaserStaffRepository extends JpaRepository<PurchaserStaff, Long> {
    Optional<PurchaserStaff> findByMobile(String mobile);
}
