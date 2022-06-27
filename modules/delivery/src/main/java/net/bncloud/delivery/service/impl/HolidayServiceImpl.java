package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.delivery.entity.SupplierDeliveryConfig;
import net.bncloud.delivery.enums.WorkBench;
import net.bncloud.delivery.param.HolidayAndSubscribeConfigParam;
import net.bncloud.delivery.service.HolidayService;
import net.bncloud.delivery.service.SupplierDeliveryConfigService;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.dto.MrpCfgDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {
    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;
    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;
    @Resource
    private  SupplierDeliveryConfigService supplierDeliveryConfigService;


    /**
     * 查询默认工作日配置
     * @return
     */
    public Map<String,Integer> getDefaultWorkDayConfig(String code) {
        R<CfgParamDTO> response = cfgParamResourceFeignClient.getParamByCode(code);
        //R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.MRP_DEFAULT_WORKDAY,orgId );
        Asserts.isTrue( response.isSuccess(),response.getMsg() );
        CfgParamDTO config = response.getData();
        Asserts.notNull( config,"获取默认工作日配置失败！" );
        String value = config.getValue();
        Asserts.isTrue( StrUtil.isNotBlank( value),"获取默认工作日配置失败！" );
        Map<String,Integer> hashMap=analyzeWorkDay(value);

        return hashMap;
    }


    private Map<String, Integer> analyzeWorkDay(String value) {
        Map<String,Integer> hashMap = new HashMap();
        JSONObject jsonObject = JSON.parseObject( value);
        // 获取值
        try {
            String start = jsonObject.getString("start");
            String end = jsonObject.getString("end");

            hashMap.put("start",Integer.valueOf(start));
            hashMap.put("end",Integer.valueOf(end));
        } catch (Exception ex){
            log.error("解析默认工作日配置失败。",ex);
            throw new ApiException("解析默认工作日配置失败！");
        }
        return hashMap;
    }

    /**
     * 查询是否订阅节假日配置
     * @return
     */
    public boolean getSubscribeConfig(String code,String platformCode,String supplierCode) {

        R<CfgParamDTO> response = cfgParamResourceFeignClient.getParamByCode(code);
        //R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.MRP_DEFAULT_WORKDAY,orgId );
         Asserts.isTrue( response.isSuccess(),response.getMsg() );
        CfgParamDTO config = response.getData();
        Asserts.notNull( config,"获取默认配置失败！" );
        String value = config.getValue();
        Asserts.isTrue( StrUtil.isNotBlank( value),"获取默认工作日配置失败！" );
        JSONObject jsonObject = JSON.parseObject( value);

        //采购方
        if(StrUtil.equals(platformCode, WorkBench.PURCHASE.getCode())){
            String pur = jsonObject.getString("pur");
            if(pur.equals("true")){
                return true;
            }else{
                return false;
            }

        }else{
            //供应商
            String changer=null;
            JSONArray sup = jsonObject.getJSONArray("sup");
            for (int i = 0; i < sup.size(); i++) {
                JSONObject jsonObject1 = sup.getJSONObject(i);
                changer = jsonObject1.getString(supplierCode);
                if(changer!=null){
                    break;
                }
            }
            if(changer!=null){
                return true;
            }else{
                return false;
            }

        }



    }


    /**
     * 获取所有默认工作日和自动订阅的配置的方法
     * @param belongType 销售工作台 or 采购工作台
     * */
    @Override
    public HolidayAndSubscribeConfigParam getAllHolidayAndSubscribeConfig(String belongType) {
        R<MrpCfgDTO> allSubscribeConfig = cfgParamResourceFeignClient.getAllSubscribeConfig();
        Asserts.isTrue(allSubscribeConfig.isSuccess(),"远程获取默认工作日和自动订阅配置失败!"+allSubscribeConfig.getMsg());
        MrpCfgDTO dto = allSubscribeConfig.getData();
        String defaultWorkday = dto.getDefaultWorkday();
        String autoScribe = dto.getAutoScribe();

        //获取默认工作日配置
        Map<String, Integer> defaultWorkdayConfigMap = analyzeWorkDay(defaultWorkday);

        //获取自动订阅法定节假日配置
        Boolean jsonObject = Boolean.parseBoolean(autoScribe);

        HolidayAndSubscribeConfigParam holidayAndSubscribeConfigParam = new HolidayAndSubscribeConfigParam();
        holidayAndSubscribeConfigParam.setDefaultWorkdayConfigMap(defaultWorkdayConfigMap);
        holidayAndSubscribeConfigParam.setJsonObjectForAuto(jsonObject);

        //如果是销售工作台
        if (WorkBench.SUPPLIER.getCode().equals(belongType)) {
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                SupplierDeliveryConfig deliveryConfig = supplierDeliveryConfigService.getOne(Wrappers.<SupplierDeliveryConfig>lambdaQuery()
                        .eq(SupplierDeliveryConfig::getSupplierCode, supplier.getSupplierCode()));
                holidayAndSubscribeConfigParam.setJsonObjectForAuto(Boolean.parseBoolean(deliveryConfig.getValue()));
            });
        }

        return holidayAndSubscribeConfigParam;
    }


    ///**
    // * 判断采购/供应是否开启自动订阅节假日的方法
    // * @param jsonObjectForAuto 远程获取的配置的参数
    // * @param belongCode 所属编码
    // * */
    //public Boolean IsAutoSubscribe(JSONObject jsonObjectForAuto, String belongCode) {
    //    //当前的采购/供应的自动订阅开关开关
    //    String changer = jsonObjectForAuto.getString(belongCode);
    //    if(StrUtil.equals(changer,"true")){
    //        return true;
    //    }else{
    //        return false;
    //    }
    //
    //}

    /**
     *list的覆盖方法
     **/
    public List<LocalDate> cover(List<LocalDate> listCover, List<LocalDate> list){
        List<LocalDate> collect = list.stream()
                .filter(item -> !listCover.contains(item))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     *获取有重合的日期的方法
     **/
    @Override
    public List<LocalDate> getConflict(List<LocalDate> conflictList, List<LocalDate> defaultList) {
        List<LocalDate> collect = defaultList.stream()
                .filter(item -> conflictList.contains(item))
                .collect(Collectors.toList());

        return collect;
    }


}
