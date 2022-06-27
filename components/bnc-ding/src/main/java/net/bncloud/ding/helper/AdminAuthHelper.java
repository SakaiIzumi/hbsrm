package net.bncloud.ding.helper;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiSsoGettokenRequest;
import com.dingtalk.api.request.OapiSsoGetuserinfoRequest;
import com.dingtalk.api.response.OapiSsoGettokenResponse;
import com.dingtalk.api.response.OapiSsoGetuserinfoResponse;
import com.taobao.api.ApiException;

public final class AdminAuthHelper {
    /**
     * 获取应用后台免登的accessToken
     *
     * @param corpId     企业Id
     * @param corpSecret 这里必须填写专属的SSOsecret
     */
    public static String getAccessToken(String corpId, String corpSecret) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sso/gettoken");
        OapiSsoGettokenRequest request = new OapiSsoGettokenRequest();
        request.setCorpid(corpId);
        request.setCorpsecret(corpSecret);
        request.setHttpMethod("GET");
        OapiSsoGettokenResponse response = client.execute(request);
        return response.getAccessToken();
    }

    public static OapiSsoGetuserinfoResponse getAdminUserInfo(String code, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sso/getuserinfo");
        OapiSsoGetuserinfoRequest request = new OapiSsoGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        OapiSsoGetuserinfoResponse response = client.execute(request, accessToken);

        return response;
    }
}
