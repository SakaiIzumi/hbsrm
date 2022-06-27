package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.service.dto.SupplierManagerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SupplierManagerMapper extends BaseMapper<SupplierManagerDTO, SupplierManager> {
}
