package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.Asserts;
import net.bncloud.delivery.entity.HolidayDate;
import net.bncloud.delivery.enums.HolidayDateEnum;
import net.bncloud.delivery.mapper.HolidayDateMapper;
import net.bncloud.delivery.service.HolidayDateService;
import net.bncloud.delivery.utils.BeanListCopyUtil;
import net.bncloud.delivery.utils.BuildDayUtil;
import net.bncloud.delivery.vo.apihub.ApiHubResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@Slf4j
@Service
public class HolidayDateServiceImpl extends ServiceImpl<HolidayDateMapper, HolidayDate> implements HolidayDateService {
    @Override
    public void batchSaveDateInfoList(Integer year, List<ApiHubResult.DateInfo> dateInfos) {

        // 删除数据库中已有的数据
        int delete = this.baseMapper.deleteWithYear(year);

        List<HolidayDate> holidayDateList = BeanListCopyUtil.copyListProperties(dateInfos, HolidayDate::new);

        holidayDateList.forEach(item->{
            item.setIsDeleted(0);
            item.setCreatedDate(new Date());
            item.setLastModifiedDate(new Date());
        });

        Asserts.isTrue( this.saveBatch(holidayDateList) ,"保存节假日数据失败！");
    }

    @Override
    public Map<String, List<LocalDate>> selectAllHoliday() {
        Map<String, List<LocalDate>> map=new HashMap<>();

        //取出法定节假日数据
        List<HolidayDate> holidayList = this.list(Wrappers
                .<HolidayDate>lambdaQuery()
                .eq(HolidayDate::getHolidayRecess, HolidayDateEnum.IS_HOLIDAY_RECESS.getInHoliday()));

        //取出法定需要上班的数据
        List<HolidayDate> list996 = this.list(Wrappers
                .<HolidayDate>lambdaQuery()
                .eq(HolidayDate::getWeekend, HolidayDateEnum.IS_WEEKEND.getInHoliday())
                .eq(HolidayDate::getWorkday, HolidayDateEnum.IS_WORKDAY.getInHoliday()))
                ;


        List<LocalDate> collectForHoliday = holidayList.stream().map(item -> {
            String s = item.getDate() + "";
            LocalDate localDate = BuildDayUtil.stringToLocalDateForHolidayDate(s);
            return localDate;
        }).collect(Collectors.toList());
        List<LocalDate> collectFor996 = list996.stream().map(item -> {
            return BuildDayUtil.stringToLocalDateForHolidayDate(item.getDate() + "");
        }).collect(Collectors.toList());


        map.put(HolidayDateEnum.HOLIDAY_DAY.getCode(),collectForHoliday);
        map.put(HolidayDateEnum.IS_996.getCode(),collectFor996);

        return map;
    }
}
