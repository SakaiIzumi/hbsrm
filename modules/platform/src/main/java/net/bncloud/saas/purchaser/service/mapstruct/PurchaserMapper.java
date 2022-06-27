package net.bncloud.saas.purchaser.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.service.dto.PurchaserDTO;
import net.bncloud.saas.supplier.service.mapper.SupplierMapper;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.service.dto.OrganizationDTO;
import net.bncloud.saas.tenant.service.mapstruct.OrganizationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, SupplierMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaserMapper extends BaseMapper<PurchaserDTO, Purchaser> {
}
