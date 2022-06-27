package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DataSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSubjectRepository extends JpaRepository<DataSubject, Long>, JpaSpecificationExecutor<DataSubject> {

    List<DataSubject> findAllByDataAppConfig_Id(Long appConfig);
}
