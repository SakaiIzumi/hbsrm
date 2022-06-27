package net.bncloud.saas.supplier.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.supplier.domain.QSupplierOpsLog;
import net.bncloud.saas.supplier.domain.SupplierOpsLog;
import net.bncloud.saas.supplier.repository.SupplierOpsLogRepository;
import net.bncloud.saas.supplier.service.dto.SupplierOpsLogDTO;
import net.bncloud.saas.supplier.service.query.SupplierOpsLogQuery;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierOpsLogService {
    private final SupplierOpsLogRepository supplierOpsLogRepository;

    private final JPAQueryFactory queryFactory;
    private final QSupplierOpsLog qSupplierOpsLog = QSupplierOpsLog.supplierOpsLog;

    @Transactional(readOnly = true)
    public List<SupplierOpsLog> selectList() {
        return supplierOpsLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SupplierOpsLog getById(Long id) {
        return supplierOpsLogRepository.findById(id).get();
    }

    @Transactional
    public void save(SupplierOpsLog supplierOpsLog) {

        if (supplierOpsLog != null) {
            supplierOpsLogRepository.save(supplierOpsLog);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (id != null) {
            supplierOpsLogRepository.deleteById(id);
        }
    }

    public Page<SupplierOpsLogDTO> queryPage(QueryParam<SupplierOpsLogQuery> queryParam, Pageable pageable) {
        SupplierOpsLogQuery supplierOpsLogQuery = queryParam.getParam();
        QueryResults<SupplierOpsLogDTO> supplierOpsLogDTOQueryResults = queryFactory.select(Projections.bean(SupplierOpsLogDTO.class,
                qSupplierOpsLog.id,
                qSupplierOpsLog.supplierId,
                qSupplierOpsLog.content,
                qSupplierOpsLog.remark,
                qSupplierOpsLog.createdBy,
                qSupplierOpsLog.createdDate,
                qSupplierOpsLog.opsUserName
        )).from(qSupplierOpsLog).where(qSupplierOpsLog.supplierId.eq(supplierOpsLogQuery.getSupplierId())).orderBy(qSupplierOpsLog.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return PageUtils.of(supplierOpsLogDTOQueryResults, pageable);
    }
}
