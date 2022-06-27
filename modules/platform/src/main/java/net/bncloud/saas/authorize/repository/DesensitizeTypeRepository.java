package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DesensitizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DesensitizeTypeRepository extends JpaRepository<DesensitizeType, Long>, JpaSpecificationExecutor<DesensitizeType> {

    List<DesensitizeType> findAllByDimensionType(String type);
}
