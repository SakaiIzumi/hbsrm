package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.DataDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DataDimensionRepository extends JpaRepository<DataDimension, Long>, JpaSpecificationExecutor<DataDimension> {


    List<DataDimension> findAllByDimensionType(String dimensionType);

    List<DataDimension> findAllByDimensionCodeIn(List<String> dimensionCodes);

}
