package net.bncloud.saas.tenant.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.service.dto.OrgDepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @ClassName OrgDepartmentMapper
 * @Description: TODO
 * @Author Administrator
 * @Date 2021/4/10
 * @Version V1.0
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgDepartmentMapper extends BaseMapper<OrgDepartmentDTO, OrgDepartment> {
}
