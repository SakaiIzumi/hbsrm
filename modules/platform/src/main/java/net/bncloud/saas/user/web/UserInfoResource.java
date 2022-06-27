package net.bncloud.saas.user.web;


import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.LoginException;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.RoleRepository;
import net.bncloud.saas.authorize.service.UserService;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.service.OrganizationService;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import net.bncloud.saas.user.service.command.UserRelateInfoUpdateCommand;
import net.bncloud.saas.user.service.dto.CurrentUserInfoDTO;
import net.bncloud.saas.user.service.dto.EditContactDTO;
import net.bncloud.saas.user.service.query.UserInfoQuery;
import net.bncloud.saas.user.strategy.switchmenunav.ChangeWorkbenchStrategyContext;
import net.bncloud.saas.user.web.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-center/users")
@AllArgsConstructor
public class UserInfoResource {

    private final UserInfoService userInfoService;
    private final UserService userService;
    private final SupplierService supplierService;
    private final OrganizationService organizationService;
    private final RoleRepository roleRepository;
    private final ChangeWorkbenchStrategyContext changeWorkbenchStrategyContext;

    public R createUser(@RequestBody UserCreatePayload payload) {
        UserCreateCommand userCreateCommand = payload.toCommand();

        return null;
    }

    @ApiOperation("发送手机验证码")
    @PostMapping("/sendRegMobileCode")
    public R<Void> sendRegMobileCode(@RequestBody SendRegMobilePayload payload) {
        userInfoService.sendRegMobileCode(payload.getMobile(), payload.getStateCode());
        return R.success();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public R<UserInfo> registerUser(@RequestBody UserRegisterPayload payload) {
        UserInfo user = userInfoService.register(payload.toCommand());
        return R.data(user);
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public R<Object> changePassword(@RequestBody ChangePasswordPayload payload) throws LoginException {


        UserInfo userInfo = userInfoService.changePassword(payload);
        if (userInfo!=null) {
           throw new LoginException(401,"修改密码成功,请重新登录");
        }

        return R.fail("修改密码失败");

    }

    @ApiOperation("通过手机号查询用户")
    @PostMapping("/getByMobile")
    public R<UserInfo> getByMobile(@RequestBody QueryByMobilePayload payload) {
        return R.data(userInfoService.getUserByMobile(payload.getMobile(), payload.getStateCode()));
    }

    @GetMapping("/current")
    public R<CurrentUserInfoDTO> currentUserInfo() {
        CurrentUserInfoDTO userInfo = userInfoService.currentUserInfo();
        return R.data(userInfo);
    }


    @GetMapping("/userInfo")
    public R<Object> userInfo() {
        return R.data(userInfoService.userInfo());
    }

    @PutMapping("/contact")
    public R<Void> updateContact(@RequestBody EditContactDTO dto) {
        userInfoService.updateContact(dto);
        return R.success();
    }

    @PostMapping("/getRolesByUserId")
    public R<Set<Long>> getRolesByUserId(@RequestBody Set<Long> roleids) {
        List<Role> roles = new ArrayList<>();
        for (Long aLong : roleids) {
            Role role = new Role();
            role.setId(new Long(aLong));
            roles.add(role);
        }
        List<User> userList = userService.getUserByRoles(roles);
        Set<Long> ids = new HashSet<Long>();
        for (User user : userList) {
            ids.add(user.getUserId());
        }

        return R.data(ids);
    }


    @PostMapping("/getRolesByOrgId")
    public R<Map<String, Object>> getRolesByOrgId(@RequestParam(value = "orgId") Long orgId, @RequestParam(value = "roleIdList") Set<Long> roleIdList) {

        Optional<Organization> organizationOptional = organizationService.findById(orgId);
        List<Role> roleList = roleRepository.findAllBySubjectTypeAndIdInOrSourceRoleIdIn(SubjectType.org, roleIdList, roleIdList);
        List<Long> roleIdListAll = roleList.stream().map(Role::getId).collect(Collectors.toList());
        Organization organization = organizationOptional.get();
        List<OrgEmployee> orgEmployeeList = supplierService.getRoleByOrganization(organization, roleIdListAll);

        List<Long> userIds = orgEmployeeList.stream().map(s -> s.getUser().getUserId()).distinct().collect(Collectors.toList());

        HashMap<String, Object> result = new HashMap<>();
        result.put("receiverName", organization.getName());
        result.put("userIdList", userIds);
        return R.data(result);
    }

    @PostMapping("/getRolesBySupplierCode")
    public R<Map<String, Object>> getRolesBySupplierCode(@RequestParam(value = "supplierCode") String supplierCode, @RequestParam(value = "roleIdList") Set<Long> roleIdList) {
        Supplier supplier = supplierService.getByCode(supplierCode);
        List<Role> roleList = roleRepository.findAllBySubjectTypeAndIdInOrSourceRoleIdIn(SubjectType.supplier, roleIdList, roleIdList);
        List<Long> roleIdListAll = roleList.stream().map(Role::getId).collect(Collectors.toList());
        List<SupplierStaff> supplierStaffList = supplierService.getRoleBySupplier(supplier, roleIdListAll);
        List<Long> userList = supplierStaffList.stream().map(s -> s.getUser().getUserId()).distinct().collect(Collectors.toList());


        HashMap<String, Object> result = new HashMap<>();
        result.put("receiverName", supplier.getName());
        result.put("userIdList", userList);
        return R.data(result);
    }


    @ApiOperation("账号分页查询")
    @PostMapping("/userInfoList")
    public R<Page<UserInfo>> pageQuery(@RequestBody UserInfoQuery query, Pageable pageable) {
        return R.data(userInfoService.pageQuery(query, pageable));
    }


    @ApiOperation("查询关联主体信息")
    @GetMapping("/relate_info")
    public R relateInfo() {
        return R.data(userInfoService.relateInfo());
    }


    @ApiOperation("当前用户信息关联资料回显")
    @GetMapping("/relate_info_edit")
    public R relateInfoEdit(@RequestParam("userId") Long userId,
                            @RequestParam("subjectType") String subjectType,
                            @RequestParam("subjectId") Long subjectId
    ) {
        return R.data(userInfoService.relateInfoEdit(userId, subjectType, subjectId));
    }

    @ApiOperation("更新当前用户信息关联资料")
    @PutMapping("/relate_info_update")
    public R relateInfoUpdate(@RequestBody UserRelateInfoUpdateCommand command) {
        userInfoService.relateInfoUpdate(command);
        return R.success();
    }


    @ApiOperation(value = "切换工作台")
    @PutMapping("/changeWorkbench/{menuNavType}")
    public R changeWorkbench(@PathVariable String menuNavType) {
        changeWorkbenchStrategyContext.changeWorkbench(menuNavType);
        return R.success();
    }


    @PostMapping("/getUserInfoBySupplierIdAndUid")
    public R<List<Map<String, Object>>> getUserInfoBySupplierIdAndUid(@RequestParam("supplierCode") String supplierCode, @RequestParam("roleIdList") Set<Long> roleIdList) {
        Supplier supplier = supplierService.getByCode(supplierCode);
        List<Role> roleList = roleRepository.findAllBySubjectTypeAndIdInOrSourceRoleIdIn(SubjectType.supplier, roleIdList, roleIdList);
        List<Long> roleIdListAll = roleList.stream().map(Role::getId).collect(Collectors.toList());
        List<SupplierStaff> supplierStaffList = supplierService.getRoleBySupplier(supplier, roleIdListAll);
        List<Map<String, Object>> all = new ArrayList<>();
        supplierStaffList.stream().forEach(supplierStaff-> {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", supplierStaff.getUser().getUserId());
            result.put("mobile", supplierStaff.getMobile());
            result.put("name", supplierStaff.getName());
            all.add(result);
        });
        return R.data(all);
    }


    @PostMapping("/getUserInfoByUid")
    public R<Map<String, Object>> getUserInfoBySupplierIdAndUid(@RequestParam("uid") String uid) {
        UserInfo userById = userInfoService.getUserById(Long.valueOf(uid));
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("mobile", userById.getMobile());
        return R.data(result);
    }
}
