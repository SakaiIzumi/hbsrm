package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeAddpreentryRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeFieldGrouplistRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeListRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeUpdateRequest;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeAddpreentryResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeFieldGrouplistResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeUpdateResponse;
import com.taobao.api.ApiException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 智能人事API
 */
public final class SmartWorkHelper {
    /**
     * 添加人员到企业待入职
     */
    public static OapiSmartworkHrmEmployeeAddpreentryResponse addPreEntry(OapiSmartworkHrmEmployeeAddpreentryRequest.PreEntryEmployeeAddParam param, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/addpreentry");
        OapiSmartworkHrmEmployeeAddpreentryRequest req = new OapiSmartworkHrmEmployeeAddpreentryRequest();
        req.setParam(param);
        return client.execute(req, accessToken);
    }

    /**
     * 获取员工花名册字段信息
     */
    public static OapiSmartworkHrmEmployeeListResponse employeeList(@Nonnull String userIdList, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/list");
        OapiSmartworkHrmEmployeeListRequest req = new OapiSmartworkHrmEmployeeListRequest();
        req.setUseridList(userIdList);
        return client.execute(req, accessToken);
    }

    public static OapiSmartworkHrmEmployeeListResponse employeeList(@Nonnull List<String> userIdList, String accessToken) throws ApiException {
        return employeeList(String.join(",", userIdList), accessToken);
    }

    /**
     * 查询花名册元数据
     */
    public static OapiSmartworkHrmEmployeeFieldGrouplistResponse employeeFieldGroupList(Long agentId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/field/grouplist");
        OapiSmartworkHrmEmployeeFieldGrouplistRequest req = new OapiSmartworkHrmEmployeeFieldGrouplistRequest();
        req.setAgentid(agentId);
        return client.execute(req, accessToken);
    }

    public static OapiSmartworkHrmEmployeeUpdateResponse updateEmployee(String param, Long agentId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/update");
        OapiSmartworkHrmEmployeeUpdateRequest req = new OapiSmartworkHrmEmployeeUpdateRequest();
        req.setParam(param);
        req.setAgentid(agentId);
        return client.execute(req, accessToken);
    }
}