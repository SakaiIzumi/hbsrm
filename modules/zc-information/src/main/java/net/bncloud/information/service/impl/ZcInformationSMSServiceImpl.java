package net.bncloud.information.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.helper.DevelopEnvHelper;
import net.bncloud.common.helper.EnvHelper;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.information.entity.ZcInformationSMS;
import net.bncloud.information.mapper.ZcInformationSMSMapper;
import net.bncloud.information.service.IZcInformationSMSService;
import net.bncloud.sms.SendSmsMsg;
import net.bncloud.utils.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 智采消息表 服务实现类
 * @author dr
 */
@Service
public class ZcInformationSMSServiceImpl extends BaseServiceImpl<ZcInformationSMSMapper, ZcInformationSMS> implements IZcInformationSMSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZcInformationSMSServiceImpl.class);

    @Autowired
    private DevelopEnvHelper developEnvHelper;

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private ZcInformationSMSMapper zcInformationSMSMapper;

    @Resource
    private SendSmsMsg sendSmsMsg;

    @Autowired
    private EnvHelper envHelper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInformationSMS(SendMsgParam sendMsgParam) {
        LOGGER.info("========开始发送消息短信===========");
        LOGGER.info("参数为:{}",JSON.toJSONString(sendMsgParam));
        try {
            Object dataJson = sendMsgParam.getDataJson();
            String s = String.valueOf(dataJson);
            JSONObject jsonObject = JSON.parseObject(s);
//            LinkedHashMap<String,Object> dataJson =(LinkedHashMap<String,Object>) sendMsgParam.getDataJson();
//            String s = JSON.toJSONString(dataJson1);
//            DataJsonVo parse = (DataJsonVo)JSON.parse(s);
//            System.out.println(dataJson1);
//            DataJsonVo dataJson = (DataJsonVo)sendMsgParam.getDataJson();
//            JSONObject jsonObject = JSON.parseObject(dataJson);
            List<Map<String,Object>> userInfos = sendMsgParam.getUserInfos();
            //短信模板
            String smsTempCode = sendMsgParam.getSmsTempCode();
            //短信参数
            String smsParams = sendMsgParam.getSmsParams();
            //短信类型 1.普通短信消息2.短信验证码
            Integer smsMsgType = sendMsgParam.getSmsMsgType();
            for (Map<String,Object> map : userInfos){
                String mobile = (String)map.get("mobile");
                String uid = (String)map.get("uid");
                String name = (String)map.get("name");

                //获取配置环境，如果是开发环境，使用指定手机号
                LOGGER.info("information:传入的手机号为:{}",JSON.toJSONString(mobile));
                List<String> phoneList = developEnvHelper.transferSmsPhoneV2(mobile);
                LOGGER.info("information:得到的手机号集合为:{}",JSON.toJSONString(phoneList));
                for (String phone : phoneList) {
                    //发送短信
                    SendSmsResponse response = sendSmsMsg(phone, smsParams, smsTempCode);
                    LOGGER.info("response:{}",JSON.toJSONString(response));
                    if(ObjectUtil.isNotEmpty(response.getBody())&&StrUtil.isNotEmpty(response.getBody().getRequestId())){

                    }
                    //获取当前登录信息
                    BaseUserEntity user = AuthUtil.getUser();
                    ZcInformationSMS msg = createMsg(sendMsgParam,response,Long.valueOf(uid));
                    msg.setCreatedBy(user.getUserId());
                    msg.setLastModifiedBy(user.getUserId());
                    Date now = DateUtil.now();
                    msg.setCreatedDate(now);
                    msg.setLastModifiedDate(now);
                    msg.setIsDeleted(0);
                    msg.setGetName(name);
                    msg.setGetMobile(phone);
                    msg.setBusinessId(String.valueOf(jsonObject.get("quotationId")));
                    msg.setSendData(String.valueOf(jsonObject.get("quotationNo")));
                    msg.setSendTemp(smsTempCode);
                    msg.setSendMsgType(smsMsgType);

                    zcInformationSMSMapper.insert(msg);
                }
                //非生产环境跳出角色循环,只给测试手机号发送一次短信
                if ( envHelper.nonPro()) {
                    break;
                }
            }

        } catch (Exception e) {
            log.error("发送短信异常,{}",e);
            e.printStackTrace();
        }
        return R.success("发送成功");
    }

    public SendSmsResponse sendSmsMsg(String mobile,String params,String smsTempCode) throws Exception {
        SendSmsRequest sendParams  = sendSmsMsg.createSendParams(mobile, smsTempCode, params);
        Client client = sendSmsMsg.createClient();
        return sendSmsMsg.SendSmsMsg(sendParams,client);
    }


    //制作消息实体
    private ZcInformationSMS  createMsg(SendMsgParam param,SendSmsResponse response,Long getUser){
        //构建待入库消息集合
        ZcInformationSMS msg = new ZcInformationSMS();
        msg.setTag(param.getTag());
        msg.setAddTime(new Timestamp(System.currentTimeMillis()));
                    //接收者信息封装
                    msg.setGetUid(getUser);
                    //发送者信息封装
                    if(BeanUtilTwo.isNotEmpty(param.getSendUid())){
                        msg.setSendUid(param.getSendUid());
                        msg.setSendName(param.getSendUserName());
                        msg.setSendSupplierName(param.getSources());
                        msg.setSendSupplierNo(param.getSourcesCode());
                    }

                    BaseUserEntity user = AuthUtil.getUser();
                    msg.setCreatedDate(new Date());
                    msg.setCreatedBy(user.getUserId());
                    msg.setLastModifiedDate(new Date());
                    msg.setLastModifiedBy(user.getUserId());
                    Map<String,Object> map = null;
                    Object dataJson = param.getDataJson();//消息源数据
                    //转map并增强map（对应消息标签界面的消息模板，常改）
                    if(dataJson instanceof java.lang.String){
                        map = JSONObject.parseObject((String) dataJson);
                    }else{
                        map = JSONObject.parseObject(JSON.toJSONString(dataJson));
                    }
                   /* map.put("msgTitle",msg.getMsgTitle());
                    map.put("getUid",msg.getGetUid());
                    map.put("getName",msg.getGetName());
                    map.put("getMobile",msg.getGetMobile());
                    map.put("sendUid",param.getSendUid());
                    map.put("sendName",param.getSendUserName());

                    map.put("getSupplierNo",msg.getGetSupplierNo());
                    map.put("getSupplierName",msg.getGetSupplierName());
                    map.put("sendSupplierNo",param.getSourcesCode());
                    map.put("sendSupplierName",param.getSources());


                    map.put("sendSubjectName",param.getSources());
                    map.put("sendSubjectCode",param.getSourcesCode());
                    map.put("receiverSubjectName",param.getReceiver());
                    map.put("receiverSubjectCode",param.getReceiverCode());

                    map.put("addTime",msg.getAddTime().toString().substring(0,19));
*/

                    //前端直接显示的字段
                   /* JSONObject jsonValueObject = null;
                    try {
                        jsonValueObject = JSONObject.parseObject(param.getDataJson().toString());
                        //解析业务id
                        if (jsonValueObject.get("businessId")!=null) {
                            msg.setBusinessId(jsonValueObject.get("businessId").toString());
                        }
                    } catch (Exception e) {
                        log.error("短信消息模板解析失败",e);
                    }*/

                    msg.setSendSubjectName(param.getSources());
                    msg.setSendSubjectCode(param.getSourcesCode());
                    msg.setReceiverSubjectName(param.getReceiver());
                    msg.setReceiverSubjectCode(param.getReceiverCode());
                    if("OK".equals(response.getBody().getCode())){
                        msg.setSendStatus(1);
                    }else{
                        msg.setSendStatus(2);
                    }
                    String [] strArray=new String[2];
                    strArray[0]=response.getBody().getMessage();
                    strArray[1]=response.getBody().getRequestId();
                    msg.setResponsMsg(JSON.toJSONString(strArray));
        return msg;
    }

}
