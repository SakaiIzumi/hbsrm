package net.bncloud.delivery.utils;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.enums.MrpVacationSourceTypeEnum;
import net.bncloud.delivery.enums.MrpVacationStatusEnum;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class BuildDayUtil {

    private static final Integer[] week ={1,2,3,4,5,6,7};

    private static Integer[] TWOYEARARRAY =new Integer[365*2];

    //返回星期数组的方法
    public static Integer[] returnWeekArray() {
        return week;
    }

    //根据星期开始和结束日返回放假的日子的数组
    public static List<Integer> returnHolidayForWeekArray(Integer start, Integer end) {
        //数组从零开始,所以要减1
        start=start-1;
        end=end-1;

        //相等时候的处理
        if(start.equals(end)){
            return oneDayEquals(start);
        }

        Integer[] holidayForWeek = new Integer[7];
        //是否在start-end的星期的循环中,默认不在,可以添加
        boolean flag=false;
        for (int i = 0,j=0; i < week.length; i++) {
            if(start.equals(i)&&!flag){
                flag=true;//进入循环
                continue;
            } else if(end.equals(i)&&flag){//到达end,跳出循环,flag改为false
                flag=false;
               continue;
            }

            if(!flag){
                holidayForWeek[j++]=week[i];
            }
        }

        //过滤为空的元素
        List<Integer> holidayList = filterArray(holidayForWeek);

        return holidayList;
    }

    private static List<Integer> oneDayEquals(Integer start) {
        List<Integer> holidayList = new ArrayList<>();
        int length = week.length;
        for (int i = 0; i <length; i++) {
            if(!week[i].equals(start)){
                holidayList.add(week[i]);
            }
        }
        return holidayList;
    }


    //过滤不为空的元素
    public static List<Integer> filterArray (Integer[] holidayForWeekFilter){

        List<Integer> holidayList = new ArrayList<>();
        for (int i = 0; i < holidayForWeekFilter.length; i++) {
            if(holidayForWeekFilter[i]!=null){
                holidayList.add(holidayForWeekFilter[i]);
            }
        }

        return holidayList;

    }

    //获取服务器当前日期
    public static LocalDate day(){
        LocalDate now = LocalDate.now();
        return now;
    }

    //获取传入的日期的年份
    public static String year(LocalDate date){
        int year = date.getYear();
        return year+"";
    }

    //根据传入的日期判断是星期几
    public static int weekOfDay(LocalDate date){
        int value = date.getDayOfWeek().getValue();
        return value;
    }

    //localdate和date 互换
    public static Date localdateToDate(LocalDate date){
        Date from = Date.from(date.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        return from;
    }

    //localdate和date 互换
    public static List<Date> localdateListToDateList(List<LocalDate> list){
        List<Date> dateList=new ArrayList<>();
        for (LocalDate localDate : list) {
            Date from = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            dateList.add(from);

        }
        return dateList;
    }

    //localdate和date 互换
    public static List<LocalDate> dateListToLocalDateList(List<Date> list){
        List<LocalDate> dateList=new ArrayList<>();
        for (Date date : list) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dateList.add(localDate);

        }
        return dateList;
    }

    //date和localdate 互换
    public static LocalDate dateToLocalDate(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    //日期字符串(不含时间)转成LocalDate
    public static LocalDate stringToLocalDate(String date){

        //String date="2022-05-18";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(date, dateTimeFormatter);
        //System.out.println(parse);
        return parse;
    }

    //日期字符串(不含时间)转成LocalDate(适应假期表的方法)
    public static LocalDate stringToLocalDateForHolidayDate(String date){

        //String date="2022-05-18";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate parse = LocalDate.parse(date, dateTimeFormatter);
        //System.out.println(parse);
        return parse;
    }

    //计算两个日期相差的天数,返回相差天数的LocalDate的list
    public static List<LocalDate> howManyDay(String startDate,String endDate){
        LocalDate start = BuildDayUtil.stringToLocalDate(startDate);
        LocalDate end = BuildDayUtil.stringToLocalDate(endDate);
        List<LocalDate> list =new ArrayList<>();
        while(end.isAfter(start)) {
            list.add(start);

            start = start.plusDays(1);
        }
        return list;
    }

    //计算两个日期相差的天数,返回相差天数的LocalDate的list
    public static List<Date> howManyDayInDateList(String startDate,String endDate){
        List<Date> dates = new ArrayList<>();
        LocalDate start = BuildDayUtil.stringToLocalDate(startDate);
        LocalDate end = BuildDayUtil.stringToLocalDate(endDate);
        if(start.isEqual(end)){
            Date dateForOne = BuildDayUtil.localdateToDate(start);
            dates.add(dateForOne);
            return dates;
        }
        List<LocalDate> list =new ArrayList<>();
        while(end.isAfter(start)||end.isEqual(start)) {
            list.add(start);

            start = start.plusDays(1);
        }


        for (LocalDate localDate : list) {
            Date date = BuildDayUtil.localdateToDate(localDate);
            dates.add(date);
        }
        return dates;
    }


    /**
     * 计算天数
     * @param bdate
     * @param edate
     * @return
     */
    public static int differentDays(String bdate, String edate) {
        try {
            //根据日期格式类型进行转化
            Date date1 = new SimpleDateFormat("yyyyMMdd").parse(bdate);
            Date date2 = new SimpleDateFormat("yyyyMMdd").parse(edate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            int day1= cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);

            int year1 = cal1.get(Calendar.YEAR);
            int year2 = cal2.get(Calendar.YEAR);
            if(year1 != year2) {
                int timeDistance = 0 ;
                for(int i = year1 ; i < year2 ; i ++) {
                    //闰年
                    if(i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                        timeDistance += 366;
                    }
                    else {
                        timeDistance += 365;
                    }
                }
                return timeDistance + (day2 - day1) ;
            }
            else {
                System.out.println("判断day2 - day1 : " + (day2-day1));
                return day2 - day1 + 1;
            }
        } catch (Exception ex) {
            log.error("日期转换出错！", ex);
            return 0;
        }
    }



    //计算从今天起,一年后的节假日的日期
    public static List<LocalDate> calHolidayForYear(List<Integer> integerList,LocalDate now){
        List<LocalDate> list =new ArrayList<>();
        Integer value = now.getDayOfWeek().getValue();
        if(integerList.contains(value)){
            list.add(now);
        }
        for (int i = 0; i < 365; i++) {
            now=now.plusDays(1);

            Integer weekDay = now.getDayOfWeek().getValue();
            if(integerList.contains(weekDay)){
                list.add(now);
            }

        }

        return list;
    }


}
