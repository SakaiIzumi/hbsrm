package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;

import java.util.List;

/**
 * 部门相关API  2.0
 */
public final class DepartmentHelper {
    /**
     * 创建部门
     * @return
     */
    public static OapiV2DepartmentCreateResponse createDepartment(OapiV2DepartmentCreateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/create");
        return client.execute(request, accessToken);
    }

    /**
     * 更新部门
     */
    public static Long updateDepartment(OapiV2DepartmentUpdateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/update");
        OapiV2DepartmentUpdateResponse response = client.execute(request, accessToken);
        return response.getErrcode();
    }

    /**
     * 删除部门
     */
    public static Long deleteDepartment(OapiV2DepartmentDeleteRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/delete");
        return client.execute(request, accessToken).getErrcode();
    }

    /**
     * 获取子部门ID列表
     */
    public static OapiV2DepartmentListsubidResponse getSubDeptIdList(Long id, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsubid");
        OapiV2DepartmentListsubidRequest request = new OapiV2DepartmentListsubidRequest();
        request.setDeptId(id);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 获取部门列表
     * @param id 父部门id（如果不传，默认部门为根部门，根部门ID为1）
     * @param lang 通讯录语言（默认zh_CN，未来会支持en_US）
     * @param fetchChild 是否递归部门的全部子部门
     */
    public static OapiV2DepartmentListsubResponse getDeptList(Long id, String lang, Boolean fetchChild, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        request.setDeptId(id);
        if (lang != null && !lang.isEmpty()) {
            request.setLanguage(lang);
        }
//        if (fetchChild != null) {
//            request.setFetchChild(fetchChild);
//        }
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 获取部门详情
     */
    public static OapiV2DepartmentGetResponse getDeptDetail(Long id, String lang, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
        OapiV2DepartmentGetRequest request = new OapiV2DepartmentGetRequest();
        request.setDeptId(id);
        if (lang != null && !lang.isEmpty()) {
            request.setLanguage(lang);
        }
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 查询部门的所有上级父部门路径
     */
    public static List<Long> listParentDeptsByDept(Long id, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listparentbydept");
        OapiV2DepartmentListparentbydeptRequest request = new OapiV2DepartmentListparentbydeptRequest();
        request.setDeptId(id);
        request.setHttpMethod("GET");
        OapiV2DepartmentListparentbydeptResponse response = client.execute(request, accessToken);
        return response.getResult().getParentIdList();
    }

    /**
     * 查询指定用户的所有上级父部门路径
     * @param userId 用户Id
     */
    public static OapiV2DepartmentListparentbyuserResponse listParentDeptsByUser(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listparentbyuser");
        OapiV2DepartmentListparentbyuserRequest request = new OapiV2DepartmentListparentbyuserRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }
}
