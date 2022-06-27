package net.bncloud.quotation.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.ApplicationContextProvider;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.quotation.constant.QuotationConstant;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.enums.QuotationCfgParam;
import net.bncloud.quotation.enums.QuotationStatusEnum;
import net.bncloud.quotation.enums.SmsMsgTempEnum;
import net.bncloud.quotation.event.supplier.QuotationSupplierEvent;
import net.bncloud.quotation.event.vo.QuotationInfoEventData;
import net.bncloud.quotation.event.vo.QuotationInfoSupplierWarningSmsEvent;
import net.bncloud.quotation.event.vo.QuotationSupplierInfoEventData;
import net.bncloud.quotation.event.vo.QuotationWinnerInfoEvent;
import net.bncloud.quotation.mapper.QuotationBaseMapper;
import net.bncloud.quotation.mapper.QuotationSupplierMapper;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: QuotationSupplierAlert
 * @Description: 询价供应商预警
 * @author: YangYu
 * @date: 2022/3/4 13:58
 */
@Slf4j
@Component
public class QuotationSupplierAlert {

    @Resource
    private PurchaserFeignClient purchaserFeignClient;
    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;
    @Resource
    QuotationBaseMapper quotationBaseMapper;
    @Resource
    QuotationSupplierMapper quotationSupplierMapper;
    @Resource
    DefaultEventPublisher defaultEventPublisher;


    /**
     * 询价供应商预警方法
     */
    public void earlyWarningMethod() {

        //获取协同配置的设置时间和发送类型
        Boolean flag=null;//开关是否开启
        Long nearTime=1L;//默认1小时
        Integer type=1;//1 短信  2 站内信

        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(QuotationCfgParam.QUOTATION_SUPPLIER_EARLY_WARNING.getCode());
        if (!(listByCode.isSuccess())) {
            log.error("获取同步配置出现异常,{}",JSON.toJSONString(listByCode));
            throw new RuntimeException("获取同步配置出现异常");
        }

        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        List<Integer> integerList = new ArrayList<>();
        CfgParamDTO paramEntity = cfgParamDTOList.get(0);
        if (paramEntity != null) {
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串

            // 获取值
            nearTime= jsonObject.getLong("hour");
            flag= jsonObject.getBoolean("quotationSwitch");
            JSONArray data = jsonObject.getJSONArray("type");
            int size = data.size();


            for (int i = 0; i <size ; i++) {
                String s=(String) data.get(i);
                integerList.add(Integer.valueOf(s));

            }
            //System.out.println(integerList.toString());

        }
        if(nearTime==null){
            log.error("请填写配置预警时间,{}",JSON.toJSONString(nearTime));
            throw new ApiException(500,"请填写配置预警时间");
        }

        //查询当天所有打开预警的询价单
        List<QuotationBase> quotationBaseList = quotationBaseMapper.selectList(
                //查询是预警开启
                Wrappers.<QuotationBase>lambdaQuery()
                    .eq(QuotationBase::getSupplierWarningSwitch, "OPEN")//查询是预警开启
                    .eq(QuotationBase::getIsSendWarning, 0)//没有发送过预警信息
                    .and( base->base.eq( QuotationBase::getQuotationStatus,QuotationStatusEnum.QUOTATION.getCode()) )//报价状态
                    );


        log.info("需要发送预警的询价单为，{}",JSON.toJSONString(quotationBaseList));
        Long finalNearTime = nearTime;
        Integer finalType = type;
        for (QuotationBase e : quotationBaseList) {
            boolean b = getDatePoor(e.getCutOffTime(), new Date()) <= finalNearTime;
//            if (getDatePoor(e.getCutOffTime(), new Date()) <= QuotationConstant.QUOTATION_WARNING_TIME_SETTING  && getDatePoor(e.getCutOffTime(), new Date()) > 0) {
            if (getDatePoor(e.getCutOffTime(), new Date()) <= finalNearTime && getDatePoor(e.getCutOffTime(), new Date()) > 0) {
                //获取供应商
                List<QuotationSupplier> quotationSupplierList = quotationSupplierMapper.selectList(Wrappers.<QuotationSupplier>lambdaQuery()
                                .eq(QuotationSupplier::getQuotationBaseId, e.getId())//.eq(供应商确认状态)
                                 )
                                //根据供应商编号去从(避免重复发送预警消息)
                                .stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(quotationSupplier -> quotationSupplier.getSupplierCode()))), ArrayList::new));

                log.info("询价单的供应商为，{}",JSON.toJSONString(quotationSupplierList));
                //发送消息通知
                for (QuotationSupplier quotationSupplier : quotationSupplierList) {
                        //获取orgId
                        Long orgId=null;
                        OrgIdQuery orgIdQuery = new OrgIdQuery();
                        orgIdQuery.setSupplierCode(quotationSupplier.getSupplierCode());
                        orgIdQuery.setPurchaseCode(e.getCustomerCode());
                        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
                        if(orgIdDTO.isSuccess()){
                            orgId = orgIdDTO.getData().getOrgId();
                        }else{
                            log.info("获取orgId异常");
                            throw new ApiException(500,"获取orgId异常");
                        }

                        //构造loginInfo
                        LoginInfo loginInfo = new LoginInfo();
                        loginInfo.setId(1L);
                        loginInfo.setName( orgIdDTO.getData().getPurchaseName() );
                        Org org=new Org();
                        org.setId(orgId);
                        org.setName(orgIdDTO.getData().getPurchaseName());
                        loginInfo.setCurrentOrg(org);
                        List<Org> orgs=new ArrayList<Org>();
                        orgs.add(org);
                        loginInfo.setOrgs(orgs);
                        loginInfo.setCurrentOrg(org);

                        QuotationSupplierInfoEventData quotationSupplierInfoEventData = new QuotationSupplierInfoEventData();
                        quotationSupplierInfoEventData.setOrgId(orgId);
                        quotationSupplierInfoEventData.setSupplierCode(quotationSupplier.getSupplierCode());
                        quotationSupplierInfoEventData.setSupplierName(quotationSupplier.getSupplierName());
                        quotationSupplierInfoEventData.setQuotationBaseId(Long.getLong(e.getQuotationNo()));
                        quotationSupplierInfoEventData.setQuotationNo(e.getQuotationNo());
                        quotationSupplierInfoEventData.setBusinessId(e.getId());
                    log.info("预警的类型List:{}",JSON.toJSONString(integerList));
                    for (Integer integer : integerList) {
                        if(integer==2){//发送消息
                            log.info("发送站内信消息");
                            defaultEventPublisher.publishEvent(new QuotationSupplierEvent(this, loginInfo, quotationSupplierInfoEventData, e.getCustomerCode(),e.getCustomerName()));
                        }else if(integer==1){//发送短信
                            quotationSupplierInfoEventData.setSmsMsgType(1);
                            quotationSupplierInfoEventData.setSmsTempCode(SmsMsgTempEnum.SMS_235491612.getCode());
                            Map<String,Object> params =  new HashMap<>();
                            params.put("inquiryNo",e.getQuotationNo());
                            quotationSupplierInfoEventData.setSmsParams(JSON.toJSONString(params));
                            //发送短信
                            log.info("发送手机短信");
                            defaultEventPublisher.publishEvent(new QuotationInfoSupplierWarningSmsEvent(this,loginInfo,quotationSupplierInfoEventData, e.getCustomerCode(),e.getCustomerName()));

                        }
                    }
                    //将预警关闭(避免一直发送消息)
                    e.setIsSendWarning(1);
                    quotationBaseMapper.updateById(e);
                }
            }

        }
    }

    /**
     * 计算两时间相差小时数
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static Double getDatePoor(Date endDate, Date nowDate) {

        Double nd = 1000 * 24 * 60 * 60D;
        Double nh = 1000 * 60 * 60D;
        Double nm = 1000 * 60D;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        Double day = diff / nd;
        // 计算差多少小时
        Double hour = diff % nd / nh;
        // 计算差多少分钟
        Double min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return hour;
    }


}
