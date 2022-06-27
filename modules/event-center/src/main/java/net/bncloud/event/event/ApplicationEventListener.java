package net.bncloud.event.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.event.constants.SubjectType;
import net.bncloud.event.domain.EventDetail;
import net.bncloud.event.domain.EventType;
import net.bncloud.event.domain.vo.FromUser;
import net.bncloud.event.domain.vo.RoleVO;
import net.bncloud.event.feign.InformationClient;
import net.bncloud.event.feign.InformationSMSClient;
import net.bncloud.event.feign.SaasClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventListener.class);

    private final SaasClient saasClient;

    private final InformationClient informationClient;

    private final InformationSMSClient informationSMSClient;

    public ApplicationEventListener(SaasClient saasClient, InformationClient informationClient, InformationSMSClient informationSMSClient) {
        this.saasClient = saasClient;
        this.informationClient = informationClient;
        this.informationSMSClient = informationSMSClient;
    }
    @EventListener(EventDataStored.class)
    public void EventDataStoredListener(EventDataStored event) {
        EventDetail eventDetail = event.getEventDetail();
        EventType eventType = eventDetail.getEventType();
        SendMsgParam sendMsgParam = new SendMsgParam();
        if (eventType.isDisabled()) {
            LOGGER.info("事件禁用状态disable={}",true);
            return;
        }

        Long orgId = eventDetail.getOrgId(); //组织id
        String supplierCode = eventDetail.getSupplierCode(); //组织id
        String smsParams = eventDetail.getSmsParams();
        Integer smsMsgType = eventDetail.getSmsMsgType();
        String smsTempCode = eventDetail.getSmsTempCode();

        List<RoleVO> roleVOList =  eventType.getRoles();
        List<Long> roleIdList = roleVOList.stream().map(RoleVO::getRoleId).collect(Collectors.toList());
        R<Map<String,Object>> setR = null;


        sendMsgParam.setSources(event.getSources().getSourcesName());//消息来源
        sendMsgParam.setSourcesCode(event.getSources().getSourcesCode());
        if (eventType.getReceiverType().equals(SubjectType.org.getCode())) {
            LOGGER.info("接收主体为org={},发送主体为sup={}",orgId,supplierCode);
            LOGGER.info("关联角色={}",roleIdList.toString());
             setR = saasClient.getRolesByOrgId(orgId,new HashSet<>(roleIdList));
            sendMsgParam.setReceiverCode(orgId.toString());//接收方编码
            sendMsgParam.setReceiver(setR.getData().get("receiverName").toString());
        }
        else{
            LOGGER.info("接收主体为sup={},发送主体为org={}",supplierCode,orgId);
            LOGGER.info("关联角色={}",roleIdList.toString());
            setR = saasClient.getRolesBySupplierCode(supplierCode,new HashSet<>(roleIdList));
            sendMsgParam.setReceiverCode(supplierCode);//接收方编码
            sendMsgParam.setReceiver(setR.getData().get("receiverName").toString());
        }




        LOGGER.info("调用platfrom服务获取useridList={}",JSONObject.toJSONString(setR));

        FromUser fromUser = eventDetail.getUser();
        sendMsgParam.setTag(eventType.getTpl().getTag());
        List<Long> receiveList = new ArrayList<Long>((List<Long>)setR.getData().get("userIdList"));
        sendMsgParam.setReceives(receiveList);
        sendMsgParam.setSendUid(fromUser.getId());
        sendMsgParam.setSendUserName(fromUser.getName());
        sendMsgParam.setModule(eventType.getModule());
        List<Long> roles = new ArrayList<Long>();
        for (RoleVO role : eventType.getRoles()) {
            roles.add(role.getRoleId());
        }
        sendMsgParam.setRoles(roles);
        sendMsgParam.setDataJson(eventDetail.getDetailData().getData());
        LOGGER.info("消息接收用户={}",receiveList.toString());
//        LOGGER.info("消息sendMsgParam={}",JSONObject.toJSONString(sendMsgParam));
        if(!Objects.isNull(eventType.getBisType()) && eventType.getBisType() == 2){
            R<List<Map<String, Object>>> userInfoBySupplierIdAndUid = saasClient.getUserInfoBySupplierIdAndUid(supplierCode, new HashSet<>(roleIdList));
            sendMsgParam.setUserInfos(userInfoBySupplierIdAndUid.getData());
            sendMsgParam.setSmsParams(smsParams);
            sendMsgParam.setSmsMsgType(smsMsgType);
            sendMsgParam.setSmsTempCode(smsTempCode);
            LOGGER.info("发送手机短信消息,消息体:{}", JSON.toJSONString(sendMsgParam));
            informationSMSClient.save(sendMsgParam);
        }else{
            LOGGER.info("发送站内信息,消息体:{}", JSON.toJSONString(sendMsgParam));
            informationClient.save(sendMsgParam);
        }
        // TODO
    }


}
