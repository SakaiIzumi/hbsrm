package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.mapper.FactoryVacationMapper;
import net.bncloud.delivery.mapper.FactoryWorkdayMapper;
import net.bncloud.delivery.param.BatchDeleteWorkDayParam;
import net.bncloud.delivery.param.FactoryWorkdayBatchParam;
import net.bncloud.delivery.param.FactoryWorkdayParam;
import net.bncloud.delivery.param.HolidayAndSubscribeConfigParam;
import net.bncloud.delivery.service.FactoryVacationService;
import net.bncloud.delivery.service.FactoryWorkdayService;
import net.bncloud.delivery.service.HolidayDateService;
import net.bncloud.delivery.service.HolidayService;
import net.bncloud.delivery.utils.BuildDayUtil;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import net.bncloud.delivery.wrapper.FactoryWorkdayWrapper;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.utils.AuthUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactoryWorkdayServiceImpl extends BaseServiceImpl<FactoryWorkdayMapper, FactoryWorkday> implements FactoryWorkdayService {
    @Resource
    private FactoryWorkdayMapper factoryWorkdayMapper;
    @Resource
    private FactoryVacationMapper factoryVacationMapper;
    @Resource
    @Lazy
    private FactoryVacationService factoryVacationService;
    /*@Resource
    private DeliveryNoteServiceVS deliveryNoteService;*/
    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;
    @Resource
    private HolidayService holidayService;
    @Resource
    private HolidayDateService holidayDateService;


    @Override
    public IPage<FactoryWorkdayVo> selectListPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam, String belongTypeCode) {
        //设置查询端
        queryParam.getParam().setBelongType(belongTypeCode);
        IPage<FactoryWorkday> workdayPage = factoryWorkdayMapper.selectListPage(page, queryParam);
        IPage<FactoryWorkdayVo> pageVo = FactoryWorkdayWrapper.build().pageVO(workdayPage);

        return pageVo;
    }

    @Override
    @Transactional
    public void batchSetWorkday(FactoryWorkdayBatchParam param, String type) {
        //采购方的编码不能为空  todo
        //工厂工作日批量保存更新
        List<FactoryWorkday> factoryWorkdayList=workDaySaveOrUpdate(param,type);

        List<FactoryVacation> factoryVacationListForSave = new ArrayList<>();

        //数据准备 配置数据
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(type);

        //数据准备 存放每轮变化的日期,方便复用
        Map<String,List<LocalDate>> dateForRun = new HashMap<>();

        List<FactoryVacation> factoryVacationList = holidayAdjustment(factoryWorkdayList, type,allHolidayAndSubscribeConfig,dateForRun);
        factoryVacationListForSave.addAll(factoryVacationList);

        //保存
        factoryVacationService.saveBatch(factoryVacationListForSave);
    }

    /**
     * 调整节日的方法
     * @param factoryWorkdayList 每一次循环的工作日list
     * @param belongType  采购方/供应方 类型
     * @param allHolidayAndSubscribeConfig mrp的默认工作日 自动订阅法定节假日的配置
     *
     * */
    private List<FactoryVacation> holidayAdjustment(List<FactoryWorkday> factoryWorkdayList,String belongType,HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig,Map<String,List<LocalDate>> dateForRun) {
        Map<Long ,Map<String, List<LocalDate>>> saveMap=new HashMap();

        //开始修改节假日--得到工厂的ids列表
        List<Long> factoryIdList = factoryWorkdayList.stream().map(FactoryWorkday::getFactoryId).collect(Collectors.toList());

        //批量删除这些工厂的所有的默认非工作节假日
        factoryVacationMapper.deleteInFactoryId(factoryIdList);

        //3--配置开启的订阅法定节假日
            //查询配置,是否开启法定节假日,开启,查询出所有的节假日
        List<LocalDate> localDateListForAuto=new ArrayList<>();
        List<LocalDate> localDate996ListForAuto=new ArrayList<>();

        //当前采购/供应商是否有订阅法定节假日
        if(allHolidayAndSubscribeConfig.getJsonObjectForAuto()){
            //是 返回法定节假日的日期
                //判断参数中是否有值,没有值,调用一次方法,后面就不需要再调用了
                if(CollectionUtil.isEmpty( allHolidayAndSubscribeConfig.getLocalDateListForAuto() )&& CollectionUtil.isEmpty(allHolidayAndSubscribeConfig.getLocalDate996ListForAuto())){
                    fillConfigHolidayList(allHolidayAndSubscribeConfig);
                }

            localDateListForAuto=allHolidayAndSubscribeConfig.getLocalDateListForAuto();
            localDate996ListForAuto=allHolidayAndSubscribeConfig.getLocalDate996ListForAuto();
        }


        //2--手动配置的节假日(注意为空判断)
        Map<Long, List<FactoryVacation>> allFactoryVacationMap=new HashMap<>();

        //先查询所有工厂的手动配置的节假日(注意为空判断)
        List<FactoryVacation> allFactoryVacationList = factoryVacationService.getBaseMapper().selectList(Wrappers
                .<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getSourceType,MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())
                .gt(FactoryVacation::getVacationDate,BuildDayUtil.day())//大于当前日期的
                .in(FactoryVacation::getFactoryId, factoryIdList));
              //.eq(FactoryVacation::getBelongCode,factoryWorkdayList.get(0).getBelongCode())//这个采购方/供应商所有的手动添加节假日

        //按工厂id分组 得到这些工厂的手动假期的分组的map 分组后的map如果获取里面的元素也要注意为空判断 下面已经做了
        if(CollectionUtil.isNotEmpty(allFactoryVacationList)){
            allFactoryVacationMap = allFactoryVacationList.stream().collect(Collectors.groupingBy(FactoryVacation::getFactoryId));

            //获取手动新增之后删除手动新增的 方便后续重新插入
            List<Long> addManuallyIds = allFactoryVacationList.stream().map(item -> item.getId()).collect(Collectors.toList());
            factoryVacationMapper.deleteAddManuallyInFactoryId(addManuallyIds);
        }

        //1--默认配置的节假日的日期 (下面的循环中获取)

        //这个map后面设置假日类型会有用
        Map<Long,Map<Date, FactoryVacation>> factoryVacationForAddMapParent=new HashMap<>();


        /**
         *
         * 重新构建此工厂的节假日 每次生成一年数据
         * 新的节假日有可能存在--更新 也有可能不存在--新增 也有可能调整后节假日不存在--删除
         * 补全一年数据
         *
         * */
        for (FactoryWorkday factoryWorkday : factoryWorkdayList) {

            //3--法定节假日数据准备(已获取)

            //2--手动配置的节假日准备(注意为空判断)
            List<LocalDate> localDateListForADD=new ArrayList<>();

            List<FactoryVacation> factoryVacationForAddList = allFactoryVacationMap.get(factoryWorkday.getFactoryId());

            //手动配置的日期list 列表数据可以不设置 因为有可能为空
            if(CollectionUtil.isNotEmpty(factoryVacationForAddList)){
                factoryVacationForAddList.forEach(item -> {
                    LocalDate localDate = BuildDayUtil.dateToLocalDate(item.getVacationDate());
                    localDateListForADD.add(localDate);
                });

                Map<Date, FactoryVacation> factoryVacationForAddMap = factoryVacationForAddList
                        .stream()
                        .collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));

                factoryVacationForAddMapParent.put(factoryWorkday.getFactoryId(),factoryVacationForAddMap);
            }


            //1--此工厂配置的默认节假日数据准备
            List<LocalDate> localDateListForDefault=getWorkDayVacation(factoryWorkday,dateForRun);

            //冲突处理
            Map<String, List<LocalDate>> factoryVacationMap = buildFactoryVacationMap(localDateListForDefault, localDateListForADD, localDateListForAuto,localDate996ListForAuto);

            saveMap.put(factoryWorkday.getId(),factoryVacationMap);

        }

        //保存这次循环的数据
        return saveHoliday(saveMap,factoryWorkdayList,factoryVacationForAddMapParent);
    }

    /**
     * 填充节假日list参数的方法
     * */
    private void fillConfigHolidayList(HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig) {
        Map<String, List<LocalDate>> holidayDate = holidayDateService.selectAllHoliday();
        allHolidayAndSubscribeConfig.setLocalDateListForAuto(holidayDate.get(HolidayDateEnum.HOLIDAY_DAY.getCode()));
        allHolidayAndSubscribeConfig.setLocalDate996ListForAuto(holidayDate.get(HolidayDateEnum.IS_996.getCode()));
    }


    /**
     * 获取FactoryWorkDay的默认
     * */
    private List<LocalDate> getWorkDayVacation(FactoryWorkday factoryWorkday,Map<String, List<LocalDate>> dateForRun) {
        //前置设置
        Integer weekStartNum = factoryWorkday.getWeekStartNum();
        Integer weekEndNum = factoryWorkday.getWeekEndNum();
        String key=weekStartNum+"-"+weekEndNum;

        //这个特定配置的工作日时间有没有存呢?
        List<LocalDate> localDates = dateForRun.get(key);

        //不为空 有的! 不用再调用循环创建了 直接赋值
        if(CollectionUtil.isNotEmpty(localDates)){
            return  localDates;
        }else{
            //没有那就乖乖调循环获取吧
            List<LocalDate> excessive = buildDefaultChangeVacation(factoryWorkday);

            //放进去方便下一次 如果有工厂也是这样配 那就不用循环直接获取了
            dateForRun.put(key,excessive);

            return excessive;
        }

    }

    /**
     * 判断本轮是否和默认配置的默认工作日一致
     * */
    private Boolean equalsDefaultVacationConfig(FactoryWorkday factoryWorkday, Map<String, Integer> defaultConfigWorkDayMap) {
        Integer weekStartNum = factoryWorkday.getWeekStartNum();
        Integer weekEndNum = factoryWorkday.getWeekEndNum();

        Integer start = defaultConfigWorkDayMap.get("start");
        Integer end = defaultConfigWorkDayMap.get("end");
        if(start.equals(weekStartNum)&&end.equals(weekEndNum)){
            return true;
        }
        return false;
    }

    /**
     *工厂工作日saveOrUpdate方法
     *
     * 现在保存的话  采购方编码存的是orgId
     **/
    private List<FactoryWorkday> workDaySaveOrUpdate(FactoryWorkdayBatchParam param, String type) {
        List<FactoryWorkdayParam> workdayList = param.getWorkdayList();
        List<FactoryWorkday> factoryWorkdayList = new ArrayList<>();
        workdayList.forEach(workday->{
            FactoryWorkday factoryWorkday = new FactoryWorkday();
            factoryWorkday.setBelongType(type);

            //如果是供应商,需要设置所属的供应商名字
            factoryWorkday.setBelongCode(workday.getBelongCode());
            factoryWorkday.setBelongName(workday.getBelongName());

            //星期的开始和结束天数设置
            Integer weekStartNum = workday.getWeekStartNum();
            Integer weekEndNum = workday.getWeekEndNum();
            if(weekStartNum>weekEndNum){
                throw new ApiException(500,workday.getFactoryName()+"的工作日开始时间不能大于结束时间");
            }
            factoryWorkday.setWeekStartNum(workday.getWeekStartNum());
            factoryWorkday.setWeekEndNum(workday.getWeekEndNum());

            //id不为空,更新
            if(!ObjectUtil.isEmpty(workday.getId())){
                factoryWorkday.setId(workday.getId());
            }
            factoryWorkday.setFactoryId(workday.getFactoryId());
            factoryWorkday.setFactoryName(workday.getFactoryName());
            factoryWorkday.setIsDeleted(0);
            factoryWorkday.setCreatedBy(AuthUtil.getUser().getUserId());
            factoryWorkday.setLastModifiedBy(AuthUtil.getUser().getUserId());
            factoryWorkday.setCreatedDate(new Date());
            factoryWorkday.setLastModifiedDate(new Date());

            factoryWorkdayList.add(factoryWorkday);
        });

        this.saveOrUpdateBatch(factoryWorkdayList);

        return factoryWorkdayList;

    }

    /**
     *默认配置的节假日方法
     **/
    private List<LocalDate> buildDefaultChangeVacation(FactoryWorkday factoryWorkday ) {

        //根据工作日时间获取默认放假时间
        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(factoryWorkday.getWeekStartNum(), factoryWorkday.getWeekEndNum());
        //获取当前日期
        LocalDate day = BuildDayUtil.day();

        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(day);
        //默认构建365天默认假期数据
        for (int i = 1; i <365 ; i++) {
            LocalDate localDate = day.plusDays(i);

            //获取当前日期是星期几
            int weekOfDay = BuildDayUtil.weekOfDay(localDate);

            //如果星期几在默认假期列表中,就是默认假期
            if(integerList.contains(weekOfDay)){
                localDateList.add(localDate);
            }
        }

        return localDateList;

    }

    /**
     *构建需要保存的节假日的方法
     **/
    private List<FactoryVacation> saveHoliday(Map<Long ,Map<String, List<LocalDate>>> saveMap,List<FactoryWorkday> factoryWorkdayList,Map<Long,Map<Date, FactoryVacation>> factoryVacationForAddMapParent) {

        List<FactoryVacation> factoryVacationList = new ArrayList<>();

        for (FactoryWorkday factoryWorkday : factoryWorkdayList) {

            Map<String, List<LocalDate>> factoryVacationMap = saveMap.get(factoryWorkday.getId());

            Set<Map.Entry<String, List<LocalDate>>> entries = factoryVacationMap.entrySet();
            for (Map.Entry<String, List<LocalDate>> entry : entries) {
                List<LocalDate> days = entry.getValue();
                String key = entry.getKey();

                Integer statusCode = MrpVacationStatusEnum.ENABLE.getCode();

                Map<Date, FactoryVacation> dateFactoryVacationMap=new HashMap<>();
                boolean flag=false;
                /*if(StrUtil.equals(key,MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY_996.getCode())){
                    statusCode=MrpVacationStatusEnum.IS_996.getCode();
                }else */
                if(StrUtil.equals(key,MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())){
                     dateFactoryVacationMap = factoryVacationForAddMapParent.get(factoryWorkday.getId());
                    flag=CollectionUtil.isNotEmpty(dateFactoryVacationMap);
                    //flag=!dateFactoryVacationMap.isEmpty();
                }
                for (LocalDate day : days) {
                    FactoryVacation factoryVacation = new FactoryVacation();
                    factoryVacation.setBelongType(factoryWorkday.getBelongType());
                    factoryVacation.setBelongCode(factoryWorkday.getBelongCode());
                    factoryVacation.setFactoryNumber(factoryWorkday.getFactoryNumber());
                    factoryVacation.setFactoryName(factoryWorkday.getFactoryName());

                    factoryVacation.setSourceType(entry.getKey());
                    factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(day));
                    factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(day));
                    factoryVacation.setStatus(statusCode);
                    factoryVacation.setFactoryId(factoryWorkday.getFactoryId());
                    factoryVacation.setVacationType(MrpVacationTypeEnum.DEFAULT_HOLIDAY.getCode());


                    if(flag){
                        Date date = BuildDayUtil.localdateToDate(day);
                        FactoryVacation factoryVacationForSet = dateFactoryVacationMap.get(date);
                        if(ObjectUtil.isNotEmpty(factoryVacationForSet)){
                            factoryVacation.setVacationType(factoryVacationForSet.getVacationType());
                            factoryVacation.setStatus(factoryVacationForSet.getStatus());
                        }

                    }
                    factoryVacationList.add(factoryVacation);
                }
            }

        }
        return factoryVacationList;
    }

    /**
     *冲突处理方法
     **/
    private Map<String, List<LocalDate>> buildFactoryVacationMap(List<LocalDate> localDateListForDefault,List<LocalDate> localDateListForAdd, List<LocalDate> localDateListForAuto,List<LocalDate> localDate996ListForAuto) {
        Map<String, List<LocalDate>> factoryVacationMap = new HashMap();

        //先用996的list去掉可以放假的天数
        localDateListForDefault=holidayService.cover(localDate996ListForAuto,localDateListForDefault);
        localDateListForAdd=holidayService.cover(localDate996ListForAuto,localDateListForAdd);

        //自动订阅覆盖默认
        localDateListForDefault= holidayService.cover(localDateListForAuto,localDateListForDefault);
        //自动订阅覆盖手动新增
        localDateListForAdd=holidayService.cover(localDateListForAuto,localDateListForAdd);
        //默认覆盖手动
        localDateListForAdd=holidayService.cover(localDateListForDefault,localDateListForAdd);

        factoryVacationMap.put(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode(),localDateListForAdd);
        factoryVacationMap.put(MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode(),localDateListForDefault);

        return factoryVacationMap;

    }

    /**
     *删除工厂方法
     **/
    @Override
    @Transactional
    public void deleteBatchWorkday(BatchDeleteWorkDayParam param) {

        List<FactoryVacation> factoryVacationArrayList = new ArrayList<>();

        List<FactoryWorkdayParam> factoryWorkdayParamList = param.getFactoryWorkdayParamList();
        Map<String, List<FactoryWorkdayParam>> map = factoryWorkdayParamList.stream().collect(Collectors.groupingBy(FactoryWorkdayParam::getBelongCode));
        List<Long> ids = factoryWorkdayParamList.stream().map(FactoryWorkdayParam::getId).collect(Collectors.toList());
        List<Long> factoryIds = factoryWorkdayParamList.stream().map(FactoryWorkdayParam::getFactoryId).collect(Collectors.toList());

        //批量删除workday
        this.deleteAllByWorkdayIds(ids);
        //this.getBaseMapper().deleteBatchIds(ids);
        //批量删除工厂的假期 重新构建
        factoryVacationService.deleteAllVacationFromFactoryId(factoryIds);
        //factoryVacationService.getBaseMapper().deleteBatchIds(factoryIds);

        //联动修改工厂的默认工作日和假期的信息  数据准备
        List<LocalDate> localDateForDefault = new ArrayList<>();
        String belongType = param.getBelongType();
        Integer startNum=null;
        Integer endNum=null;

        //获取远程的默认工作日配置 订阅法定节假日开关
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());

        Map<String, List<LocalDate>> stringListMap = holidayDateService.selectAllHoliday();
        List<LocalDate> localDates = stringListMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
        List<LocalDate> localDatesFor996 = stringListMap.get(HolidayDateEnum.IS_996.getCode());
        Boolean jsonObjectForAuto = allHolidayAndSubscribeConfig.getJsonObjectForAuto();

        //获取全局默认工作日
        Map<String, Integer> defaultWorkdayConfigMap = allHolidayAndSubscribeConfig.getDefaultWorkdayConfigMap();
        startNum = defaultWorkdayConfigMap.get("start");
        endNum = defaultWorkdayConfigMap.get("end");
        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(startNum, endNum);

        if(CollectionUtil.isNotEmpty(integerList)){
            //有休息日 创建默认休息日
            localDateForDefault = BuildDayUtil.calHolidayForYear(integerList, BuildDayUtil.day());
        }


        if(StrUtil.equals(WorkBench.PURCHASE.getCode(),belongType)){

            for (FactoryWorkdayParam factoryWorkdayParam : factoryWorkdayParamList) {
                //如果开启了订阅
                if(jsonObjectForAuto){
                    factoryVacationArrayList.addAll(buildEntity(localDatesFor996,MrpVacationStatusEnum.IS_996.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),belongType,factoryWorkdayParam));
                    factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),belongType,factoryWorkdayParam));

                    localDateForDefault = holidayService.cover(localDates, localDateForDefault);
                    localDateForDefault = holidayService.cover(localDates, localDateForDefault);

                }
                if(CollectionUtil.isNotEmpty(localDateForDefault)){
                    factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode(),belongType,factoryWorkdayParam));
                }
            }


        }else{

            for (FactoryWorkdayParam factoryWorkdayParam : factoryWorkdayParamList) {
                //如果开启了订阅
                if(jsonObjectForAuto){
                    factoryVacationArrayList.addAll(buildEntity(localDatesFor996,MrpVacationStatusEnum.IS_996.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),belongType,factoryWorkdayParam));
                    factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),belongType,factoryWorkdayParam));

                    localDateForDefault = holidayService.cover(localDates, localDateForDefault);
                    localDateForDefault = holidayService.cover(localDates, localDateForDefault);

                }
                if(!localDateForDefault.isEmpty()){
                    factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode(),belongType,factoryWorkdayParam));
                }
            }
        }

        if(CollectionUtil.isNotEmpty(factoryVacationArrayList)){
            factoryVacationService.saveBatch(factoryVacationArrayList);
        }
        /*Map<String, Integer> defaultWorkDayConfig = holidayService.getDefaultWorkDayConfig(DeliveryCfgParam.MRP_DEFAULT_WORKDAY.getCode());
        List<FactoryWorkday> collect = ids.stream().map(item -> {
            FactoryWorkday factoryWorkday = new FactoryWorkday();
            factoryWorkday.setWeekStartNum(defaultWorkDayConfig.get("start"));
            factoryWorkday.setWeekEndNum(defaultWorkDayConfig.get("end"));
            factoryWorkday.setId(item);
            return factoryWorkday;
        }).collect(Collectors.toList());

        holidayAdjustment(collect,WorkBench.PURCHASE.getCode());*/

    }

    private void deleteAllByWorkdayIds(List<Long> ids) {
        factoryWorkdayMapper.deleteAllByWorkdayIds(ids);
    }

    @Override
    public void deleteAllWorkDayFromFactoryId(List<Long> ids) {
        factoryWorkdayMapper.deleteAllWorkDayFromFactoryId(ids);
    }

    private List<FactoryVacation> buildEntity(List<LocalDate> localDates, Integer status, String sourceType, String belongType,FactoryWorkdayParam factoryWorkdayParam) {
        List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
        for (LocalDate localDate : localDates) {

            FactoryVacation factoryVacation = new FactoryVacation();
            factoryVacation.setStatus(status);
            factoryVacation.setSourceType(sourceType);
            factoryVacation.setBelongType(belongType);
            factoryVacation.setFactoryId(factoryWorkdayParam.getFactoryId());
            factoryVacation.setFactoryName(factoryWorkdayParam.getFactoryName());
            factoryVacation.setBelongCode(factoryWorkdayParam.getBelongCode());
            factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(localDate));
            factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(localDate));

            factoryVacationArrayList.add(factoryVacation);
        }
        return factoryVacationArrayList;
    }




}
