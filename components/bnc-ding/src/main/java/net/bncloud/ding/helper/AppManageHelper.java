package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMicroappListRequest;
import com.dingtalk.api.request.OapiMicroappSetVisibleScopesRequest;
import com.dingtalk.api.response.OapiMicroappListResponse;
import com.dingtalk.api.response.OapiMicroappSetVisibleScopesResponse;
import com.taobao.api.ApiException;

public class AppManageHelper {
    /**
     * 获取钉钉应用列表
     * @param request
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static OapiMicroappListResponse getMicroAppList(OapiMicroappListRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/microapp/list");
        return client.execute(request, accessToken);
    }

    /**
     * 设置钉钉应用的可见范围
     * @param request
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static OapiMicroappSetVisibleScopesResponse setVisibleScopes(OapiMicroappSetVisibleScopesRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/microapp/set_visible_scopes");
        return client.execute(request, accessToken);
    }
}