package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DataSubjectDimensionRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataSubjectDimensionRelRepository extends JpaRepository<DataSubjectDimensionRel, Long>, JpaSpecificationExecutor<DataSubjectDimensionRel> {

    List<DataSubjectDimensionRel> findAllByDimensionCodeIn(List<String> codes);

    List<DataSubjectDimensionRel> findAllByDataSubject_Id(Long subjectId);

    Optional<DataSubjectDimensionRel> findByDimensionCodeAndDataSubject_Id(String code, Long subjectId);

}
