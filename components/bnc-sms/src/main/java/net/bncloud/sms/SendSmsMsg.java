package net.bncloud.sms;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaModel;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.configurationprocessor.json.JSONException;


public class SendSmsMsg {

    private String appId;

    private String appSecrete;

    private String sign;

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public  com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(appId)
                // 您的AccessKey Secret
                .setAccessKeySecret(appSecrete);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 处理参数
     * @param mobile
     * @param tempcode
     * @param templateParam
     * @return
     * @throws JSONException
     */
    public  SendSmsRequest createSendParams(String mobile,String tempcode,String templateParam) throws JSONException {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(mobile)
                .setSignName(sign)
                .setTemplateCode(tempcode)
                .setTemplateParam(templateParam);
        return sendSmsRequest;
    }

    public  SendSmsResponse SendSmsMsg(SendSmsRequest sendSmsRequest,com.aliyun.dysmsapi20170525.Client client) throws Exception {
        SendSmsResponse resp = client.sendSms(sendSmsRequest);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(resp)));
        //{"headers":{"access-control-allow-origin":"*","date":"Fri, 04 Mar 2022 01:17:20 GMT","content-length":"110","access-control-max-age":"172800","x-acs-request-id":"64416539-E18F-5DA8-9B7F-57EC7979BA30","access-control-allow-headers":"X-Requested-With, X-Sequence, _aop_secret, _aop_signature, x-acs-action, x-acs-version, x-acs-date, Content-Type","connection":"keep-alive","content-type":"application/json;charset\u003dutf-8","access-control-allow-methods":"POST, GET, OPTIONS, PUT, DELETE","x-acs-trace-id":"fa9f03fe47c29482bc34f2c5248d8cf8"},
        // "body":{"Message":"OK","RequestId":"64416539-E18F-5DA8-9B7F-57EC7979BA30","BizId":"438116046356640125^0","Code":"OK"}}
        return resp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecrete() {
        return appSecrete;
    }

    public void setAppSecrete(String appSecrete) {
        this.appSecrete = appSecrete;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}


