package net.bncloud.saas.tenant.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.saas.org.OrgDTO;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.service.OrganizationService;
import net.bncloud.saas.tenant.service.query.OrganizationQuery;
import net.bncloud.saas.tenant.web.payload.CreateOrgPayload;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant/org")
public class
OrganizationResource {

    private final OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @ApiOperation("分页查询组织")
    @PostMapping("/pageQuery")
    public R pageQuery(@RequestBody QueryParam<OrganizationQuery> queryParam, Pageable pageable) {
        return R.data(organizationService.pageQuery(queryParam, pageable));
    }


    @ApiOperation("创建组织")
    @PostMapping("/add")
    public R<Organization> createOrg(@RequestBody CreateOrgPayload payload) {
        Organization org = organizationService.createOrg(payload.toCommand());
        return R.data(org);
    }


    @GetMapping("/getById")
    public R<OrgDTO> getById(@RequestParam(name = "orgId") Long orgId) {
        OrgDTO orgDTO = organizationService.findById(orgId).map(organization -> {
            OrgDTO dto = new OrgDTO();
            dto.setCompanyId(organization.getCompany().getId());
            dto.setCompanyName(organization.getCompany().getName());
            dto.setOrgId(organization.getId());
            dto.setOrgName(organization.getName());
            return dto;
        }).orElse(null);
        return R.data(orgDTO);
    }

}
