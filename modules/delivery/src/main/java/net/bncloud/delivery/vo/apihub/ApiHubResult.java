package net.bncloud.delivery.vo.apihub;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 接口坞返回结果
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@Data
public class ApiHubResult<T> implements Serializable {
    private static final long serialVersionUID = -1664464774829436884L;
    /**
     * 0 正常状态
     */
    private int code;
    /**
     * msg
     */
    private String msg;
    /**
     * 结果集
     */
    private T data;

    public boolean requestFail(){
        return this.code != 0;
    }

    public ApiHubResult() {
    }

    public ApiHubResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Data
    public static class PageInfo<T> implements Serializable {

        private static final long serialVersionUID = 2918867488632380659L;
        /**
         * 分页数据
         */
        private List<T> list;

        /**
         * 页
         */
        private Integer page;

        /**
         * 数
         */
        private Integer size;

        /**
         * 总的数据
         */
        private Integer total;


    }

    /**
     * 日期信息
     "year": 2022,
     "month": 202206,
     "date": 20220630,
     "yearweek": 202226,
     "yearday": 181,
     "lunar_year": 2022,
     "lunar_month": 202206,
     "lunar_date": 20220602,
     "lunar_yearday": 150,
     "week": 4,
     "weekend": 2,
     "workday": 1,
     "holiday": 10,
     "holiday_or": 10,
     "holiday_overtime": 10,
     "holiday_today": 2,
     "holiday_legal": 2,
     "holiday_recess": 2,
     "year_cn": "2022年",
     "month_cn": "2022年06月",
     "date_cn": "2022年06月30日",
     "yearweek_cn": "2022年第26周",
     "yearday_cn": "2022年第181天",
     "lunar_year_cn": "二零二二年",
     "lunar_month_cn": "二零二二年六月",
     "lunar_date_cn": "二零二二年六月初二",
     "lunar_yearday_cn": "2022年第150天",
     "week_cn": "星期四",
     "weekend_cn": "非周末",
     "workday_cn": "工作日",
     "holiday_cn": "非节假日",
     "holiday_or_cn": "非节假日",
     "holiday_overtime_cn": "非节假日调休",
     "holiday_today_cn": "非节日当天",
     "holiday_legal_cn": "非法定节假日",
     "holiday_recess_cn": "非假期节假日"
     */
    @Data
    public static class DateInfo implements Serializable {

        private static final long serialVersionUID = -698244630767910350L;
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
        @JsonProperty("yearweek")
        private int yearWeek;
        /**
         * 年日 2022181
         */
        @JsonProperty("yearday")
        private int yearDay;

        /**
         * 2022  农历
         */
        @JsonProperty("lunar_year")
        private int lunarYear;
        @JsonProperty("lunar_month")
        private int lunarMonth;
        @JsonProperty("lunar_date")
        private int lunarDate;
        @JsonProperty("lunar_yearday")
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
        @JsonProperty("holiday_or")
        private int holidayOr;
        /**
         * 非节假日调休  1-节假日调休 2-非节假日调休
         */
        @JsonProperty("holiday_overtime")
        private int holidayOvertime;
        /**
         * 非节日当天 1-节日当天 2-非节日当天
         */
        @JsonProperty("holiday_today")
        private int holidayToday;
        /**
         * 非法定节假日  1-法定节假日 2-非法定节假日
         */
        @JsonProperty("holiday_legal")
        private int holidayLegal;
        /**
         * 非假期节假日  1-假期节日 2-非假期节日
         */
        @JsonProperty("holiday_recess")
        private int holidayRecess;

        @JsonProperty("year_cn")
        private String yearCn;
        @JsonProperty("month_cn")
        private String monthCn;
        @JsonProperty("date_cn")
        private String dateCn;
        @JsonProperty("yearweek_cn")
        private String yearWeekCn;
        @JsonProperty("yearday_cn")
        private String yearDayCn;
        @JsonProperty("lunar_year_cn")
        private String lunarYearCn;
        @JsonProperty("lunar_month_cn")
        private String lunarMonthCn;
        @JsonProperty("lunar_date_cn")
        private String lunarDateCn;
        @JsonProperty("lunar_yearday_cn")
        private String lunarYearDayCn;
        @JsonProperty("week_cn")
        private String weekCn;
        @JsonProperty("weekend_cn")
        private String weekendCn;
        @JsonProperty("workday_cn")
        private String workdayCn;
        @JsonProperty("holiday_cn")
        private String holidayCn;
        @JsonProperty("holiday_or_cn")
        private String holidayOrCn;
        @JsonProperty("holiday_overtime_cn")
        private String holidayOvertimeCn;
        @JsonProperty("holiday_today_cn")
        private String holidayTodayCn;
        @JsonProperty("holiday_legal_cn")
        private String holidayLegalCn;
        @JsonProperty("holiday_recess_cn")
        private String holidayRecessCn;

    }

}
