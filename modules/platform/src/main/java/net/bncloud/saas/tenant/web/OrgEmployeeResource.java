package net.bncloud.saas.tenant.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.vo.BatchZltPurchaserStaffVO;
import net.bncloud.saas.tenant.domain.vo.RemoveOrgMemberVO;
import net.bncloud.saas.tenant.domain.vo.UserMemberVO;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import net.bncloud.saas.tenant.service.command.CreateOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.command.EditBatchOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.command.EditUserInfoCommand;
import net.bncloud.saas.tenant.service.command.EditOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.dto.OrgEmployeeDTO;
import net.bncloud.saas.tenant.service.query.OrgEmployeeQuery;
import net.bncloud.saas.tenant.service.query.OrgMemberQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenant/org/emp")
public class OrgEmployeeResource {

    private final OrgEmployeeService orgEmployeeService;

    public OrgEmployeeResource(OrgEmployeeService orgEmployeeService) {
        this.orgEmployeeService = orgEmployeeService;
    }

    /**
     * 人员选择器，分页查询接口
     *
     * @param pageable 分页参数
     * @param query    查询参数
     * @return
     */
    @PostMapping("/pageQuery")
    public R<Page<OrgEmployee>> pageQuery(Pageable pageable, @RequestBody OrgEmployeeQuery query) {
        return R.data(orgEmployeeService.pageQuery(query, pageable));
    }

    /**
     * 分页查询接口
     *
     * @param pageable 分页对象
     * @param query    查询参数
     * @return 员工分页信息
     */
    @PostMapping("/manage/pageQuery")
    public R<Page<OrgEmployeeDTO>> managePageQuery(Pageable pageable, @RequestBody QueryParam<OrgEmployeeQuery> query) {
        return R.data(orgEmployeeService.managePageQuery(query, pageable));
    }

    /**
     * 获取详情
     *
     * @return 员工详情
     */
    @GetMapping("/get/{employeeId}")
    public R<OrgEmployeeDTO> get(@PathVariable Long employeeId) {
        return R.data(orgEmployeeService.get(employeeId));
    }


    /**
     * 获取详情
     *
     * @return 员工详情
     */
    @GetMapping("/getPopoverInfo/{userId}")
    public R<OrgEmployeeDTO> getPopoverInfo(@PathVariable Long userId) {
        return R.data(orgEmployeeService.getPopoverInfo(userId));
    }


    /**
     * 新增员工
     *
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody CreateOrgEmployeeCommand command) {
        orgEmployeeService.addEmployee(command);
        return R.success();
    }

    /**
     * 批量编辑员工
     *
     * @return
     */
    @ApiOperation("批量编辑员工")
    @PostMapping("/editBatch")
    public R<Void> editBatch(@Validated @RequestBody EditBatchOrgEmployeeCommand command) {
        orgEmployeeService.editBatchEmployee(command);
        return R.success();
    }

    /**
     * 编辑员工信息
     *
     * @return
     */
    @ApiOperation("编辑员工信息")
    @PostMapping("/edit")
    public R<Void> edit(@Validated @RequestBody EditOrgEmployeeCommand command) {
        orgEmployeeService.edit(command);
        return R.success();
    }

    /**
     * 编辑员工用户信息
     *
     * @return
     */
    @ApiOperation("编辑员工用户信息")
    @PostMapping("/editEmpUserInfo")
    public R<Void> editEmpUserInfo(@Validated @RequestBody EditUserInfoCommand command) {
        orgEmployeeService.editEmpUserInfo(command);
        return R.success();
    }


    @ApiOperation(value = "新增成员(采购方)")
    @PostMapping("/createMember")
    public R createMember(@RequestBody BatchZltPurchaserStaffVO vo) {
        orgEmployeeService.createMember(vo);
        return R.success();
    }


    @ApiOperation(value = "[成员管理]协助组织成员表格", notes = "根据类型查询管理员/普通成员,全部用户")
    @PostMapping("/purchaserMemberTable")
    public R orgMemberTable(@RequestBody QueryParam<OrgMemberQuery> queryParam, Pageable pageable) {
        return R.data(orgEmployeeService.orgMemberTable(queryParam, pageable));
    }

    @ApiOperation(value = "[成员管理]移除组织成员")
    @DeleteMapping("/removePurchaserMember")
    public R removePurchaserMember(@RequestBody RemoveOrgMemberVO vo) {
        orgEmployeeService.batchRemovePurchaserMember(vo);
        return R.success();
    }

    @ApiOperation(value = "组织成员详情")
    @GetMapping("/memberDetail/{id}")
    public R memberDetail(@PathVariable Long id) {
        return R.data(orgEmployeeService.memberDetail(id));
    }

    @ApiOperation(value = "[人员管理]成员详情回显")
    @GetMapping("/editInfo")
    public R editInfo(@RequestParam Long id) {
        return R.data(orgEmployeeService.editInfo(id));
    }


    @ApiOperation(value = "[人员管理]成员详情更新")
    @PutMapping("/update/info")
    public R updateInfo(@RequestBody UserMemberVO vo) {
        orgEmployeeService.updateInfo(vo);
        return R.success();
    }


    @ApiOperation(value = "[首页]加载所在组织列表")
    @GetMapping("/loadRelateOrgs")
    public R loadRelateOrgs() {
        return R.data(orgEmployeeService.loadRelateOrgs());
    }


}
