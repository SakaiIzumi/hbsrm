package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;

import java.util.List;

/**
 * 通讯录
 */
public final class UserHelper {
    /**
     * 创建用户
     * @param //code
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static OapiV2UserCreateResponse createUser(OapiV2UserCreateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/create");
        OapiV2UserCreateResponse response = client.execute(request, accessToken);
        return response;
    }

    /**
     * 根据用户id更新用户信息
     * @param request
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static OapiV2UserUpdateResponse updateUser(OapiV2UserUpdateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/update");
        OapiV2UserUpdateResponse response = client.execute(request, accessToken);
        return response;
    }

    /**
     * 根据用户id删除用户信息
     * @param request
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static Long deleteUser(OapiV2UserDeleteRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/delete");
        return client.execute(request, accessToken).getErrcode();
    }

    /**
     * 通过免登授权码和access_token获取用户的userid
     * @param code 免登授权码
     */
    public static OapiUserGetuserinfoResponse getUserInfo(String code, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 获取用户详情
     */
    public static OapiV2UserGetResponse getUserDetail(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest request = new OapiV2UserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 获取部门用户userid列表
     * @param deptId 部门id
     */
    public static List<String> getDeptMember(Long deptId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
        OapiUserListidRequest req = new OapiUserListidRequest();
        req.setDeptId(deptId);
        req.setHttpMethod("GET");
        OapiUserListidResponse response = client.execute(req, accessToken);
        return response.getResult().getUseridList();
    }

    /**
     * 获取部门用户
     */
    public static OapiUserListsimpleResponse getDeptUserSimpleList(Long deptId, Long offset, Long size, String accessToken) throws ApiException {
        if (offset == null) {
            offset = 0L;
        }
        if (size == null) {
            size = 10L;
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest request = new OapiUserListsimpleRequest();
        request.setDeptId(deptId);
        request.setCursor(offset);
        request.setSize(size);
        request.setHttpMethod("GET");

        OapiUserListsimpleResponse response = client.execute(request, accessToken);
        return response;
    }

    /**
     * 获取部门用户详情
     * @param deptId 获取的部门id，1表示根部门
     * @param offset 支持分页查询，与size参数同时设置时才生效，此参数代表偏移量，偏移量从0开始
     * @param size 支持分页查询，与offset参数同时设置时才生效，此参数代表分页大小，最大100
     * @param order 支持分页查询，部门成员的排序规则，默认 是按自定义排序；
     * entry_asc：代表按照进入部门的时间升序，
     * entry_desc：代表按照进入部门的时间降序，
     * modify_asc：代表按照部门信息修改时间升序，
     * modify_desc：代表按照部门信息修改时间降序，
     * custom：代表用户定义(未定义时按照拼音)排序
     */
    public static OapiV2UserListResponse getDeptUserDetails(Long deptId, Long offset, Long size, String order, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
        OapiV2UserListRequest request = new OapiV2UserListRequest();
        request.setDeptId(deptId);
        request.setCursor(offset);
        request.setSize(size);
        request.setOrderField(order);
        request.setHttpMethod("GET");
        OapiV2UserListResponse response = client.execute(request, accessToken);
        return response;
    }

    /**
     * 根据手机号获取userid
     * @param mobile 手机号
     */
    public static OapiV2UserGetbymobileResponse getUserByMobile(String mobile, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
        OapiV2UserGetbymobileRequest request = new OapiV2UserGetbymobileRequest();
        request.setMobile(mobile);
        return client.execute(request, accessToken);
    }
}