package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.repeatrequest.RepeatRequestOperation;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.mapper.FactoryVacationMapper;
import net.bncloud.delivery.mapper.FactoryWorkdayMapper;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.service.FactoryVacationService;
import net.bncloud.delivery.service.HolidayDateService;
import net.bncloud.delivery.service.HolidayService;
import net.bncloud.delivery.service.NewFactoryWorkdayService;
import net.bncloud.delivery.utils.BuildDayUtil;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import net.bncloud.delivery.wrapper.FactoryWorkdayWrapper;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.utils.AuthUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewFactoryWorkdayServiceImpl extends BaseServiceImpl<FactoryWorkdayMapper, FactoryWorkday> implements NewFactoryWorkdayService {
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
    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;


    @Override
    public IPage<FactoryWorkdayVo> selectListPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam, String belongTypeCode) {
        //设置查询端
        queryParam.getParam().setBelongType(belongTypeCode);
        IPage<FactoryWorkday> workdayPage = factoryWorkdayMapper.selectListPage(page, queryParam);
        IPage<FactoryWorkdayVo> pageVo = FactoryWorkdayWrapper.build().pageVO(workdayPage);

        //如果查询段是供应的 还需要根据配置对按钮进行控制(现在不用 前端获取配置 然后根据配置来进行按钮的启用和禁用)
        /*if(belongTypeCode.equals(WorkBench.SUPPLIER.getCode())){
            R<CfgParamDTO> paramResult = cfgParamResourceFeignClient.getParamByCode(CfgParamKeyEnum.MRP_SUPPLIER_DEFAULT_WORKDAY.getCode());
            Asserts.isTrue(paramResult.isSuccess()&&ObjectUtil.isNotEmpty(paramResult.getData()),"远程获取是否设置供应方默认工作日配置异常"+paramResult.getMsg());
            Boolean changer = Boolean.valueOf(paramResult.getData().getValue());
            if(changer){

            }
        }*/

        return pageVo;
    }

    @RepeatRequestOperation
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSetWorkday(FactoryWorkdayBatchParam param, String type) {
        //采购方/供应方的编码不能为空
        //工厂工作日批量保存更新
        List<FactoryWorkday> factoryWorkdayList=workDaySaveOrUpdate(param,type);

        //构建节假日
        holidayAdjustment(factoryWorkdayList);

    }

    /**
     * 调整节日的方法
     * @param factoryWorkdayList 每一次循环的工作日list
     * */
    @Override
    public void holidayAdjustment(List<FactoryWorkday> factoryWorkdayList) {
        //数据准备 存放每轮变化的日期,方便复用
        Map<String,List<LocalDate>> dateForRun = new HashMap<>();
        //总的saveMap,批量操作的时候所有工厂所有的需要修改的日期放到这个map中
        Map<Long ,Map<String, List<LocalDate>>> saveMap = new HashMap<>();

        //开始修改节假日--得到工厂的ids列表
        List<Long> factoryIdList = factoryWorkdayList.stream().map(FactoryWorkday::getFactoryId).collect(Collectors.toList());

        //批量删除这些工厂的所有的默认非工作节假日
        factoryVacationMapper.deleteInFactoryId(factoryIdList);

        //查询法定节假日
        List<FactoryVacation> factoryVacationAutoSubscribeList = factoryVacationService.getBaseMapper().selectList(Wrappers
                .<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getSourceType, MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())
                .gt(FactoryVacation::getVacationDate, BuildDayUtil.day().toString() )
                .in(FactoryVacation::getFactoryId, factoryIdList));
        //按工厂id划分
        Map<Long, List<FactoryVacation>> factoryVacationAutoSubscribeListGroupByFactoryId = factoryVacationAutoSubscribeList.stream().collect(Collectors.groupingBy(FactoryVacation::getFactoryId));


        //查询手动添加的节假日
        List<FactoryVacation> factoryVacationAddManuallyList = factoryVacationService.getBaseMapper().selectList(Wrappers
                .<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getSourceType, MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())
                .gt(FactoryVacation::getVacationDate, BuildDayUtil.day().toString())
                .in(FactoryVacation::getFactoryId, factoryIdList));
        //按工厂id划分
        Map<Long, List<FactoryVacation>> factoryVacationAddManuallyListGroupByFactoryId = factoryVacationAddManuallyList.stream().collect(Collectors.groupingBy(FactoryVacation::getFactoryId));


        //Map<String,List<Integer>> dateForRun2 = new HashMap<>();
        //1--默认配置的节假日的日期  所有工厂配置的默认节假日数据准备
        //Map<Integer, List<LocalDate>> dayMap = getWorkDayVacationV2(factoryWorkdayList,dateForRun2);

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
            List<FactoryVacation> factoryVacationAutoSubscribeListForThisFactory = factoryVacationAutoSubscribeListGroupByFactoryId.get(factoryWorkday.getFactoryId());
            List<FactoryVacation> factoryVacationAddManuallyListForThisFactory = factoryVacationAddManuallyListGroupByFactoryId.get(factoryWorkday.getFactoryId());

            //获取此工厂的开启和关闭的法定节假日的两个list
            List < Date > factoryVacationAutoSubscribeEnableVacationList=new ArrayList<>();
            List < Date > factoryVacationAutoSubscribeDisableVacationList=new ArrayList<>();
            if(CollectionUtil.isNotEmpty(factoryVacationAutoSubscribeListForThisFactory)){
                factoryVacationAutoSubscribeEnableVacationList = factoryVacationAutoSubscribeListForThisFactory.stream().filter(item -> item.getStatus().equals(MrpVacationStatusEnum.ENABLE.getCode())).map(item -> item.getVacationDate()).collect(Collectors.toList());
                factoryVacationAutoSubscribeDisableVacationList = factoryVacationAutoSubscribeListForThisFactory.stream().filter(item-> item.getStatus().equals(MrpVacationStatusEnum.DISABLE.getCode()) ).map(item -> item.getVacationDate()).collect(Collectors.toList());
            }

            //法定上班不管的话,因为不会记录,所以这里调整工作日,刚好这一天是法定上班,那就会变成休息,按照产品 这里暂时不作处理
            //其实会有一个问题,如果用户点击关闭了法定节假日,重新开启了,法定上班刚好这一年,又会重新覆盖的

            //获取此工厂的开启和关闭的手动添加节假日的一个list  不需要分开两个 后面处理冲突可以一起处理
            List < Date > factoryVacationAddManuallyVacationList=new ArrayList<>();
            if(CollectionUtil.isNotEmpty(factoryVacationAddManuallyListForThisFactory)){
                factoryVacationAddManuallyVacationList = factoryVacationAddManuallyListForThisFactory.stream().map(FactoryVacation::getVacationDate).collect(Collectors.toList());
            }


            //1--此工厂配置的默认节假日数据准备
            List<LocalDate> localDateListForDefault=getWorkDayVacation(factoryWorkday,dateForRun);

            List<LocalDate> factoryVacationAutoSubscribeEnableVacationLocalDateList = BuildDayUtil.dateListToLocalDateList(factoryVacationAutoSubscribeEnableVacationList);
            List<LocalDate> factoryVacationAutoSubscribeDisableVacationLocalDateList = BuildDayUtil.dateListToLocalDateList(factoryVacationAutoSubscribeDisableVacationList);
            List<LocalDate> factoryVacationAddManuallyVacationLocalDateList = BuildDayUtil.dateListToLocalDateList(factoryVacationAddManuallyVacationList);


            //冲突处理
            Map<String, List<LocalDate>> factoryVacationMap = buildFactoryVacationMapForSave(factoryWorkday.getFactoryId(),localDateListForDefault   ,   factoryVacationAutoSubscribeEnableVacationLocalDateList , factoryVacationAutoSubscribeDisableVacationLocalDateList  , factoryVacationAddManuallyVacationLocalDateList ,factoryVacationAutoSubscribeList,factoryVacationAddManuallyList,factoryVacationForAddMapParent);

            saveMap.put(factoryWorkday.getId(),factoryVacationMap);

        }

        //保存这次循环的数据
        saveHoliday(saveMap,factoryWorkdayList,factoryVacationForAddMapParent);
    }

    /**
     * 构造返回需要保存和更新的日期map
     * */
    private Map<String, List<LocalDate>> buildFactoryVacationMapForSave(Long factoryId,List<LocalDate> localDateListForDefault, List<LocalDate> factoryVacationAutoSubscribeEnableVacationLocalDateList, List<LocalDate> factoryVacationAutoSubscribeDisableVacationLocalDateList, List<LocalDate> factoryVacationAddManuallyVacationLocalDateList,List<FactoryVacation> factoryVacationAutoSubscribeList ,List<FactoryVacation> factoryVacationAddManuallyList,Map<Long,Map<Date, FactoryVacation>> factoryVacationForAddMapParent) {
        Map<String, List<LocalDate>> factoryVacationMap = new HashMap<>();

        List<LocalDate> localDateForUpdate = new ArrayList<>();
        List<LocalDate> localDateForSave= new ArrayList<>();
        //法定节假日开启的 有重合日期 不能覆盖 那就跳过 调用方法跳过这些日期  剩下可以保存的
        localDateListForDefault = new ArrayList<>(holidayService.cover(factoryVacationAutoSubscribeEnableVacationLocalDateList, localDateListForDefault));

        //1-处理法定的
        //法定节假日关闭的 有重合的 可以覆盖 调用方法获取重合
        List<LocalDate> conflictForAuto996 = holidayService.getConflict(factoryVacationAutoSubscribeDisableVacationLocalDateList, localDateListForDefault);
        //然后去掉这些重合的(法定上班不需要保存)
        localDateListForDefault=holidayService.cover(conflictForAuto996, localDateListForDefault);
        //localDateForUpdate.addAll(holidayService.getConflict(factoryVacationAutoSubscribeDisableVacationLocalDateList,localDateListForDefault));

        //2-处理手动的
        //手动添加假日关闭和开启的 有重合的 可以覆盖 调用方法获取重合  获取到的就是需要更新的
        List<LocalDate> conflictForAdd = holidayService.getConflict(factoryVacationAddManuallyVacationLocalDateList, localDateListForDefault);
        localDateForUpdate.addAll(conflictForAdd);
        //去掉默认中需要更新的假期
        localDateListForDefault=holidayService.cover(conflictForAdd, localDateListForDefault);

        localDateForSave.addAll(localDateListForDefault);

        factoryVacationMap.put(MrpVacationSourceTypeEnum.SAVE_LIST.getCode(),localDateForSave);
        factoryVacationMap.put(MrpVacationSourceTypeEnum.UPDATE_LIST.getCode(),localDateForUpdate);
        
        //构建更新需要用到的vacation 的map
        List<FactoryVacation> fitList = new ArrayList<>();
        List<Date> dateListForUpdate = BuildDayUtil.localdateListToDateList(localDateForUpdate);
        fitList.addAll(factoryVacationAutoSubscribeList);
        fitList.addAll(factoryVacationAddManuallyList);
        //方便后续更新的时候保存该工厂该天的假期的启动和关闭状态
        Map<Date, FactoryVacation> collect = fitList.stream().filter(item ->item.getFactoryId().equals(factoryId)&& dateListForUpdate.contains(item.getVacationDate())).collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));
        factoryVacationForAddMapParent.put(factoryId,collect);

        return factoryVacationMap;
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

        //检查缓存中是否有
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
     * 获取FactoryWorkDay的默认休息日 v2版本
     * */
    private Map<Integer, List<LocalDate>> getWorkDayVacationV2(List<FactoryWorkday> factoryWorkdayList,Map<String,List<Integer>> dateForRun) {
        //首先需要知道所有的workday对应的休息日
        Set<Integer> defaultholidaySet = new HashSet<Integer>();

        factoryWorkdayList.forEach(item->{
            //保存这个item是星期几休息的(注意空的处理)todo
            List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(item.getWeekStartNum(), item.getWeekEndNum());
            defaultholidaySet.addAll(integerList);
            dateForRun.put(item.getWeekStartNum()+"-"+item.getWeekEndNum(),integerList);
        });

        //通过休息日获取一年的默认非工作日的休息日
        Map<Integer, List<LocalDate>> dayMap = buildDefaultChangeVacationV2(defaultholidaySet);
        return dayMap;
    }

    /**
     * 一周有多少天是休息,那就生成多少个list,放在map中
     * */
    private Map<Integer, List<LocalDate>> nListInMapByHoliday(Set<Integer> defaultholidaySet) {
        Map<Integer, List<LocalDate>> dayMap = new HashMap<>();
        for (Integer integer : defaultholidaySet) {
            List<LocalDate> list = new ArrayList();
            dayMap.put(integer,list);
        }
        return dayMap;
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

        Asserts.isTrue( CollectionUtil.isNotEmpty( workdayList ),"更新参数集合为空！" );
        //校验新增工厂中是否有重复,有重复那就返回提示哪个工厂重复
        //workConflict(param);

        // 待保存或更新的集合
        List<FactoryWorkday> factoryWorkdayList = new ArrayList<>();

        workdayList.forEach( workday->{

            // 校验一个采购方同时存在多条工作日数据，一个采购方只能存在一条工作日数据 or 一个供应商的一个工厂只能有一个工作日配置
            FactoryWorkday oldFactoryWorkday = this.getOne(Wrappers.<FactoryWorkday>lambdaQuery().eq(
                    FactoryWorkday::getBelongCode, workday.getBelongCode())
                    .eq(FactoryWorkday::getBelongType, type)
                    .eq(FactoryWorkday::getFactoryNumber, workday.getFactoryNumber())
                    .ne(workday.getId() != null, FactoryWorkday::getId, workday.getId())
            );

            String showMsg = WorkBench.PURCHASE.getCode().equals( type ) ? workday.getBelongName() + "的工作日已存在！" : workday.getBelongName() +"的"+ workday.getFactoryName() + "的工作日已存在！";
            Asserts.isNull( oldFactoryWorkday, showMsg);

            FactoryWorkday factoryWorkday = new FactoryWorkday();
            factoryWorkday.setBelongType(type);

            //设置所属的名字
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
            factoryWorkday.setFactoryNumber(workday.getFactoryNumber());
            factoryWorkday.setIsDeleted(0);
            factoryWorkday.setCreatedBy(AuthUtil.getUser().getUserId());
            factoryWorkday.setLastModifiedBy(AuthUtil.getUser().getUserId());
            factoryWorkday.setCreatedDate(new Date());
            factoryWorkday.setLastModifiedDate(new Date());

            this.saveOrUpdate( factoryWorkday );

            factoryWorkdayList.add(factoryWorkday);
        });

        //this.saveOrUpdateBatch(factoryWorkdayList);

        return factoryWorkdayList;

    }

    /**
     * 校验新增工厂中是否有重复,有重复那就返回提示哪个工厂重复 (因为接口是新增和编辑一起的,所以还要校验到底是新增还是编辑)
     * */
    private void workConflict(FactoryWorkdayBatchParam param) {
        String opera = param.getSaveOrUpdate();
        //如果是新增 需要校验
        //if(StrUtil.equals(opera,WordDayOperationEnums.save.name())){
        List<FactoryWorkdayParam> workdayList = param.getWorkdayList();
        //id为空 是新增
        if(workdayList.get(0).getId()==null){
            List<String> workdayListBelongCodes = workdayList.stream().map(item -> item.getBelongCode()).collect(Collectors.toList());
            List<FactoryWorkday> factoryWorkdaylist = this.list(Wrappers.<FactoryWorkday>lambdaQuery().in(FactoryWorkday::getBelongCode, workdayListBelongCodes));
            if(CollectionUtil.isNotEmpty(factoryWorkdaylist)){
                StringBuilder stringBuilder=new StringBuilder();
                List<String> selectBelongCodeList = factoryWorkdaylist.stream().map(item -> item.getBelongCode()).collect(Collectors.toList());
                for (FactoryWorkdayParam factoryWorkdayParam : workdayList) {
                    if(selectBelongCodeList.contains(factoryWorkdayParam.getBelongCode())){
                        stringBuilder.append(factoryWorkdayParam.getBelongName()).append("已经存在工作日;");
                    }
                }
                throw new ApiException(500,stringBuilder.toString());
            }
        }

        //}
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
        int todayWeekOfDay = BuildDayUtil.weekOfDay(day);
        if(integerList.contains(todayWeekOfDay)){
            localDateList.add(day);
        }

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
     *默认配置的节假日方法V2
     **/
    private Map<Integer, List<LocalDate>> buildDefaultChangeVacationV2( Set<Integer> defaultholidaySet) {
        //一周有多少天是休息,那就生成多少个list,放在map中
        Map<Integer, List<LocalDate>> dayMap = nListInMapByHoliday(defaultholidaySet);

        //获取当前日期
        LocalDate day = BuildDayUtil.day();
        //获取当前日期是星期几
        int weekOfDay = BuildDayUtil.weekOfDay(day);

        //计算出一年的非默认工作日
        //List<LocalDate> localDateList = new ArrayList<>();
        for (int i = 1; i <=365 ; i++) {
            if(defaultholidaySet.contains(weekOfDay)){
                //localDateList.add(day);
                dayMap.get(weekOfDay).add(day);
            }
            //然后加一天
            day = day.plusDays(1);
            //如果是星期日变成星期一  否则加一天
            weekOfDay = weekOfDay==7?1:weekOfDay+1;

        }
        return dayMap;
    }

    /**
     *构建需要保存的节假日的方法
     **/
    private void saveHoliday(Map<Long ,Map<String, List<LocalDate>>> saveMap,List<FactoryWorkday> factoryWorkdayList,Map<Long,Map<Date, FactoryVacation>> factoryVacationForAddMapParent) {

//        Map<String, List<FactoryVacation>> returnMap = new HashMap<>();

        List<FactoryVacation> factoryVacationSaveList = new ArrayList<>();
        List<FactoryVacation> factoryVacationUpdateList = new ArrayList<>();

        for (FactoryWorkday factoryWorkday : factoryWorkdayList) {

            Map<String, List<LocalDate>> factoryVacationMap = saveMap.get(factoryWorkday.getId());

            List<LocalDate> localDatesForSave = factoryVacationMap.get(MrpVacationSourceTypeEnum.SAVE_LIST.getCode());
            List<LocalDate> localDatesForUpdate = factoryVacationMap.get(MrpVacationSourceTypeEnum.UPDATE_LIST.getCode());

            //保存的vacation
            for (LocalDate localDate : localDatesForSave) {
                FactoryVacation factoryVacation = new FactoryVacation();
                factoryVacation.setBelongType(factoryWorkday.getBelongType());
                factoryVacation.setBelongCode(factoryWorkday.getBelongCode());
                factoryVacation.setBelongName(factoryWorkday.getBelongName());
                factoryVacation.setFactoryName(factoryWorkday.getFactoryName());

                factoryVacation.setVacationType(MrpVacationTypeEnum.DEFAULT_HOLIDAY.getCode());
                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode());
                factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(localDate));
                factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(localDate));
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                factoryVacation.setFactoryId(factoryWorkday.getFactoryId());
                factoryVacation.setFactoryNumber( factoryWorkday.getFactoryNumber() );
                factoryVacation.setIsDeleted( 0 );
                factoryVacationSaveList.add(factoryVacation);

            }

            //更新的vacation
            Map<Date, FactoryVacation> dateFactoryVacationMap = factoryVacationForAddMapParent.get(factoryWorkday.getFactoryId());
            for (LocalDate localDate : localDatesForUpdate) {

                Date date = BuildDayUtil.localdateToDate(localDate);
                FactoryVacation factoryVacation = dateFactoryVacationMap.get(date);

                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode());
                factoryVacation.setVacationType(MrpVacationTypeEnum.DEFAULT_HOLIDAY.getCode());
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                factoryVacationUpdateList.add(factoryVacation);

            }


        }
//        returnMap.put(MrpVacationSourceTypeEnum.SAVE_LIST.getCode(),factoryVacationSaveList);
//        returnMap.put(MrpVacationSourceTypeEnum.UPDATE_LIST.getCode(),factoryVacationUpdateList);

        //保存
        List<FactoryVacation> saveOrUpdateBatchList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(factoryVacationSaveList)){
            saveOrUpdateBatchList.addAll(factoryVacationSaveList);
            //factoryVacationService.saveBatch(factoryVacationSaveList);
        }
        if(CollectionUtil.isNotEmpty(factoryVacationUpdateList)){
            saveOrUpdateBatchList.addAll(factoryVacationUpdateList);
            //factoryVacationService.updateBatchById(factoryVacationUpdateList);
        }
        if(CollectionUtil.isNotEmpty(saveOrUpdateBatchList)){
            factoryVacationService.saveOrUpdateBatch(saveOrUpdateBatchList);
        }
        //return returnMap;
    }



    /**
     *删除工厂方法
     **/
    @Override
    @Transactional
    public void deleteBatchWorkday(BatchDeleteWorkDayParam param) {

        List<FactoryWorkdayParam> factoryWorkdayParamList = param.getFactoryWorkdayParamList();

        //重新构建工厂的默认节假日
        //获取远程的默认工作日配置 订阅法定节假日开关
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());

        //获取全局默认工作日
        Map<String, Integer> defaultWorkdayConfigMap = allHolidayAndSubscribeConfig.getDefaultWorkdayConfigMap();
        Integer startNum = defaultWorkdayConfigMap.get("start");
        Integer endNum = defaultWorkdayConfigMap.get("end");

        //如果删除的工厂的手动调整的工作日和配置的工作日一样  那就不需要修改了 相当于已经覆盖  但是要把所有的默认非工作日的状态变成启用
        // 这一步不做了,一起交给下面的调整日期方法调整 因为实际上这样做的情况比较少


        List<FactoryWorkday> collect=factoryWorkdayParamList.stream().map(factoryWorkday->{
            factoryWorkday.setWeekStartNum(startNum);
            factoryWorkday.setWeekEndNum(endNum);
            return factoryWorkday;
        }).collect(Collectors.toList());

        //调整假期
        holidayAdjustment(collect);

        //删除工厂workday
        List<Long> ids = factoryWorkdayParamList.stream().map(FactoryWorkdayParam::getId).collect(Collectors.toList());
        this.deleteAllByWorkdayIds(ids);
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


    @Override
    @Transactional
    public void deleteBatchWorkdayForSupplier(BatchDeleteWorkDayParam param) {
        List<FactoryWorkdayParam> factoryWorkdayParamList = param.getFactoryWorkdayParamList();

        //不需要重新构建销售工厂的默认节假日
        //因为供应方没有全局默认配置  按照产品说法 没有配 就是一到七都是工作日 那就删除这些工厂的所有默认非工作日就行了

        //删除工厂
        List<Long> ids = factoryWorkdayParamList.stream().map(FactoryWorkdayParam::getId).collect(Collectors.toList());
        this.deleteAllByWorkdayIds(ids);

        //调整假期
        factoryVacationMapper.deleteInFactoryId(ids);

    }

    @Override
    public IPage<FactoryWorkdayVo> supplierPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam) {
        String supplierCode = AuthUtil.getUser().getCurrentSupplier().getSupplierCode();
        queryParam.getParam().setBelongCode(supplierCode);
        return factoryWorkdayMapper.supplierPage(page,queryParam);
    }

    



}
