package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import net.bncloud.saas.supplier.domain.TypeConfigItem;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TagConfigItemMapper.class, TypeConfigItemMapper.class, SupplierManagerMapper.class, SupplierLinkManMapper.class, SupplierExtMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper extends BaseMapper<SupplierDTO, Supplier> {
}
