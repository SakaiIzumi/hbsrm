package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.SupplierLinkMan;
import net.bncloud.saas.supplier.service.dto.SupplierLinkManDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierLinkManMapper extends BaseMapper<SupplierLinkManDTO, SupplierLinkMan> {
}
