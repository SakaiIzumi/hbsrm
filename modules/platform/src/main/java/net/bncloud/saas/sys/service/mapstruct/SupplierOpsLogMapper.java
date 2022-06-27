package net.bncloud.saas.sys.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.SupplierOpsLog;
import net.bncloud.saas.supplier.service.dto.SupplierOpsLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierOpsLogMapper extends BaseMapper<SupplierOpsLogDTO, SupplierOpsLog> {

}
