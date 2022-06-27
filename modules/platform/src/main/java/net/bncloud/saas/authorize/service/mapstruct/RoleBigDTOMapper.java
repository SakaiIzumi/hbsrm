package net.bncloud.saas.authorize.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.service.dto.RoleBigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {RoleGroupMapper.class, MenuMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleBigDTOMapper extends BaseMapper<RoleBigDTO, Role> {
}
