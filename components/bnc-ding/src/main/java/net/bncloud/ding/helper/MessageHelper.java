package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息
 */
public final class MessageHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHelper.class);

    public static OapiMessageCorpconversationAsyncsendV2Response messageAsyncSend(OapiMessageCorpconversationAsyncsendV2Request request, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        LOGGER.info(request.getMsg());
        LOGGER.info(request.getUseridList());
        LOGGER.info(request.getAgentId().toString());
        return client.execute(request, accessToken);
    }
}