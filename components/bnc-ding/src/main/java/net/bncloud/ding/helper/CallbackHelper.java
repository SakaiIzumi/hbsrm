package net.bncloud.ding.helper;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class CallbackHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackHelper.class);
    /**
     * 注册业务事件回调接口
     */
    public static OapiCallBackRegisterCallBackResponse register(List<String> tags, String url, String aesKey, String token, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/register_call_back");
        OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
        request.setUrl(url);
        request.setAesKey(aesKey);
        request.setToken(token);
        request.setCallBackTag(tags);
        LOGGER.info("注册业务事件回调接口, request: {}", JSON.toJSONString(request));
        return client.execute(request, accessToken);
    }

    /**
     * 查询事件回调接口
     */
    public static OapiCallBackGetCallBackResponse getCallback(String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back");
        OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 更新事件回调接口
     */
    public static OapiCallBackUpdateCallBackResponse updateCallback(List<String> tags, String url, String aesKey, String token, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/update_call_back");
        OapiCallBackUpdateCallBackRequest request = new OapiCallBackUpdateCallBackRequest();
        request.setUrl(url);
        request.setAesKey(aesKey);
        request.setToken(token);
        request.setCallBackTag(tags);
        LOGGER.info("更新事件回调接口, request: {}", JSON.toJSONString(request));
        return client.execute(request, accessToken);
    }

    /**
     * 删除事件回调接口
     */
    public static OapiCallBackDeleteCallBackResponse deleteCallback(String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/delete_call_back");
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    /**
     * 获取回调失败的结果
     */
    public static OapiCallBackGetCallBackFailedResultResponse getCallbackFailedResult(String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back_failed_result");
        OapiCallBackGetCallBackFailedResultRequest request = new OapiCallBackGetCallBackFailedResultRequest();
        request.setHttpMethod("GET");
        return client.execute(request,accessToken);
    }
}