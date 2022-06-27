package net.bncloud.saas.authorize.service.mapstruct;


import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.service.dto.RoleGroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleGroupMapper extends BaseMapper<RoleGroupDTO, RoleGroup> {

}
