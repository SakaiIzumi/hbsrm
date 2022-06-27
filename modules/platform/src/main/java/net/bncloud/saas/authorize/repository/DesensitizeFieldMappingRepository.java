package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DesensitizeFieldMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesensitizeFieldMappingRepository extends JpaRepository<DesensitizeFieldMapping, Long>, JpaSpecificationExecutor<DesensitizeFieldMapping> {


    List<DesensitizeFieldMapping> findAllByDataSubject_Id(Long subjectId);

}
