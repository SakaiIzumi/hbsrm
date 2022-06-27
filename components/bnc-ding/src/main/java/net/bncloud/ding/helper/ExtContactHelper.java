package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;

/**
 * 外部联系人接口
 */
public final class ExtContactHelper {
    private ExtContactHelper() {}

    /**
     * 获取外部联系人标签列表
     */
    public static OapiExtcontactListlabelgroupsResponse listLabelGroups(Long offset, Long size, String accessToken) throws ApiException {
        if (offset == null || offset < 0L) {
            offset = 0L;
        }
        if (size == null || size < 0L) {
            size = 20L;
        }
        if (size > 100L) {
            size = 100L;
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/listlabelgroups");
        OapiExtcontactListlabelgroupsRequest request = new OapiExtcontactListlabelgroupsRequest();
        request.setOffset(offset);
        request.setSize(size);
        return client.execute(request, accessToken);
    }

    /**
     * 获取外部联系人列表
     */
    public static OapiExtcontactListResponse list(Long offset, Long size, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/list");
        OapiExtcontactListRequest request = new OapiExtcontactListRequest();
        request.setOffset(offset);
        request.setSize(size);
        return client.execute(request, accessToken);
    }

    /**
     * 获取外部联系人详情
     */
    public static OapiExtcontactGetResponse detail(String userId, String accessToken) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/get");
        OapiExtcontactGetRequest request = new OapiExtcontactGetRequest();
        request.setUserId(userId);
        return client.execute(request, accessToken);
    }

    /**
     * 添加外部联系人
     */
    public static OapiExtcontactCreateResponse create(OapiExtcontactCreateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/create");
        return client.execute(request, accessToken);
    }

    /**
     * 更新外部联系人
     */
    public static OapiExtcontactUpdateResponse update(OapiExtcontactUpdateRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/update");
        return client.execute(request, accessToken);
    }

    /**
     * 删除外部联系人
     */
    public static OapiExtcontactDeleteResponse delete(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/extcontact/delete");
        OapiExtcontactDeleteRequest request = new OapiExtcontactDeleteRequest();
        request.setUserId(userId);
        return client.execute(request, accessToken);
    }
}