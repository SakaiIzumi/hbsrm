package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.taobao.api.ApiException;

/**
 * 待办
 */
public final class WorkRecordHelper {
    /**
     * 发起待办
     */
    public static String addWorkRecord(OapiWorkrecordAddRequest request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
        OapiWorkrecordAddResponse rsp = client.execute(request, accessToken);
        return rsp.getRecordId();
    }

    /**
     * 更新待办<br/>
     * 企业可以调用该接口更新待办事项状态，调用成功后，该待办事项在该用户的“待办事项”列表页面中消失
     * @param userId 待办事项对应的用户id
     * @param recordId 待办事项唯一id
     */
    public static boolean updateWorkRecord(String userId, String recordId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/update");
        OapiWorkrecordUpdateRequest req = new OapiWorkrecordUpdateRequest();
        req.setUserid(userId);
        req.setRecordId(recordId);
        OapiWorkrecordUpdateResponse rsp = client.execute(req, accessToken);
        return rsp.getResult() != null && rsp.getResult();
    }

    /**
     * 获取用户待办事项
     * @param userId 待办事项对应的用户id
     * @param offset 分页游标，从0开始，如返回结果中has_more为true，则表示还有数据，offset再传上一次的offset+limit
     * @param limit 每次请求可获取的最大待办数量，最多传50
     * @param status 待办事项状态，0表示未完成，1表示完成
     */
    public static OapiWorkrecordGetbyuseridResponse.PageResult getWorkRecordsByUserId(String userId, Long offset, Long limit, Long status, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/getbyuserid");
        OapiWorkrecordGetbyuseridRequest req = new OapiWorkrecordGetbyuseridRequest();
        req.setUserid(userId);
        req.setOffset(offset);
        req.setLimit(limit);
        req.setStatus(status);
        OapiWorkrecordGetbyuseridResponse rsp = client.execute(req, accessToken);
        return rsp.getRecords();
    }
}