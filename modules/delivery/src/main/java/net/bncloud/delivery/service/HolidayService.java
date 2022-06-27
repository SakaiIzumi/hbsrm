package net.bncloud.delivery.service;

import net.bncloud.delivery.param.HolidayAndSubscribeConfigParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HolidayService {
    /**
     * 查询默认配置
     * @return
     */
    Map<String,Integer> getDefaultWorkDayConfig(String code);

    /**
     * 查询默认配置
     * @return
     */
    boolean getSubscribeConfig(String code,String platformCode,String supplierCode);

    /**
     * 查询所有的配置
     * @return
     */
    HolidayAndSubscribeConfigParam getAllHolidayAndSubscribeConfig(String belongType);

    /**
     * 判断采购/供应是否开启自动订阅节假日的方法
     * @param belongType 远程获取的配置的参数
     * @param belongCode 所属编码
     * */
    //Boolean IsAutoSubscribe(JSONObject jsonObjectForAuto, String belongCode);

    /**
     *list的覆盖方法
     **/
    List<LocalDate> cover(List<LocalDate> listCover, List<LocalDate> list);


    List<LocalDate> getConflict(List<LocalDate> factoryVacationAutoSubscribeDisableVacationLocalDateList, List<LocalDate> localDateListForDefault);
}
