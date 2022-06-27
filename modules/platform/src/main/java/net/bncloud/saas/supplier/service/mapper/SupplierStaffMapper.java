package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.service.dto.SupplierStaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierStaffMapper extends BaseMapper<SupplierStaffDTO, SupplierStaff> {
}
