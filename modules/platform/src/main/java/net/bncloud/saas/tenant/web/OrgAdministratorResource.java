package net.bncloud.saas.tenant.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.tenant.service.OrgAdministratorService;
import net.bncloud.saas.tenant.service.command.TransferOrgManagerCommand;
import net.bncloud.saas.tenant.service.dto.OrgAdministratorDTO;
import net.bncloud.saas.tenant.service.query.OrgManagerQuery;
import net.bncloud.saas.tenant.web.param.OrgAdministratorParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant/org/manager")
public class OrgAdministratorResource {

    private final OrgAdministratorService orgAdministratorService;

    public OrgAdministratorResource(OrgAdministratorService orgAdministratorService) {
        this.orgAdministratorService = orgAdministratorService;
    }


    @PostMapping("/pageQuery")
    public R<Page<OrgAdministratorDTO>> pageQuery(@RequestBody OrgManagerQuery query, Pageable pageable) {
        return R.data(orgAdministratorService.pageQuery(query, pageable));
    }

    @GetMapping("/{id}")
    public R<OrgAdministratorDTO> detail(@PathVariable Long id) {
        return orgAdministratorService.detail(id);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        orgAdministratorService.deleteOrgAdmin(id);
        return R.success();
    }

    @PostMapping
    public R<Void> addOrgAdmin(@RequestBody OrgAdministratorParam orgAdministratorParam) {
        orgAdministratorService.addOrgAdmin(orgAdministratorParam);
        return R.success();
    }

    @GetMapping("/checkTransfer")
    public R<OrgAdministratorDTO> getCurrentMainManagerForTransfer() {
        return R.data(orgAdministratorService.getCurrentMainManagerForTransfer());
    }

    @PutMapping("/transfer")
    public R<Void> transferOrgAdmin(@RequestBody TransferOrgManagerCommand command) {
        orgAdministratorService.transferAdmin(command);
        return R.success();
    }

    @GetMapping("/sendShortMessage")
    public R<Integer> sendShortMessage(@RequestBody String mobile) {
        return R.data(orgAdministratorService.sendShortMessage(mobile));
    }

    @ApiOperation(value = "[成员管理]协助组织管理员表格", notes = "根据类型查询管理员/普通成员,全部用户")
    @PostMapping("/orgManagerTable")
    public R orgManagerTable(@RequestBody QueryParam<OrgManagerQuery> queryParam, Pageable pageable) {
        return R.data(orgAdministratorService.orgManagerTable(queryParam, pageable));
    }
}
