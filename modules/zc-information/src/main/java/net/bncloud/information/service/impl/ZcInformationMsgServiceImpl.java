package net.bncloud.information.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import main.java.net.bncloud.enums.ModularType;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.IResultCode;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.enums.SubjectType;
import net.bncloud.enums.SystemType;
import net.bncloud.information.Enum.InformationReadStatusEnum;
import net.bncloud.information.Enum.InformationStatusEnum;
import net.bncloud.information.Enum.MsgTypeEnum;
import net.bncloud.information.Enum.OperStatusEnum;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.entity.ZcInformationRoute;
import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.information.mapper.ZcInformationMsgMapper;
import net.bncloud.information.mapper.ZcInformationRouteMapper;
import net.bncloud.information.mapper.ZcInformationTagMapper;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.information.service.IZcInformationMsgService;
import net.bncloud.information.vo.InformationMsgStatisticsVo;
import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.information.vo.ZcInformationRouteParamVo;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static main.java.net.bncloud.enums.ModularType.CUSTOMER_MANAGEMENT;
import static main.java.net.bncloud.enums.ModularType.CUSTOMER_ORDER_COLLABORATION;
import static main.java.net.bncloud.enums.ModularType.PURCHASE_ORDER_COLLABORATION;
import static main.java.net.bncloud.enums.ModularType.PURCHASING_RECEIVING_COLLABORATION;
import static main.java.net.bncloud.enums.ModularType.SUPPLIER_MANAGEMENT;
import static main.java.net.bncloud.enums.ModularType.SUPPLY_DELIVERY_COLLABORATION;

/**
 * 智采消息表 服务实现类
 * @author dr
 */
@Service
public class ZcInformationMsgServiceImpl extends BaseServiceImpl<ZcInformationMsgMapper, ZcInformationMsg> implements IZcInformationMsgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZcInformationMsgServiceImpl.class);
    @Resource
    private ZcInformationTagMapper zcInformationTagMapper;
//    @Resource
//    private ZcInformationRoleMapper zcInformationRoleMapper;
    @Resource
    private ZcInformationMsgMapper zcInformationMsgMapper;

    @Resource
    private ZcInformationRouteMapper zcInformationRouteMapper;

    @Override
    public IPage<ZcInformationMsgVo> selectPage(IPage<ZcInformationMsgVo> page, QueryParam<ZcInformationMsgParam> queryParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        // notice.setTenantId(SecureUtil.getTenantId());

        return page.setRecords(baseMapper.selectListPage(page, queryParam));
    }

    @Override
    @Transactional
    public R saveInformationMsg(SendMsgParam sendMsgParam) {
        try {
            //获取当前登录信息
            BaseUserEntity user = AuthUtil.getUser();
            LOGGER.info("==========创建站内信消息体==============");
            List<ZcInformationMsg> msgs = createMsg(sendMsgParam);
            LOGGER.info("==========创建站内信消息体成功==============");
            for(ZcInformationMsg msg:msgs){
                msg.setCreatedBy(user.getUserId());
                msg.setLastModifiedBy(user.getUserId());
                Date now = DateUtil.now();
                msg.setCreatedDate(now);
                msg.setLastModifiedDate(now);
                msg.setIsDeleted(0);
                zcInformationMsgMapper.insert(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success("发送成功");
    }



    @Override
    public InformationMsgStatisticsVo statistics(Long userId, String sendType, String terminalType,String supCode) {
        QueryWrapper<ZcInformationMsg> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
               .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getReceiverSubjectCode,supCode)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode())
                .eq(ZcInformationMsg::getIsRead, InformationReadStatusEnum.UNREAD.getCode());


        int agentMessages = count(queryWrapper);

        QueryWrapper<ZcInformationMsg> queryWrapper2 = Wrappers.query();
        queryWrapper2.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.remind.getCode())
               .eq(ZcInformationMsg::getGetUid, userId)
                .eq(ZcInformationMsg::getReceiverSubjectCode, supCode)
                .eq(ZcInformationMsg::getTerminalType, terminalType)
                .eq(ZcInformationMsg::getSendType, sendType)
                .eq(ZcInformationMsg::getIsRead, InformationReadStatusEnum.UNREAD.getCode());

        int notice = count(queryWrapper2);

        QueryWrapper<ZcInformationMsg> queryWrapper3 = Wrappers.query();
        queryWrapper3.lambda().eq(ZcInformationMsg::getIsRead, InformationReadStatusEnum.UNREAD.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getReceiverSubjectCode,supCode)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getMsgType, MsgTypeEnum.remind.getCode());
        int UnReadCount = count(queryWrapper3);

        InformationMsgStatisticsVo informationMsgStatisticsVo = new InformationMsgStatisticsVo();
        informationMsgStatisticsVo.setAgentMessages(agentMessages);
        informationMsgStatisticsVo.setNotice(notice);
        informationMsgStatisticsVo.setUnReadCount(UnReadCount);
        return informationMsgStatisticsVo;
    }



    @Override
    public InformationMsgStatisticsVo statisticsOrg(Long userId, String sendType, String terminalType,Long orgId) {




        QueryWrapper<ZcInformationMsg> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getReceiverSubjectCode,orgId)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());


        int agentMessages = count(queryWrapper);

        QueryWrapper<ZcInformationMsg> queryWrapper2 = Wrappers.query();
        queryWrapper2.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.remind.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getReceiverSubjectCode,orgId)
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType);

        int notice = count(queryWrapper2);

        QueryWrapper<ZcInformationMsg> queryWrapper3 = Wrappers.query();
        queryWrapper3.lambda().eq(ZcInformationMsg::getIsRead, InformationReadStatusEnum.UNREAD.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getReceiverSubjectCode,orgId)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getMsgType, MsgTypeEnum.remind.getCode());
        int UnReadCount = count(queryWrapper3);

        InformationMsgStatisticsVo informationMsgStatisticsVo = new InformationMsgStatisticsVo();
        informationMsgStatisticsVo.setAgentMessages(agentMessages);
        informationMsgStatisticsVo.setNotice(notice);
        informationMsgStatisticsVo.setUnReadCount(UnReadCount);
        return informationMsgStatisticsVo;
    }

    @Override
    public List<ZcInformationRouteParamVo> getHandleUrl(Long id) {
        ZcInformationMsg zcInformationMsg = zcInformationMsgMapper.selectById(id);
        ZcInformationTag query = new ZcInformationTag();
        query.setTag(zcInformationMsg.getTag());
        ZcInformationTag zcInformationTags = zcInformationTagMapper.selectOne(Condition.getQueryWrapper(query));
        ZcInformationRoute zcInformationRouteQuery = new ZcInformationRoute();
        zcInformationRouteQuery.setTagId(zcInformationTags.getId());
        List<ZcInformationRoute> zcInformationRoutes = zcInformationRouteMapper.selectList(Condition.getQueryWrapper(zcInformationRouteQuery));
        List<ZcInformationRouteParamVo> zcInformationRouteParamVos = new ArrayList<ZcInformationRouteParamVo>();
        for (ZcInformationRoute zcInformationRoute : zcInformationRoutes) {
            String dataJson = zcInformationMsg.getDataJson();


            JSONObject paramJson = null;
            try {
                JSONObject jsonObject = JSONObject.parseObject(dataJson);
                paramJson = new JSONObject();

                String parameterTemplate = zcInformationRoute.getParameterTemplate();
                String[] split = parameterTemplate.split(",");
                for (String s : split) {
                    if (jsonObject.get(s)!=null) {
                        paramJson.put(s,jsonObject.get(s));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(zcInformationTags.getName()+"路由无参数,路由类型"+zcInformationRoute.getRouteType(),e);
            }

            ZcInformationRouteParamVo zcInformationRouteParamVo = new ZcInformationRouteParamVo();
            zcInformationRouteParamVo.setMsgId(id);
            zcInformationRouteParamVo.setRouteType(zcInformationRoute.getRouteType());
            zcInformationRouteParamVo.setRouteName(zcInformationRoute.getRouteUrl());
            zcInformationRouteParamVo.setParameter(paramJson);
            zcInformationRouteParamVos.add(zcInformationRouteParamVo);
        }

        //修改待办消息为已读状态
        zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
        zcInformationMsg.setOperStatus(OperStatusEnum.HANDLED.getCode());
        updateById(zcInformationMsg);

        return zcInformationRouteParamVos;
    }

    @Override
    public InformationMsgStatisticsVo waitHandle(Long userId, String sendType, String terminalType) {
        InformationMsgStatisticsVo informationMsgStatisticsVo = new InformationMsgStatisticsVo();
        //待办消息
             QueryWrapper<ZcInformationMsg> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,SUPPLIER_MANAGEMENT.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus,  OperStatusEnum.UNHANDLED.getCode());
        int supplierCount = count(queryWrapper); //合同协同
        informationMsgStatisticsVo.setSupplierCount(supplierCount);

        QueryWrapper<ZcInformationMsg> queryWrapper2 = Wrappers.query();
        queryWrapper2.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,CUSTOMER_MANAGEMENT.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());
        int customerCount = count(queryWrapper2);//客户协同
        informationMsgStatisticsVo.setCustomerCount(customerCount);
        QueryWrapper<ZcInformationMsg> queryWrapper3 = Wrappers.query();
        queryWrapper3.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,PURCHASE_ORDER_COLLABORATION.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());
        int purchaseOrderCollaborationCount = count(queryWrapper3);
        informationMsgStatisticsVo.setPurchaseOrderCollaborationCount(purchaseOrderCollaborationCount);
        QueryWrapper<ZcInformationMsg> queryWrapper4 = Wrappers.query();
        queryWrapper4.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,CUSTOMER_ORDER_COLLABORATION.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());
        int customerOrderCollaborationCount = count(queryWrapper4);
        informationMsgStatisticsVo.setCustomerOrderCollaborationCount(customerOrderCollaborationCount);

        QueryWrapper<ZcInformationMsg> queryWrapper5 = Wrappers.query();
        queryWrapper5.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,PURCHASING_RECEIVING_COLLABORATION.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());
        int purchasingReceivingCollaborationCount = count(queryWrapper5);
        informationMsgStatisticsVo.setPurchasingReceivingCollaborationCount(purchasingReceivingCollaborationCount);
        QueryWrapper<ZcInformationMsg> queryWrapper6 = Wrappers.query();
        queryWrapper6.lambda().eq(ZcInformationMsg::getMsgType, MsgTypeEnum.handle.getCode())
                .eq(ZcInformationMsg::getGetUid,userId)
                .eq(ZcInformationMsg::getModuleType,SUPPLY_DELIVERY_COLLABORATION.getCode())
                .eq(ZcInformationMsg::getTerminalType,terminalType)
                .eq(ZcInformationMsg::getSendType,sendType)
                .eq(ZcInformationMsg::getOperStatus, OperStatusEnum.UNHANDLED.getCode());
        int supplyDeliveryCollaborationCount = count(queryWrapper6);
        informationMsgStatisticsVo.setSupplyDeliveryCollaborationCount(supplyDeliveryCollaborationCount);




        return informationMsgStatisticsVo;
    }

    @Override
    public void changeStatus(Long id) {
        ZcInformationMsg zcInformationMsg = zcInformationMsgMapper.selectById(id);
//        if (zcInformationMsg.getIsRead().equals(InformationReadStatusEnum.READ.getCode())) {
//            zcInformationMsg.setIsRead(InformationReadStatusEnum.UNREAD.getCode());
//        }else{
//            zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
//        }
        zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
        zcInformationMsgMapper.updateById(zcInformationMsg);


    }

    @Override
    public void handlerInformation(HandlerMsgParam handlerMsgParam) {
        ZcInformationMsg query = new ZcInformationMsg();
        query.setTag(handlerMsgParam.getEventCode());
//        query.setIsRead(InformationReadStatusEnum.UNREAD.getCode());
        query.setOperStatus(OperStatusEnum.UNHANDLED.getCode());
        query.setMsgType(MsgTypeEnum.handle.getCode());
        query.setBusinessId(handlerMsgParam.getBusinessId());
        query.setReceiverType(handlerMsgParam.getReceiverType());
//        business_id
        QueryWrapper<ZcInformationMsg> queryWrapper = Condition.getQueryWrapper(query);
        List<ZcInformationMsg> zcInformationMsgs = zcInformationMsgMapper.selectList(queryWrapper);
        for (ZcInformationMsg zcInformationMsg : zcInformationMsgs) {
            zcInformationMsg.setIsRead(InformationReadStatusEnum.READ.getCode());
            zcInformationMsg.setOperStatus(OperStatusEnum.HANDLED.getCode());
            zcInformationMsgMapper.updateById(zcInformationMsg);
        }
    }


    //制作消息实体
        private List<ZcInformationMsg>  createMsg(SendMsgParam param){
            ZcInformationTag query = new ZcInformationTag();
            //构建待入库消息集合
            List<ZcInformationMsg> msgs = new ArrayList<>();

        for (String tagParam : param.getTag().split(",")) {
                query.setTag(tagParam);
                ZcInformationTag tag = zcInformationTagMapper.selectOne(Condition.getQueryWrapper(query));
                if (tag==null) {
                    LOGGER.error("消息标签为空");
                    throw new BizException(new IResultCode() {
                        //测试
                        @Override
                        public String getMessage() {
                            return "消息标签为空";
                        }

                        @Override
                        public int getCode() {
                            return 500;
                        }
                    });
                }
                //获取接收者集合
                List<Long> getUsers = new ArrayList<>();
                if(BeanUtilTwo.isNotEmpty(param.getReceives())){
                    getUsers=param.getReceives();
                    LOGGER.info("==========接受者集合=============={}",JSON.toJSONString(getUsers));
                }

                ZcInformationRoute zcInformationRoute = new ZcInformationRoute();
                zcInformationRoute.setDisabled(false);
                zcInformationRoute.setTagId(tag.getId());
                //消息tag失效
                if(tag.getStatus().equals(InformationStatusEnum.invalid)){
                    LOGGER.error("消息标签状态失效={}",tag.getName());
                    continue;
                }
                List<ZcInformationRoute> zcInformationRoutes = zcInformationRouteMapper.selectList(Condition.getQueryWrapper(zcInformationRoute));
                LOGGER.info("==========查询的路由=============={}",JSON.toJSONString(zcInformationRoutes));
                //emptyRoute
                if(zcInformationRoutes.size()==0){
                    ZcInformationRoute zcInformationRoute1 = new ZcInformationRoute();
                    zcInformationRoute1.setRouteType("1");
                    zcInformationRoute1.setDisabled(false);
                    zcInformationRoutes.add(zcInformationRoute1);
                }


                for (ZcInformationRoute informationRoute : zcInformationRoutes) {

                    if (informationRoute.getDisabled()) {
                        continue;
                    }
                    for(Long getUser:getUsers){
                            ZcInformationMsg msg = new ZcInformationMsg();
                            msg.setTag(param.getTag());
                            msg.setAddTime(new Timestamp(System.currentTimeMillis()));
                            msg.setTerminalType(informationRoute.getRouteType());
                            //接收者信息封装
                            msg.setGetUid(getUser);
                            //发送者信息封装
                            if(BeanUtilTwo.isNotEmpty(param.getSendUid())){
                                msg.setSendUid(param.getSendUid());
                                msg.setSendName(param.getSendUserName());
                                msg.setSendSupplierName(param.getSources());
                                msg.setSendSupplierNo(param.getSourcesCode());
                            }
                            msg.setMsgType(tag.getMsgType());
                            //发送接收模块转换
                            if (tag.getModuleType().equals(ModularType.SUPPLIER_MANAGEMENT.getCode())) {
                                msg.setModuleType(ModularType.CUSTOMER_MANAGEMENT.getCode());
                            } else if (tag.getModuleType().equals(ModularType.CUSTOMER_MANAGEMENT.getCode())) {
                                msg.setModuleType(ModularType.SUPPLIER_MANAGEMENT.getCode());
                            }else if(tag.getModuleType().equals(ModularType.PURCHASE_ORDER_COLLABORATION.getCode())){
                                msg.setModuleType(ModularType.CUSTOMER_ORDER_COLLABORATION.getCode());
                            }else if(tag.getModuleType().equals(ModularType.CUSTOMER_ORDER_COLLABORATION.getCode())){
                                msg.setModuleType(ModularType.PURCHASE_ORDER_COLLABORATION.getCode());
                            }else if(tag.getModuleType().equals(ModularType.PURCHASING_RECEIVING_COLLABORATION.getCode())){
                                msg.setModuleType(ModularType.SUPPLY_DELIVERY_COLLABORATION.getCode());
                            }else if(tag.getModuleType().equals( ModularType.SUPPLY_DELIVERY_COLLABORATION.getCode())) {
                                msg.setModuleType(ModularType.PURCHASING_RECEIVING_COLLABORATION.getCode());
                            }else if (tag.getModuleType().equals(ModularType.INQUIRY_AND_BIDDING_COLLABORATION.getCode())){
                                msg.setModuleType(ModularType.INQUIRY_AND_BIDDING_COLLABORATION.getCode());
                            }else{
                                log.error("转换失败");
                                msg.setModuleType(tag.getModuleType());
                            }

                            msg.setMsgTitle(tag.getName());
                            BaseUserEntity user = AuthUtil.getUser();
                            msg.setCreatedDate(new Date());
                            msg.setCreatedBy(user.getUserId());
                            msg.setLastModifiedDate(new Date());
                            msg.setLastModifiedBy(user.getUserId());
                            JSONObject json = JSON.parseObject(param.getDataJson().toString());
                            msg.setDataJson(JSONObject.toJSONString(json));
                            Map<String,Object> map = null;
                            Object dataJson = param.getDataJson();//消息源数据
                            //转map并增强map（对应消息标签界面的消息模板，常改）
                            if(dataJson instanceof java.lang.String){
                                map = JSONObject.parseObject((String) dataJson);
                            }else{
                                map = JSONObject.parseObject(JSON.toJSONString(dataJson));
                            }
                            map.put("msgTitle",msg.getMsgTitle());
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
                            if (StringUtil.isBlank(tag.getSystemType())) {
                                log.error(tag.getName()+"消息模板配置系统类型");
                                continue;
                            }
                            //智采 和 智易转换
                            map.put("systemType",tag.getSystemType().equals(SystemType.ZC.getCode())?SystemType.ZC.getCode():SystemType.ZY.getCode());
                            map.put("sendType",tag.getSendType().equals(SubjectType.org.getCode())?SubjectType.org.getCode():SubjectType.sup.getCode());
                            map.put("receivereType",tag.getSendType().equals(SubjectType.org.getCode())?SubjectType.sup.getCode():SubjectType.org.getCode());
                            //填充消息模板并封装消息体
                            String msgTemplate = tag.getMsgTemplate();
                            for(Map.Entry entry : map.entrySet()){
                                String mapKey = entry.getKey().toString();
                                Object mapValue = entry.getValue();
                                if(BeanUtilTwo.isNotEmpty(mapKey) && BeanUtilTwo.isNotEmpty(mapValue)){
                                    msgTemplate=msgTemplate.replaceAll("@"+mapKey+"@",mapValue.toString());
                                }
                            }
                            msg.setMsg(msgTemplate);

                            //前端直接显示的字段
                            String MessageValue= null;
                            JSONObject jsonValueObject = null;
                            try {
                                JSONObject jsonObject = JSONObject.parseObject(msgTemplate);
                                jsonValueObject = JSONObject.parseObject(param.getDataJson().toString());
                                if(StringUtil.isNotBlank(tag.getMessageTemplate())){
                                    MessageValue=tag.getMessageTemplate();
                                }else{
                                    MessageValue = jsonObject.get("消息内容").toString();
                                }
                                //解析业务id
                                if (jsonValueObject.get("businessId")!=null) {
                                    msg.setBusinessId(Long.valueOf(jsonValueObject.get("businessId").toString()));
                                }


                            } catch (Exception e) {
                                log.error(tag.getName()+"消息模板"+msgTemplate+"解析失败",e);
                                continue;
                            }

                            //如果模板字段存在不试用参数模板
                            LOGGER.info("==========替换模板==============");
                            for(Map.Entry entry : jsonValueObject.entrySet()){
                                String mapKey = entry.getKey().toString();
                                Object mapValue = entry.getValue();
                                if(BeanUtilTwo.isNotEmpty(mapKey) && BeanUtilTwo.isNotEmpty(mapValue)){
                                    MessageValue=MessageValue.replaceAll("@"+mapKey+"@",mapValue.toString());
                                }
                            }
                            msg.setMsgValue(MessageValue);
                            msg.setSystemType(tag.getSystemType().equals(SystemType.ZY.getCode())?SystemType.ZC.getCode():SystemType.ZY.getCode());
                            msg.setSendType(tag.getSendType().equals(SubjectType.org.getCode())?SubjectType.org.getCode():SubjectType.sup.getCode());
                            msg.setReceiverType(tag.getSendType().equals(SubjectType.org.getCode())?SubjectType.sup.getCode():SubjectType.org.getCode());
                            msg.setSendSubjectName(param.getSources());
                            msg.setSendSubjectCode(param.getSourcesCode());
                            msg.setReceiverSubjectName(param.getReceiver());
                            msg.setReceiverSubjectCode(param.getReceiverCode());
                            msgs.add(msg);
                         }
                    }
            }

            return msgs;
        }



//    //制作消息实体（弃用）
//    private List<ZcInformationMsg> createMsg(InformationParam param){
//        ZcInformationTag query = new ZcInformationTag();
//        query.setTag(param.getTag());
//        ZcInformationTag tag = zcInformationTagMapper.selectOne(Condition.getQueryWrapper(query));
//
//        //获取接收者集合
//        List<Long> getUsers = new ArrayList<>();
//        if(BeanUtilTwo.isNotEmpty(param.getGetUid())){
//            getUsers.add(param.getGetUid());
//        }else{
//            //TODO:还需根据后期基础逻辑的完善-根据 param.getGetSupplierNo() 和 tag.getRoles() 得到对应接收者集合
//        }
//        //获取接收终端集合
//        String[] terminals = tag.getTerminalType().split(",");
//
//        //构建待入库消息集合
//        List<ZcInformationMsg> msgs = new ArrayList<>();
//        for(String terminal:terminals){
//            String nowPrRoute="";
//            String[] strings = tag.getPrRoute().split(",");
//            for(String s:strings){
//                if(s.contains(terminal+"@@")){
//                    nowPrRoute=s.split("@@")[1];
//                }
//            }
//            for(Long getUser:getUsers){
//                ZcInformationMsg msg = new ZcInformationMsg();
//                msg.setTag(param.getTag());
//                msg.setClientId(param.getClientId());
//                if(BeanUtilTwo.isNotEmpty(param.getMsgId())){
//                    msg.setMsgId(param.getMsgId()+"@"+terminal+"@"+getUser);
//                }else{
//                    msg.setMsgId(tag.getMsgIdPrefix()+"@"+UUID.randomUUID()+"@"+terminal+"@"+getUser);
//                }
//                msg.setAddTime(new Timestamp(System.currentTimeMillis()));
//                msg.setTerminalType(Integer.parseInt(terminal));
//                //接收者信息封装
//                msg.setGetUid(getUser);
//                /**
//                 * TODO:还需根据后期基础逻辑的完善-添加 `get_name` varchar(255) DEFAULT NULL COMMENT '接收人名称',
//                 * TODO:还需根据后期基础逻辑的完善-添加 `get_mobile` varchar(50) DEFAULT NULL COMMENT '接收者手机号',
//                 */
//                msg.setGetSupplierNo(param.getGetSupplierNo());
//                /**
//                 * TODO:还需根据后期基础逻辑的完善-添加 `get_supplier_name` varchar(255) DEFAULT NULL COMMENT '接收者公司编码',
//                 */
//                //发送者信息封装
//                if(BeanUtilTwo.isNotEmpty(param.getSendUid())){
//                    msg.setSendUid(param.getSendUid());
//                    /**
//                     * TODO:还需根据后期基础逻辑的完善-添加 `send_name` varchar(255) DEFAULT NULL COMMENT '发送人名字',
//                     * TODO:还需根据后期基础逻辑的完善-添加 `send_mobile` varchar(50) DEFAULT NULL COMMENT '发送者或对应联系手机号',
//                     */
//                }
//                if(BeanUtilTwo.isNotEmpty(param.getSendSupplierNo())){
//                    msg.setSendSupplierNo(param.getSendSupplierNo());
//                    /**
//                     * TODO:还需根据后期基础逻辑的完善-添加 `send_supplier_name` varchar(255) DEFAULT NULL COMMENT '发送者或对应联系公司名称',
//                     */
//                }
//                msg.setMsgType(tag.getMsgType());
//                if(BeanUtilTwo.isNotEmpty(param.getMsgTitle())){
//                    msg.setMsgTitle(param.getMsgTitle());
//                }
//                BaseUserEntity user = AuthUtil.getUser();
//                msg.setCreatedDate(new Date());
//                msg.setCreatedBy(user.getUserId());
//                msg.setLastModifiedDate(new Date());
//                msg.setLastModifiedBy(user.getUserId());
//                msg.setDataJson(JSONObject.toJSONString(param.getDataJson()));
//
//                Map<String,Object> map = null;
//                Object dataJson = param.getDataJson();//消息源数据
//                //转map并增强map（对应消息标签界面的消息模板，常改）
//                if(dataJson instanceof java.lang.String){
//                    map = JSONObject.parseObject((String) dataJson);
//                }else{
//                    map = JSONObject.parseObject(JSON.toJSONString(dataJson));
//                }
//                map.put("msgTitle",msg.getMsgTitle());
//                map.put("getUid",msg.getGetUid());
//                map.put("getName",msg.getGetName());
//                map.put("getMobile",msg.getGetMobile());
//                map.put("getSupplierNo",msg.getGetSupplierNo());
//                map.put("getSupplierName",msg.getGetSupplierName());
//                map.put("sendUid",msg.getSendUid());
//                map.put("sendName",msg.getSendName());
//                map.put("sendMobile",msg.getSendMobile());
//                map.put("sendSupplierNo",msg.getSendSupplierNo());
//                map.put("sendSupplierName",msg.getSendSupplierName());
//                map.put("ddRoute",tag.getDdRoute());//钉钉路由
//                map.put("prRoute",nowPrRoute);//应用内路由
//                map.put("addTime",msg.getAddTime().toString().substring(0,19));
//
//                //填充消息模板并封装消息体
//                String msgTemplate = tag.getMsgTemplate();
//                for(Map.Entry entry : map.entrySet()){
//                    String mapKey = entry.getKey().toString();
//                    Object mapValue = entry.getValue();
//                    if(BeanUtilTwo.isNotEmpty(mapKey) && BeanUtilTwo.isNotEmpty(mapValue)){
//                        msgTemplate=msgTemplate.replaceAll("@"+mapKey+"@",mapValue.toString());
//                    }
//                }
//                msg.setMsg(msgTemplate);
//
//                msgs.add(msg);
//            }
//        }
//
//        return msgs;
//    }
public static void main(String[] args) {
    String jsonValue="{\"supplierName\":\"佛山市南海金橡源橡塑五金制品有限公司\",\"createdByName\":\"黄涛\",\"necessary\":\"Y\",\"lastModifiedDate\":1617703229000,\"lastModifiedBy\":13,\"customerCode\":\"020-1234\",\"contractStatusCode\":\"2\",\"supplierCode\":\"200-0261\",\"contractTypeId\":1370322275087310850,\"customerName\":\"商网云科技(深圳)有限公司\",\"expiryDate\":1649088000000,\"signedTime\":1617638400000,\"contractTitle\":\"采购合同HT1200129\",\"createdDate\":1617702853000,\"isDeleted\":0,\"createdBy\":13,\"validPeriodType\":\"2\",\"id\":1379371858856505345,\"contractCode\":\"HT1200129\",\"taxIncludedAmount\":100.00000000,\"excludingTaxAmount\":200.00000000}";

    String json ="{\"businessId\":\"1\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"客户：@customerName@于@addTime@，撤回合同：@contractCode@。请及时查看，创建人@createdBy@。\"}";

    JSONObject jsonObject = JSONObject.parseObject(json);
    //解析业务id
    if (jsonObject.get("businessId")!=null) {
        System.out.println(Long.valueOf(jsonObject.get("businessId").toString()));
    }else{
        System.out.println(123);
    }

//    JSONObject jsonObject = JSONObject.parseObject(json);
//    String MessageTemplate = jsonObject.get("消息内容").toString();
//    JSONObject jsonValueObject = JSONObject.parseObject(jsonValue);
//    for(Map.Entry entry : jsonValueObject.entrySet()){
//                    String mapKey = entry.getKey().toString();
//                    Object mapValue = entry.getValue();
//                    if(BeanUtilTwo.isNotEmpty(mapKey) && BeanUtilTwo.isNotEmpty(mapValue)){
//                        MessageTemplate=MessageTemplate.replaceAll("@"+mapKey+"@",mapValue.toString());
//                    }
//    }

//
//    String MessageTemplate= "123,1222,3";
//    for (String s : MessageTemplate.split(",")) {
//        System.out.println(s);
//    }




}
}
