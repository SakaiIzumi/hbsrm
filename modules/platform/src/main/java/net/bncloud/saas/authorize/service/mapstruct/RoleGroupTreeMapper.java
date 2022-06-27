package net.bncloud.saas.authorize.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.service.dto.RoleGroupTreeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {RoleSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleGroupTreeMapper extends BaseMapper<RoleGroupTreeDTO, RoleGroup> {
}
