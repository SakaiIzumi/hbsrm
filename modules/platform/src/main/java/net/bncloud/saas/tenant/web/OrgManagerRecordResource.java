package net.bncloud.saas.tenant.web;


import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.tenant.service.OrgManagerRecordService;
import net.bncloud.saas.tenant.service.command.CreatedOrgManagerCommand;
import net.bncloud.saas.tenant.service.query.OrgManagerQuery;
import net.bncloud.saas.user.domain.vo.OrganizationManagerVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenant/org/manager_record")
@AllArgsConstructor
public class OrgManagerRecordResource {

    private OrgManagerRecordService orgManagerRecordService;

    @ApiOperation(value = "[成员管理]协助组织成员表格")
    @PostMapping("/allOrgManagerTable")
    public R allOrgManagerTable(@RequestBody QueryParam<OrgManagerQuery> queryParam, Pageable pageable) {
        return R.data(orgManagerRecordService.allOrgManagerTable(queryParam, pageable));
    }

    @ApiOperation(value = "创建协助组织管理员")
    @PostMapping("/createOrganizationManager")
    public R createOrganizationManager(@RequestBody CreatedOrgManagerCommand  command) {
        orgManagerRecordService.createOrgManager(command);
        return R.success();
    }

    @ApiOperation(value = "移除协助组织管理员")
    @DeleteMapping("/deleteOrganizationManager/{managerId}")
    public R deleteOrganizationManager(@PathVariable Long managerId) {
        orgManagerRecordService.deleteOrganizationManager(managerId);
        return R.success();
    }
}
