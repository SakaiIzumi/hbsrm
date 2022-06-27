package net.bncloud.uaa.service;

import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponse;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import me.chanjar.weixin.common.util.http.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DingTalkService {

    @Value("${dingtalk.corp-id}")
    private String corpId;
    @Value("${dingtalk.app-key}")
    private String appKey;
    @Value("${dingtalk.app-secret}")
    private String appSecret;

    public String buildQrConnectUrl(String redirectUri, String state) {
        return String.format("https://login.dingtalk.com/oauth2/auth?response_type=code&scope=openid&prompt=consent&client_id=%s&redirect_uri=%s&state=%s",
                appKey,
                URIUtil.encodeURIComponent(redirectUri), StringUtils.trimToEmpty(state));
    }

    public String getCorpId() {
        return corpId;
    }

    public String getUserInfoByQrCode(String authCode) throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()

                //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
                .setClientId(appKey)

                //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
                .setClientSecret(appSecret)
                .setCode(authCode)
                .setGrantType("authorization_code");
        GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
        //获取用户个人token
        String accessToken = getUserTokenResponse.getBody().getAccessToken();
        return getUserinfoMobile(accessToken);
    }

    private String getUserinfoMobile(String accessToken) throws Exception {
        com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        final GetUserResponse response = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions());
        final GetUserResponseBody body = response.getBody();
        return body.getMobile();
    }

    public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }

    com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    private String getAccessToken() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        return response.getAccessToken();
    }

    public String getUserMobileByCode(String code) throws Exception {
        final String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
        System.out.println(response.getBody());
        // 查询得到当前用户的userId
        // 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
        String userId = response.getUserid();

        return getUserMobile(userId, accessToken);
    }

    private String getUserMobile(String userId, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        OapiV2UserGetResponse rsp = client.execute(req, accessToken);
        System.out.println(rsp.getBody());
        return rsp.getResult().getMobile();
    }
}
