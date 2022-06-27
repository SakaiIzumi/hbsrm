package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;

import java.util.List;

public final class RoleHelper {
    /**
     * 获取角色列表
     *
     * @param offset 分页偏移，默认值：0
     * @param size   分页大小，默认值：20，最大值200
     */
    public static OapiRoleListResponse getRolePage(Long offset, Long size, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/list");
        OapiRoleListRequest request = new OapiRoleListRequest();
        request.setOffset(offset);
        request.setSize(size);
        return client.execute(request, accessToken);
    }

    /**
     * 获取角色下的员工列表
     *
     * @param roleId 角色ID
     * @param offset 分页偏移，默认值：0
     * @param size   分页大小，默认值：20，最大值200
     */
    public static OapiRoleSimplelistResponse getRoleUsers(Long roleId, Long offset, Long size, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
        OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
        request.setRoleId(roleId);
        request.setOffset(offset);
        request.setSize(size);
        return client.execute(request, accessToken);
    }

    /**
     * 获取角色组
     *
     * @param groupId 角色组的Id
     */
    public static OapiRoleGetrolegroupResponse getRoleGroup(Long groupId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/getrolegroup");
        OapiRoleGetrolegroupRequest request = new OapiRoleGetrolegroupRequest();
        request.setGroupId(groupId);
        return client.execute(request, accessToken);
    }

    /**
     * 获取角色详情
     *
     * @param roleId 角色Id
     */
    public static OapiRoleGetroleResponse getRoleDetail(Long roleId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/getrole");
        OapiRoleGetroleRequest req = new OapiRoleGetroleRequest();
        req.setRoleId(roleId);
        return client.execute(req, accessToken);
    }

    /**
     * 创建角色
     *
     * @param roleName 角色名称
     * @param groupId  角色组id
     */
    public static Long addRole(String roleName, Long groupId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/role/add_role");
        OapiRoleAddRoleRequest req = new OapiRoleAddRoleRequest();
        req.setRoleName(roleName);
        req.setGroupId(groupId);
        OapiRoleAddRoleResponse rsp = client.execute(req, accessToken);
        return rsp.getRoleId();
    }

    /**
     * 更新角色
     *
     * @param roleId   角色id。“默认”分组内的角色不支持修改，包括：负责人、主管、主管理员、子管理员
     * @param roleName 角色名称
     */
    public static boolean updateRole(Long roleId, String roleName, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/role/update_role");
        OapiRoleUpdateRoleRequest req = new OapiRoleUpdateRoleRequest();
        req.setRoleId(roleId);
        req.setRoleName(roleName);
        OapiRoleUpdateRoleResponse rsp = client.execute(req, accessToken);
        return rsp.isSuccess();
    }

    /**
     * 删除角色
     * 删除角色前，需确保角色下面的员工没有被赋予这个角色
     *
     * @param roleId 角色id。“默认”分组内的角色不支持修改，包括：负责人、主管、主管理员、子管理员
     */
    public static boolean deleteRole(Long roleId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/deleterole");
        OapiRoleDeleteroleRequest request = new OapiRoleDeleteroleRequest();
        request.setRoleId(roleId);
        OapiRoleDeleteroleResponse response = client.execute(request, accessToken);
        return response.isSuccess();
    }

    /**
     * 创建角色组
     *
     * @param groupName 角色组名称
     */
    public static Long addRoleGroup(String groupName, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/role/add_role_group");
        OapiRoleAddrolegroupRequest req = new OapiRoleAddrolegroupRequest();
        req.setName("名称");
        OapiRoleAddrolegroupResponse rsp = client.execute(req, accessToken);
        return rsp.getGroupId();
    }

    /**
     * 批量增加员工角色
     *
     * @param roleIds 角色id list，最大列表长度：20，方法内做分片处理
     * @param userIds 员工id list，最大列表长度：20，方法内做分片处理
     */
    public static void addRolesForEmps(List<String> roleIds, List<String> userIds, String accessToken) throws ApiException {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("角色id不能为空");
        }
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("员工id不能为空");
        }
        List<List<String>> roleIdPartList = Lists.partition(roleIds, 20);
        List<List<String>> userIdPartList = Lists.partition(userIds, 20);
        for (List<String> roleIdList : roleIdPartList) {
            for (List<String> userIdList : userIdPartList) {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/addrolesforemps");
                OapiRoleAddrolesforempsRequest request = new OapiRoleAddrolesforempsRequest();
                request.setRoleIds(String.join(",", roleIdList));
                request.setUserIds(String.join(",", userIdList));
                client.execute(request, accessToken);
            }
        }
    }

    /**
     * 批量删除员工角色
     * @param roleIds 角色标签id，最大列表长度：20，方法内做分片处理
     * @param userIds 用户userId，最大列表长度：100，方法内做分片处理
     */
    public static void removeRolesForEmps(List<String> roleIds, List<String> userIds, String accessToken) throws ApiException {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("角色id不能为空");
        }
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("员工id不能为空");
        }
        List<List<String>> roleIdPartList = Lists.partition(roleIds, 20);
        List<List<String>> userIdPartList = Lists.partition(userIds, 100);
        for (List<String> roleIdList : roleIdPartList) {
            for (List<String> userIdList : userIdPartList) {

                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/removerolesforemps");
                OapiRoleRemoverolesforempsRequest request = new OapiRoleRemoverolesforempsRequest();
                request.setRoleIds(String.join(",", roleIdList));
                request.setUserIds(String.join(",", userIdList));
                client.execute(request, accessToken);
            }
        }
    }

    /**
     * 设定角色成员管理范围
     * @param userId 用户id
     * @param roleId 角色id，必须是用户已经加入的角色
     * @param deptIds 部门id列表，最多50个，不传则设置范围为默认值：所有人，方法内做分片处理
     */
    public static void updateRoleScope(String userId, Long roleId, List<String> deptIds, String accessToken) throws ApiException {
        if (deptIds == null || deptIds.size() <= 50) {
            doUpdateRoleScope(userId, roleId, deptIds, accessToken);
        } else {
            List<List<String>> deptIdListPart = Lists.partition(deptIds, 50);
            for (List<String> deptIdList : deptIdListPart) {
                doUpdateRoleScope(userId, roleId, deptIdList, accessToken);
            }
        }
    }

    private static void doUpdateRoleScope(String userId, Long roleId, List<String> deptIds, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/scope/update");
        OapiRoleScopeUpdateRequest req = new OapiRoleScopeUpdateRequest();
        req.setUserid(userId);
        req.setRoleId(roleId);
        if (deptIds != null && !deptIds.isEmpty()) {
            req.setDeptIds(String.join(",", deptIds));
        }
        req.setHttpMethod("POST");
        OapiRoleScopeUpdateResponse response = client.execute(req, accessToken);
    }
}