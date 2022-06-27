package net.bncloud.saas.tenant.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.service.dto.TenantCompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @ClassName CompanyMapper
 * @Description: TODO
 * @Author Administrator
 * @Date 2021/4/13
 * @Version V1.0
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper extends BaseMapper<TenantCompanyDTO, Company> {
}
