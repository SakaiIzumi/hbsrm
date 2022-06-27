package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.base.BaseEntity;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_holiday_date")
public class HolidayDate extends BaseEntity {
    private static final long serialVersionUID = 6928416218563847538L;


    /**
     * 年 2022
     */
    private int year;
    /**
     * 年月 202206
     */
    private int month;
    /**
     * 年月日 20220630
     */
    private int date;
    /**
     * 年周 202226
     */
    private int yearWeek;
    /**
     * 年日 2022181
     */
    private int yearDay;

    /**
     * 2022  农历
     */
    private int lunarYear;
    private int lunarMonth;
    private int lunarDate;
    private int lunarYearDay;

    /**
     * 星期几 1-7
     */
    private int week;
    /**
     * 是不是周末  1-周末 2-不是周末
     */
    private int weekend;
    /**
     * 是不是工作日 1-工作日  2非工作日
     */
    private int workday;
    /**
     * 节假日代号，指向名称
     * see https://api.apihubs.cn/enum/get?type=holiday
     */
    private int holiday;
    /**
     * 非节假日 1-节假日 2-非节假日
     */
    private int holidayOr;
    /**
     * 非节假日调休  1-节假日调休 2-非节假日调休
     */
    private int holidayOvertime;
    /**
     * 非节日当天 1-节日当天 2-非节日当天
     */
    private int holidayToday;
    /**
     * 非法定节假日  1-法定节假日 2-非法定节假日
     */
    private int holidayLegal;
    /**
     * 非假期节假日  1-假期节日 2-非假期节日
     */
    private int holidayRecess;

    private String yearCn;
    private String monthCn;
    private String dateCn;
    private String yearWeekCn;
    private String yearDayCn;
    private String lunarYearCn;
    private String lunarMonthCn;
    private String lunarDateCn;
    private String lunarYearDayCn;
    private String weekCn;
    private String weekendCn;
    private String workdayCn;
    private String holidayCn;
    private String holidayOrCn;
    private String holidayOvertimeCn;
    private String holidayTodayCn;
    private String holidayLegalCn;
    private String holidayRecessCn;


}
