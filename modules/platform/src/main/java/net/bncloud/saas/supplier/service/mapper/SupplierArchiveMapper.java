package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.service.dto.SupplierArchiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TagConfigItemMapper.class, TypeConfigItemMapper.class, SupplierExtMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierArchiveMapper extends BaseMapper<SupplierArchiveDTO, Supplier> {
}
