
package net.bncloud.saas.authorize.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.security.UserAuthorize;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.authorize.service.command.CreateRoleGroupCommand;
import net.bncloud.saas.authorize.service.command.GrantMenuToRoleCommand;
import net.bncloud.saas.authorize.service.command.GrantRoleToUsers;
import net.bncloud.saas.authorize.service.dto.RoleBigDTO;
import net.bncloud.saas.authorize.service.dto.RoleDTO;
import net.bncloud.saas.authorize.service.dto.RoleGroupDTO;
import net.bncloud.saas.authorize.service.dto.RoleGroupTreeDTO;
import net.bncloud.saas.authorize.service.query.RoleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/authorize/role")
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    public R<RoleBigDTO> query(@PathVariable Long id) {
        return R.data(roleService.findById(id));
    }

    @PostMapping("/getUserIdListByRoleIdList")
    public R<Set<Long>> getUserIdListByRoleIdList(@RequestBody Set<Long> roleIdList) {
        return R.data(roleService.getUserIdListByRoleIdList(roleIdList));
    }

    @ApiOperation("返回全部的角色")
    @GetMapping("/role_list")
    public R<List<RoleDTO>> query(RoleQuery query) {
        return R.data(roleService.query(query));
    }

    @ApiOperation("返回全部的角色")
    @GetMapping("/queryEventRoleList")
    public R<List<RoleDTO>> queryEventRole(RoleQuery query) {
        return R.data(roleService.queryEventRole(query));
    }


    @ApiOperation("角色分页查询")
    @PostMapping("/role_page")
    public R<Page<RoleDTO>> pageQuery(@RequestBody RoleQuery query, Pageable pageable) {
        return R.data(roleService.pageQuery(query, pageable));
    }

    @ApiOperation("新增角色")
    @PostMapping("/add_role")
    public R<RoleBigDTO> create(@Validated @RequestBody RoleDTO resources) {
        RoleBigDTO roleDTO = roleService.createRole(resources);
        return R.data(roleDTO);
    }

    @ApiOperation("修改角色")
    @PutMapping("/update_role")
    public R<Void> update(@RequestBody RoleDTO resources) {
        roleService.update(resources);
        return R.success();
    }

    @PutMapping("/status/{id}")
    public R<RoleBigDTO> switchStatus(@PathVariable Long id) {
        roleService.switchRoleStatus(id);
        return R.data(roleService.findById(id));
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/delete_role/{id}")
    public R<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return R.success();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/bulk_delete_role")
    public R<Void> bulkDelete(@RequestBody Set<Long> ids) {
        roleService.bulkDeleteByIds(ids);
        return R.success();
    }

    @PostMapping("/grantPrivilege")
    public R<Void> grantPrivileges(@RequestBody GrantMenuToRoleCommand command) {
        roleService.grantPrivileges(command);
        return R.success();
    }

    @PostMapping("/grantToUsers")
    public R<Void> grantToUsers(@RequestBody GrantRoleToUsers command) {
        roleService.grantRoleToUsers(command);
        return R.success();
    }

    @ApiOperation("角色树")
    @GetMapping("/tree")
    public R<List<RoleGroupTreeDTO>> tree() {
        return R.data(roleService.tree());
    }

    @PostMapping("/add_group")
    public R<RoleGroupDTO> addRoleGroup(@RequestBody CreateRoleGroupCommand command) {
        return R.data(roleService.createRoleGroup(command.getName(), command.getType()));
    }

    @ApiOperation("修改角色组")
    @PutMapping("/update_group")
    public R<Void> updateGroup(@RequestBody RoleGroupDTO resources) {
        roleService.updateGroup(resources);
        return R.success();
    }

    @GetMapping("/group/{id}")
    public R<RoleGroupDTO> getGroupById(@PathVariable Long id) {
        return R.data(roleService.getGroupById(id));
    }

    @GetMapping("/group_list")
    public R<List<RoleGroupDTO>> groups(@RequestParam(name = "name", required = false) String name) {
        return R.data(roleService.groupList(name));
    }

    @PostMapping("/group_page")
    public R<Page<RoleGroupDTO>> groupPageQuery(Pageable pageable) {
        return R.data(roleService.groupPageQuery(pageable));
    }

    @DeleteMapping("/delete_group/{id}")
    public R<Void> deleteGroup(@PathVariable Long id) {
        roleService.deleteGroup(id);
        return R.success();
    }

//    @GetMapping("/getUserAuthorize")
//    public R<UserAuthorize> getRolesByUserId(@PathVariable Long userId) {
//        UserAuthorize userAuthorize = roleService.getUserRoles(userId);
//        return R.data(userAuthorize);
//    }
}
