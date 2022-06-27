package net.bncloud.saas.tenant.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.service.mapstruct.RoleMapper;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.service.dto.OrgEmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @ClassName OrgEmployeeMapper
 * @Description: TODO
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgEmployeeMapper extends BaseMapper<OrgEmployeeDTO, OrgEmployee> {

}
