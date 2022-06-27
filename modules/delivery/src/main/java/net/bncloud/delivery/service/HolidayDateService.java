package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.bncloud.delivery.entity.HolidayDate;
import net.bncloud.delivery.vo.apihub.ApiHubResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/19
 **/
public interface HolidayDateService extends IService<HolidayDate> {
    /**
     * 批量保存
     * @param year
     * @param dateInfos
     */
    void batchSaveDateInfoList(Integer year, List<ApiHubResult.DateInfo> dateInfos);

    /**
     * 获取法定节假日数据(包括法定上班的数据)
     */
    Map<String,List<LocalDate>> selectAllHoliday();



}
