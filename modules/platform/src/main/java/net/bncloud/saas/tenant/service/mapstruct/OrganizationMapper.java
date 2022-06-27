package net.bncloud.saas.tenant.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.service.dto.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationMapper extends BaseMapper<OrganizationDTO, Organization> {
}
