package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.SupplierExt;
import net.bncloud.saas.supplier.service.dto.SupplierExtDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierExtMapper extends BaseMapper<SupplierExtDTO, SupplierExt> {
}
