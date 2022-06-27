package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import net.bncloud.base.BaseEntity;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactorySubscribe;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.mapper.FactoryInfoMapper;
import net.bncloud.delivery.mapper.FactoryVacationMapper;
import net.bncloud.delivery.mapper.FactoryWorkdayMapper;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.service.*;
import net.bncloud.delivery.service.listener.ImportFactoryVacationListener;
import net.bncloud.delivery.service.listener.ImportSupplierFactoryVacationListener;
import net.bncloud.delivery.utils.BuildDayUtil;
import net.bncloud.delivery.vo.FactoryVacationImportVo;
import net.bncloud.delivery.vo.FactoryVacationVo;
import net.bncloud.delivery.vo.ImportFactoryVacationVo;
import net.bncloud.delivery.vo.ImportSupplierFactoryVacationVo;
import net.bncloud.delivery.wrapper.FactoryVacationWrapper;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactoryVacationServiceImpl extends BaseServiceImpl<FactoryVacationMapper, FactoryVacation> implements FactoryVacationService {
    @Resource
    private FactoryVacationMapper factoryVacationMapper;
    @Resource
    private FactoryWorkdayMapper factoryWorkdayMapper;
    @Resource
    private FactoryInfoMapper factoryInfoMapper;
    @Resource
    private HolidayService holidayService;
    @Resource
    private HolidayDateService holidayDateService;
    @Resource
    private FactorySubscribeService factorySubscribeService;
    @Resource
    private SupplierFeignClient supplierFeignClient;
    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    @Lazy
    private FactoryInfoService factoryInfoService;


    @Override
    public IPage<FactoryVacationVo> selectListPage(IPage<FactoryVacation> page, QueryParam<FactoryVacationParam> queryParam) {
        boolean button=false;
        String port = queryParam.getParam().getPort();
        String tab = queryParam.getParam().getTab();
        if(port.equals("0")&&tab.equals("0")){
            //采购端查看采购页面  查自己的全部
            queryParam.getParam().setBelongType(WorkBench.PURCHASE.getCode());
            //加按钮
            button=true;

        }else if(port.equals("0")&&tab.equals("1")){
            //采购端查看销售页面  不加按钮  查所有供应
            queryParam.getParam().setBelongType(WorkBench.SUPPLIER.getCode());

        }else if(port.equals("1")&&tab.equals("0")){
            //供应方查询采购的tab  不加按钮  查采购的所有
            queryParam.getParam().setBelongType(WorkBench.PURCHASE.getCode());
            //queryParam.getParam().setBelongCode();  belong code由前端传

        }else if(port.equals("1")&&tab.equals("1")){
            //供应方查自己   加按钮   //只能查自己
            queryParam.getParam().setBelongType(WorkBench.SUPPLIER.getCode());
            queryParam.getParam().setBelongCode(AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
            button=true;

        }

        IPage<FactoryVacation> factoryVacationIPage = factoryVacationMapper.selectListPage(page, queryParam);
        IPage<FactoryVacationVo> pageVO = FactoryVacationWrapper.build().pageVO(factoryVacationIPage);

        for (FactoryVacationVo record : pageVO.getRecords()) {
            record.adjustButton(record.getStatus());
            if(StrUtil.equals(record.getSourceType(),MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())){
                record.setPermissionButton(MrpVacationOperateRel.operations(button));
            }else{
                record.setPermissionButton(MrpVacationOperateRel.operations(false));
            }

            //设置假期类型
            if(StrUtil.equals(record.getSourceType(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())){
                record.setVacationType(MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode());
            }else if(StrUtil.equals(record.getSourceType(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode())){
                record.setVacationType(MrpVacationTypeEnum.DEFAULT_HOLIDAY.getCode());
            }

        }

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void vacationSave(FactoryVacationSetParam param) {
        //计算一共有多少天日期
        List<Date> dateList = BuildDayUtil.howManyDayInDateList(param.getStartDate(), param.getEndDate());

        List<FactoryInfoParam> factoryInfoParamList = param.getFactoryInfoParamList();
        //按照工厂分组
        Map<Long, FactoryInfoParam> factoryInfoGroupByFactoryId = factoryInfoParamList.stream().collect(Collectors.toMap(FactoryInfoParam::getId, item -> item));

        //可以新增的手动节假日都放在这里
        Map<FactoryInfoParam, List<Date>> factoryInfoParamListHashMap = new HashMap<>();
        //发生冲突 需要覆盖原来的节假日的数据放这里
        Map<FactoryInfoParam, List<FactoryVacation>> factoryInfoParamListUpdateHashMap = new HashMap<>();
        for (Map.Entry<Long, FactoryInfoParam> entry : factoryInfoGroupByFactoryId.entrySet()) {

            //构造不同工厂的dateList
            FactoryInfoParam factoryInfoParam = entry.getValue();
            List<FactoryVacation> thisFactoryVacationList = this.list(Wrappers.<FactoryVacation>lambdaQuery()
                    .eq(FactoryVacation::getFactoryId, factoryInfoParam.getId())
                    .in(FactoryVacation::getVacationDate, dateList));

            //需要更新  覆盖原来的禁用状态  禁用状态不管是手动 默认非 法定 直接覆盖就行了
            List<FactoryVacation> disableFactoryVacationList = thisFactoryVacationList.stream().filter(item -> item.getStatus().equals(MrpVacationStatusEnum.DISABLE.getCode())).collect(Collectors.toList());

            //启用状态 法定和非默认不覆盖 但手动新增 原来是节假日  现在变成调休 那就有可能需要更新  所以先得到启用状态中的手动新增
            List<FactoryVacation> vacationForAdd = thisFactoryVacationList.stream().filter(item -> !item.getStatus().equals(MrpVacationStatusEnum.DISABLE.getCode())).filter(item -> item.getSourceType().equals(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())).collect(Collectors.toList());

            //获取手动新增日期
            List<Date> vacationForAddDates = vacationForAdd.stream().map(item -> item.getVacationDate()).collect(Collectors.toList());
            //再得到启动状态 非手动新增的节假日的日期
            List<Date> thisFactoryVacationDateList = thisFactoryVacationList.stream().filter(item -> !item.getStatus().equals(MrpVacationStatusEnum.DISABLE.getCode())).filter(item -> !item.getSourceType().equals(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())).map(item -> item.getVacationDate()).collect(Collectors.toList());

            //调用覆盖方法 去掉法定和非默认的日期
            List<Date> thisFactoryDateList=coverDate(dateList,thisFactoryVacationDateList);
            //调用覆盖方法 去掉要更新的手动新增的日期(因为是更新,所以新增的list中不需要)
            List<Date> thisFactoryDateList2=coverDate(thisFactoryDateList,vacationForAddDates);

            //需要新增的日期
            factoryInfoParamListHashMap.put(factoryInfoParam,thisFactoryDateList2);

            //需要覆盖的加上需要更新的手动新增,那就是所有需要进行覆盖更新的
            disableFactoryVacationList.addAll(vacationForAdd);
            //需要覆盖的日期
            factoryInfoParamListUpdateHashMap.put(factoryInfoParam,disableFactoryVacationList);

        }
        this.buildVacation(param, factoryInfoParamListHashMap, factoryInfoParamListUpdateHashMap);
    }

    private List<Date> coverDate(List<Date> dateList, List<Date> thisFactoryVacationDateList) {
        List<Date> collect = dateList.stream().filter(item -> !thisFactoryVacationDateList.contains(item)).collect(Collectors.toList());
        return collect;
    }


    @Override
    public void batchDeleteVacation(List<Long> ids) {
        Collection<FactoryVacation> factoryVacations = this.listByIds(ids);
        factoryVacations.forEach(item->{
            if(StrUtil.equals(item.getSourceType(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())
                    || StrUtil.equals(item.getSourceType(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode())){
                throw new ApiException(500,"不能删除订阅的法定节假日和非默认工作日的休息日");
            }
        });

        factoryVacationMapper.deleteByVacationIds(ids);
    }


    @Override
    public void importVacation(FactoryVacationImportVo vo) {
        List<FactoryVacation> listForSave = vo.getListForSave();
        List<FactoryVacation> listForUpdate = vo.getListForUpdate();

        if(CollectionUtil.isNotEmpty(listForSave)){
            this.saveBatch(listForSave);
        }
        if(CollectionUtil.isNotEmpty(listForUpdate)){
            this.updateBatchById(listForUpdate);
        }
        /*returnList.forEach(vo->{
                //校验该工厂编码是否存在工厂信息
                FactoryInfo factoryInfo = factoryInfoService.getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getNumber, vo.getNumber()));
                if (ObjectUtil.isEmpty(factoryInfo)){
                    throw new RuntimeException("导入不存在的工厂编码的节假日信息");
                }


                //校验是否已存在该工厂，该日期的节假日
                FactoryVacation factoryVacation = getOne(Wrappers.<FactoryVacation>lambdaQuery().eq(FactoryVacation::getFactoryNumber, vo.getNumber())
                        .eq(FactoryVacation::getVacationDate, vo.getVacationDate()));
                if (ObjectUtil.isNotEmpty(factoryVacation)&&){
                    log.error("该工厂在该日期下已经存在假期安排");
                    throw new ApiException(500, "该工厂在该日期下已经存在假期安排");
                }
                FactoryVacation vacation = new FactoryVacation()
                        //.setFactoryNumber(vo.getNumber())
                        .setFactoryNumber(vo.getNumber())
                        .setFactoryName(vo.getFactoryName())
                        .setVacationDate(vo.getVacationDate())
                        .setVacationType(vo.getVacationType())
                        .setStatus(MrpVacationStatusEnum.ENABLE.getCode())
                        .setBelongCode(factoryInfo.getBelongCode())
                        .setBelongType(factoryInfo.getBelongType())
                        .setFactoryId(factoryInfo.getId())
                        .setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());

                factoryVacationList.add(vacation);

            });*/
        //saveBatch(factoryVacationList);

    }



    @Override
    public void buildHolidayForSave(List<FactoryVacationParam> factoryVacationParamList ){
        //新增/编辑
        List<Date> dateList = factoryVacationParamList.stream().map(item -> item.getVacationDate())
                .collect(Collectors.toList());
        List<Long> factoryIdList = factoryVacationParamList.stream().map(item -> item.getFactoryId()).collect(Collectors.toList());


        List<LocalDate> localDateList = new ArrayList();

        //日期转换
        for (Date date : dateList) {
            localDateList.add(BuildDayUtil.dateToLocalDate(date));
        }



        //取出所有的工厂的 并且和需要新增/编辑的日期重合的节假日的数据  有可能为空  也有可能某个工厂的为空 也有可能某个工厂的某个日期的节假日为空
        List<FactoryVacation> factoryVacationListForSelect = this.getBaseMapper().selectList(Wrappers
                .<FactoryVacation>lambdaQuery()
                .and(item->item.in(FactoryVacation::getVacationDate, dateList).in(FactoryVacation::getFactoryId, factoryIdList)));

        List<FactoryVacation> factoryVacationList = new ArrayList<>();
        List<FactoryVacation> factoryVacationListForUpdate = new ArrayList<>();


        if(factoryVacationListForSelect.isEmpty()){
            //为空直接插入就行
            for (FactoryVacationParam factoryVacationParam : factoryVacationParamList) {

                nonExistHandler(factoryVacationParam,factoryVacationList);

            }
        }else{
            //存在处理不同情况的方法
            differentSituationsMethod(factoryVacationParamList,factoryVacationList,factoryVacationListForUpdate,factoryVacationListForSelect);

        }

        //更新或保存
        List<FactoryVacation> saveUpdateList = new ArrayList<>();
        saveUpdateList.addAll(factoryVacationList);
        saveUpdateList.addAll(factoryVacationListForUpdate);
        this.saveOrUpdateBatch(saveUpdateList);

    }

    /**
     * 查询的假期不为空,处理不同情况的方法
     * */
    private void differentSituationsMethod(List<FactoryVacationParam> factoryVacationParamList, List<FactoryVacation> factoryVacationList, List<FactoryVacation> factoryVacationListForUpdate, List<FactoryVacation> factoryVacationListForSelect) {
        //不为空,查询,再更新
        //先按工厂id分组
        Map<Long, List<FactoryVacation>> factoryVacationGroupByFactoryId = factoryVacationListForSelect
                .stream()
                .collect(Collectors.groupingBy(FactoryVacation::getFactoryId));

        for (FactoryVacationParam factoryVacationParam : factoryVacationParamList) {
            HashMap<LocalDate, FactoryVacation> mapForExist = new HashMap<>();
            Long factoryId = factoryVacationParam.getFactoryId();
            if(factoryVacationGroupByFactoryId.get(factoryId)!=null){
                //说明有这个的假期
                List<FactoryVacation> factoryVacationListForExist = factoryVacationGroupByFactoryId.get(factoryId);

                //把这一轮的工厂的这些已经存在数据库的假期进行循环  日期放到一个map中
                for (FactoryVacation factoryVacation : factoryVacationListForExist) {
                    LocalDate localDate = BuildDayUtil.dateToLocalDate(factoryVacation.getVacationDate());
                    mapForExist.put(localDate,factoryVacation);
                }

                LocalDate startDate = BuildDayUtil.stringToLocalDate(factoryVacationParam.getStartDate());
                LocalDate endDate = BuildDayUtil.stringToLocalDate(factoryVacationParam.getEndDate());
                while(endDate.isAfter(startDate)) {
                    boolean flag=false;//默认这一天的日期没有已经存在数据库假期的数据
                    FactoryVacation factoryVacation = mapForExist.get(startDate);
                    if(ObjectUtil.isNotEmpty(factoryVacation)){
                        //说明存在这条假期,进行判断
                        flag=true;
                    }

                    //存在,那么就有三种情况
                    if(flag){

                        if(StrUtil.equals(factoryVacation.getSourceType(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode()) ){
                            //法定节假日,不用改直接跳过
                            continue;
                        }else if(StrUtil.equals(factoryVacation.getSourceType(),MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode())){
                            //手动新增的,有可能是关闭状态,那就启用,更新状态
                            factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                            factoryVacationListForUpdate.add(factoryVacation);
                            continue;
                        }else {
                            //可以覆盖的默认假日
                            factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                            factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                            factoryVacationListForUpdate.add(factoryVacation);
                            continue;
                        }

                    }
                    FactoryVacation factoryVacationForAdd=buildEntity(startDate,factoryVacationParam);
                    factoryVacationList.add(factoryVacationForAdd);
                    startDate = startDate.plusDays(1);
                }

            }else{
                //这段时间没有假期,直接保存
                nonExistHandler(factoryVacationParam,factoryVacationList);

            }
        }
    }

    /**
     * 为空不存在的循环处理方法
     * */
    private void nonExistHandler(FactoryVacationParam factoryVacationParam, List<FactoryVacation> factoryVacationList) {
        LocalDate startDate = BuildDayUtil.stringToLocalDate(factoryVacationParam.getStartDate());
        LocalDate endDate = BuildDayUtil.stringToLocalDate(factoryVacationParam.getEndDate());

        while(endDate.isAfter(startDate)){

            FactoryVacation factoryVacation=buildEntity(startDate,factoryVacationParam);

            factoryVacationList.add(factoryVacation);


            startDate=startDate.plusDays(1);
        }
    }

    /**
     * 构建假日模型方法
     * */
    private FactoryVacation buildEntity(LocalDate startDate, FactoryVacationParam factoryVacationParam) {
        FactoryVacation factoryVacation = new FactoryVacation();
        factoryVacation.setBelongType(factoryVacationParam.getBelongType());//属于供应商/采购商
        //说明是供应方
        if(factoryVacationParam.getBelongCode()!=null){
            factoryVacation.setBelongCode(factoryVacationParam.getBelongCode());
        }else{
            //采购方
            factoryVacation.setBelongCode(AuthUtil.getUser().getCurrentOrg().getId()+"");
        }
        factoryVacation.setVacationType(factoryVacationParam.getVacationType());
        factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
        factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(startDate));
        factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(startDate));
        factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
        factoryVacation.setFactoryId(factoryVacationParam.getFactoryId());
        factoryVacation.setFactoryName(factoryVacationParam.getFactoryName());
        return factoryVacation;
    }

    /**
     *根据开关是否订阅法定节假日
     **/
    @Override
    @Transactional
    public void autoVacation(AutoSubscribeParam param) {
        if(param.getChanger().equals("true")){
            //调用订阅方法
            //如果是采购方  全局控制
            if(StrUtil.equals(param.getBelongType(), WorkBench.PURCHASE.getCode())){

                autoSubscribeForPurchase(param);
            }
            //供应方
            else{
                autoSubscribe(param);
            }

        }else{
            //调用去掉订阅方法 (根据产品意思不需要做处理)
            //disAutoSubscribe(param);
        }

    }

    @Override
    public void deleteAllVacationFromFactoryId(List<Long> ids) {
        factoryVacationMapper.deleteAllVacationFromFactoryId(ids);
    }

    /**
     * 修改配置取消自动订阅方法
     * */
    private void disAutoSubscribe(AutoSubscribeParam param) {
        //删除的list
        List<FactoryVacation> listForDelete=new ArrayList<>();
        //更新的list
        List<FactoryVacation> listForUpdate=new ArrayList<>();


        //todo 取出所有的工厂的所有的法定节假日   需要过滤时间吗? 不需要  定时任务定时删除过期时间  并且补全时间
//        Map<String, List<LocalDate>> stringListMap = holidayDateService.selectAllHoliday();
//        List<LocalDate> localDateListAuto = stringListMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
//        List<LocalDate> localDateListAuto996 = stringListMap.get(HolidayDateEnum.IS_996.getCode());

        LambdaQueryWrapper<FactoryVacation> wrapper = Wrappers.<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getBelongCode,param.getCode())
                .eq(FactoryVacation::getSourceType, MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());



        List<FactoryVacation> factoryVacationList = this.getBaseMapper().selectList(wrapper);

        List<Long> collect = factoryVacationList.stream().map(FactoryVacation::getFactoryId).distinct().collect(Collectors.toList());

        //通过上面的节假日拿到的工厂id 取出对应的工厂, 如果是使用用默认配置的工厂的话那么就不会在workDay表中
        List<FactoryInfo> factoryInfoList = factoryInfoMapper.selectList(Wrappers.<FactoryInfo>lambdaQuery()
                .in(FactoryInfo::getId, collect));
        //取出所有采购,获取他们的工作日信息,通过工作日可以知道休息日  注意为空判断
        List<FactoryWorkday> factoryWorkdayList = factoryWorkdayMapper.selectList(Wrappers.<FactoryWorkday>lambdaQuery()
                .in(FactoryWorkday::getFactoryId, collect));

        //非空判断之后  取出存在工作日表中的所有的工厂的id
        List<Long> factoryWorkdayIds = factoryWorkdayList.stream().map(FactoryWorkday::getFactoryId).collect(Collectors.toList());
        //过滤工作日表中的id,剩下就是使用默认配置的工厂了
        List<Long> factoryInfoForFilterIdList = factoryInfoList.stream()
                    .filter(item -> !factoryWorkdayIds.contains(item.getId()))
                    .map(item -> item.getId())
                    .collect(Collectors.toList());


        //获取默认全局的节假日的localDayList
        Map<String, Integer> defaultWorkDayConfig = holidayService.getDefaultWorkDayConfig(DeliveryCfgParam.MRP_DEFAULT_WORKDAY.getCode());
        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(defaultWorkDayConfig.get("start"), defaultWorkDayConfig.get("end"));
        LocalDate day = BuildDayUtil.day();
        List<LocalDate> localDateForDefaultList = BuildDayUtil.calHolidayForYear(integerList, day);

        //获取所有节假日里面使用默认全局的节假日的所有的工厂的所有的节假日
        List<FactoryVacation> factoryVacationForUserDefaultHolidayList = factoryVacationList
                .stream()
                .filter(item -> factoryInfoForFilterIdList.contains(item.getFactoryId()))
                .collect(Collectors.toList());



        //for循环,判断是否包含在默认的全局的节假日,包含,说明 要更改这天的手动/订阅节假日,变成默认配置的
        //不包含,直接删除
        for (FactoryVacation factoryVacation : factoryVacationForUserDefaultHolidayList) {
            LocalDate date = BuildDayUtil.dateToLocalDate(factoryVacation.getVacationDate());
            if(localDateForDefaultList.contains(date)){
                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode());
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                listForUpdate.add(factoryVacation);
            }else{
                listForDelete.add(factoryVacation);
            }
        }

        //todo 查看现在的默认节假日是否够一年,不够补全  定时任务补全就行了


        //有自己默认配置工作日的工厂的处理

        //把这些工厂的workDay按id分组 循环会用到
        Map<Long, List<FactoryWorkday>> factoryWorkdayMap = factoryWorkdayList
                .stream()
                .collect(Collectors.groupingBy(FactoryWorkday::getFactoryId));

        //过滤工作日表中的有自己默认工作日配置的工厂  并且按工厂id分组
        Map<Long, List<FactoryVacation>> factoryVacationForUserItHolidayMap = factoryVacationList
                .stream()
                .filter(item -> factoryWorkdayIds.contains(item.getFactoryId()))

                .collect(Collectors.groupingBy(FactoryVacation::getFactoryId));

        Set<Map.Entry<Long, List<FactoryVacation>>> entries = factoryVacationForUserItHolidayMap.entrySet();
        for (Map.Entry<Long, List<FactoryVacation>> entry : entries) {
            Long key = entry.getKey();
            FactoryWorkday factoryWorkday = factoryWorkdayMap.get(key).get(0);
            List<Integer> integerListForThisRun = BuildDayUtil.returnHolidayForWeekArray(factoryWorkday.getWeekStartNum(), factoryWorkday.getWeekEndNum());

            List<FactoryVacation> value = entry.getValue();
            for (FactoryVacation factoryVacation : value) {
                int week = BuildDayUtil.dateToLocalDate(factoryVacation.getVacationDate()).getDayOfWeek().getValue();
                if(integerListForThisRun.contains(week)){
                    factoryVacation.setSourceType(MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode());
                    factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                    listForUpdate.add(factoryVacation);
                }else{
                    listForDelete.add(factoryVacation);
                }
            }
        }

        if(!listForDelete.isEmpty()){
            List<Long> deleteList = listForDelete.stream().map(BaseEntity::getId).collect(Collectors.toList());
            this.getBaseMapper().deleteBatchIds(deleteList);
        }
        if(!listForUpdate.isEmpty()){
            this.updateBatchById(listForUpdate);
        }


        //todo  不够一年的需要补全一年的默认假日时间  定时任务补全


    }

    /**
     * 修改供应方配置的自动订阅方法
     * 注意:订阅法定节假日是不需要考虑补全一年的
     * */
    private void autoSubscribe(AutoSubscribeParam param) {
        //false-不需要操作  true-需要操作
        Boolean operFlag=needSubscribeOper(param);

        //校验当前工厂有没有工厂,有那就接着循环,没有那就跳过
        FactoryWorkday factoryWorkday = factoryWorkdayMapper.selectOne(Wrappers
                .<FactoryWorkday>lambdaQuery()
                .eq(FactoryWorkday::getBelongCode, param.getCode()));
        if(ObjectUtil.isEmpty(factoryWorkday)){
            return;
        }



        if(operFlag){
            //方法,获取法定节假日,得到auto的法定假节日和上班的日期
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();//todo  检查取的是否正确

            //法定节假日
            List<LocalDate> autoLocalDate = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            //法定节假日补班的日子
            List<LocalDate> autoLocalDateIs996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());

            List<Date> dateListForAuto = BuildDayUtil.localdateListToDateList(autoLocalDate);
            List<Date> dateListForAuto996 = BuildDayUtil.localdateListToDateList(autoLocalDateIs996);

            LambdaQueryWrapper<FactoryVacation> wrapper = Wrappers.<FactoryVacation>lambdaQuery()
                    .eq(FactoryVacation::getBelongCode,param.getCode())
                    .in(FactoryVacation::getVacationDate,dateListForAuto);
            LambdaQueryWrapper<FactoryVacation> wrapper996 = Wrappers.<FactoryVacation>lambdaQuery()
                    .eq(FactoryVacation::getBelongCode,param.getCode())
                    .in(FactoryVacation::getVacationDate,dateListForAuto996);

            //取出这个采购/供应的所有工厂的所有的假日
            List<FactoryVacation> factoryVacationListForSelect = this.getBaseMapper().selectList(wrapper);
            List<FactoryVacation> factoryVacationListForSelect996 = this.getBaseMapper().selectList(wrapper996);


            List<FactoryVacation> factoryVacationListForSave=new ArrayList();
            List<FactoryVacation> factoryVacationListForUpdate=new ArrayList();
            List<FactoryVacation> factoryVacationListForDelete=new ArrayList();//法定上班(补班) 如果有重合,那就要干掉这天的假期

            //构建法定节假日
            buildList(factoryWorkday,param, dateListForAuto, factoryVacationListForSelect,  factoryVacationListForSave, factoryVacationListForUpdate,MrpVacationStatusEnum.ENABLE.getCode());

            //法定加班进行过滤
            conflict996( dateListForAuto996, factoryVacationListForSelect996,  factoryVacationListForDelete);

            if(CollectionUtil.isNotEmpty(factoryVacationListForSave)){
                this.saveBatch(factoryVacationListForSave);
            }
            if(CollectionUtil.isNotEmpty(factoryVacationListForUpdate)){
                this.updateBatchById(factoryVacationListForUpdate);
            }
            if(CollectionUtil.isNotEmpty(factoryVacationListForDelete)){
                this.batchDeleteVacation(factoryVacationListForDelete.stream().map(item->item.getId()).collect(Collectors.toList()));
            }

        }
    }

    /**
     * 法定加班进行过滤
     * */
    private void conflict996( List<Date> dateListForAuto996, List<FactoryVacation> factoryVacationListForSelect996, List<FactoryVacation> factoryVacationListForDelete) {
        List<FactoryVacation> conflictList = factoryVacationListForSelect996.stream().filter(item -> dateListForAuto996.contains(item.getVacationDate())).collect(Collectors.toList());
        factoryVacationListForDelete.addAll(conflictList);
    }

    /**
     * 供应方判断是否需要订阅操作
     * */
    private Boolean needSubscribeOper(AutoSubscribeParam param) {
        //false-不需要操作  true-需要操作
        Boolean operFlag=true;
        //获取当前年份
        Integer nowYear = Integer.valueOf(BuildDayUtil.year(BuildDayUtil.day()));
        //获取当前操作的是采购还是供应
        String code = param.getCode();

        //获取当前编码的供应商的法定节假日
        LambdaQueryWrapper<FactoryVacation> wrapper = Wrappers
                .<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getSourceType, MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())
                .eq(FactoryVacation::getBelongCode,code);

        List<FactoryVacation> list = this.list(wrapper);

        //不为空,判断年份是否当前年份 是 说明已经订阅 否 删除然后订阅这些年份
        if(CollectionUtil.isNotEmpty(list)){
            Date vacationDate = list.get(0).getVacationDate();
            LocalDate localDate = BuildDayUtil.dateToLocalDate(vacationDate);
            Integer selectYear = localDate.getYear();

            if(nowYear.equals(selectYear)){
                operFlag=false;
            }
            //不是当前年份需要操作
            else{
                List<Long> idsForDelete = list.stream().map(item -> item.getId()).collect(Collectors.toList());
                this.batchDeleteVacation(idsForDelete);
                operFlag=true;
            }
        }
        //为空 那就需要订阅操作
        else{
            operFlag=true;
        }
        return operFlag;
    }

    /**
     * 采购方判断是否需要订阅操作
     * */
    private Boolean needSubscribeOperForPurchase(AutoSubscribeParam param) {
        //false-不需要操作  true-需要操作
        Boolean operFlag=true;
        //获取当前年份
        Integer nowYear = Integer.valueOf(BuildDayUtil.year(BuildDayUtil.day()));
        //获取当前操作的是采购还是供应
        String code = param.getCode();

        //获取当前编码的供应商的法定节假日
        LambdaQueryWrapper<FactoryVacation> wrapper = Wrappers
                .<FactoryVacation>lambdaQuery()
                .eq(FactoryVacation::getSourceType, MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())
                .eq(FactoryVacation::getBelongType,WorkBench.PURCHASE.getCode());

        List<FactoryVacation> list = this.list(wrapper);

        //不为空,判断年份是否当前年份 是 说明已经订阅 否 删除然后订阅这些年份
        if(CollectionUtil.isNotEmpty(list)){
            Date vacationDate = list.get(0).getVacationDate();
            LocalDate localDate = BuildDayUtil.dateToLocalDate(vacationDate);
            Integer selectYear = localDate.getYear();

            if(nowYear.equals(selectYear)){
                operFlag=false;
            }
            //不是当前年份需要操作
            else{
                List<Long> idsForDelete = list.stream().map(item -> item.getId()).collect(Collectors.toList());
                this.batchDeleteVacation(idsForDelete);
                operFlag=true;
            }
        }
        //为空 那就需要订阅操作
        else{
            operFlag=true;
        }
        return operFlag;
    }

    /**
     * 修改采购方配置的自动订阅方法  全局控制
     * 注意:订阅法定节假日是不需要考虑补全一年的
     * */
    private void autoSubscribeForPurchase(AutoSubscribeParam param) {
        //false-不需要操作  true-需要操作
        Boolean operFlag=needSubscribeOperForPurchase(param);

        if(operFlag){
            //方法,获取法定节假日,得到auto的法定假节日和上班的日期
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();//todo  检查取的是否正确

            //法定节假日
            List<LocalDate> autoLocalDate = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            //法定节假日补班的日子
            List<LocalDate> autoLocalDateIs996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());

            List<Date> dateListForAuto = BuildDayUtil.localdateListToDateList(autoLocalDate);
            List<Date> dateListForAuto996 = BuildDayUtil.localdateListToDateList(autoLocalDateIs996);

            //获取所有的采购方
            R<List<PurchaserVo>> purInfoR = purchaserFeignClient.queryAllPurchaserInfo();
            Asserts.isTrue(purInfoR.isSuccess()&&CollectionUtil.isNotEmpty(purInfoR.getData()),"远程获取采购方信息异常"+purInfoR.getMsg());
            List<PurchaserVo> purInfoVo = purInfoR.getData();


            List<FactoryVacation> factoryVacationListForSaveAll=new ArrayList();
            List<FactoryVacation> factoryVacationListForUpdateAll=new ArrayList();
            List<FactoryVacation> factoryVacationListForDeleteAll=new ArrayList();//法定上班(补班) 如果有重合,那就要干掉这天的假期
            for (PurchaserVo purchaserVo : purInfoVo) {

                //校验当前工厂有没有工厂,有那就接着循环,没有那就跳过
                FactoryWorkday factoryWorkday = factoryWorkdayMapper.selectOne(Wrappers
                        .<FactoryWorkday>lambdaQuery()
                        .eq(FactoryWorkday::getBelongCode, purchaserVo.getCode()));
                if(ObjectUtil.isEmpty(factoryWorkday)){
                    continue;
                }


                String purchaseCode = purchaserVo.getCode();

                LambdaQueryWrapper<FactoryVacation> wrapper = Wrappers.<FactoryVacation>lambdaQuery()
                        .eq(FactoryVacation::getBelongCode,purchaseCode)
                        .in(FactoryVacation::getVacationDate,dateListForAuto);
                LambdaQueryWrapper<FactoryVacation> wrapper996 = Wrappers.<FactoryVacation>lambdaQuery()
                        .eq(FactoryVacation::getBelongCode,purchaseCode)
                        .in(FactoryVacation::getVacationDate,dateListForAuto996);

                //取出这个采购的所有工厂的所有的假日
                List<FactoryVacation> factoryVacationListForSelect = this.getBaseMapper().selectList(wrapper);
                List<FactoryVacation> factoryVacationListForSelect996 = this.getBaseMapper().selectList(wrapper996);

                List<FactoryVacation> factoryVacationListForSave=new ArrayList();
                List<FactoryVacation> factoryVacationListForUpdate=new ArrayList();
                List<FactoryVacation> factoryVacationListForDelete=new ArrayList();//法定上班(补班) 如果有重合,那就要干掉这天的假期

                //构建法定节假日
                buildListForPurchase(factoryWorkday,param, dateListForAuto, factoryVacationListForSelect,  factoryVacationListForSave, factoryVacationListForUpdate,MrpVacationStatusEnum.ENABLE.getCode(),purchaserVo);

                //法定加班进行过滤
                conflict996( dateListForAuto996, factoryVacationListForSelect996,  factoryVacationListForDelete);

                factoryVacationListForSaveAll.addAll(factoryVacationListForSave);
                factoryVacationListForUpdateAll.addAll(factoryVacationListForUpdate);
                factoryVacationListForDeleteAll.addAll(factoryVacationListForDelete);

            }

            if(CollectionUtil.isNotEmpty(factoryVacationListForSaveAll)){
                this.saveBatch(factoryVacationListForSaveAll);
            }
            if(CollectionUtil.isNotEmpty(factoryVacationListForUpdateAll)){
                this.updateBatchById(factoryVacationListForUpdateAll);
            }
            if(CollectionUtil.isNotEmpty(factoryVacationListForDeleteAll)){
                this.batchDeleteVacation(factoryVacationListForDeleteAll.stream().map(item->item.getId()).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 标记已经订阅了法定节假日的工厂
     */
    private void saveNeedSubscribe(AutoSubscribeParam param, Set<Long> factoryInfoNeedSubscribeIds) {
        String code = param.getCode();
        String year = BuildDayUtil.year(BuildDayUtil.day());
        List<FactorySubscribe> collect = factoryInfoNeedSubscribeIds.stream().map(item -> {
            FactorySubscribe factorySubscribe = new FactorySubscribe();
            factorySubscribe.setBelongCode(code);
            factorySubscribe.setFactoryId(item);
            factorySubscribe.setYear(year);
            return factorySubscribe;
        }).collect(Collectors.toList());
        factorySubscribeService.saveBatch(collect);
    }

    /**
     * 判断操作开关是否需要更改当前采购/供应的工厂的假期数据
     * */
    private Map<Long, FactoryInfo> isSubscribe(AutoSubscribeParam param) {
        //判断当前的采购/供应是否有工厂没有订阅,如果都订阅 直接返回
        String code = param.getCode();
        //取出当前年份
        String year = BuildDayUtil.year(BuildDayUtil.day());
        //取出今年已经订阅过法定节假日的所有的工厂
        List<FactorySubscribe> factorySubscribeList=factorySubscribeService.listAllSubscribeForLocalYear(code,year);
        //取出当前供应/采购的所有工厂
        List<FactoryInfo> factoryInfos = factoryInfoMapper.selectList(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongCode, code));
        //
        List<Long> ids = factorySubscribeList.stream().map(item -> item.getFactoryId()).collect(Collectors.toList());

        Map<Long, FactoryInfo> factoryInfoNeedSubscribeMap = factoryInfos.stream().filter(item -> !ids.contains(item.getId())).collect(Collectors.toMap(FactoryInfo::getId, item -> item));

        return factoryInfoNeedSubscribeMap;
    }


    //                                     传进来的参数                 法定节假/补班的list            查询的有重合的节假日/补班                                   按工厂id分组的节假日
    private void buildListForDelete(AutoSubscribeParam param, List<Date> dateListForAuto996, List<FactoryVacation> factoryVacationListForSelect996, Map<Long, List<FactoryVacation>> collectGroup996, List<FactoryVacation> factoryVacationListForSave, List<FactoryVacation> factoryVacationListForDelete, Integer code) {
        for (Map.Entry<Long, List<FactoryVacation>> entry : collectGroup996.entrySet()) {
            Long factoryId = entry.getKey();
            List<FactoryVacation> value = entry.getValue();
            //按日期划分
            Map<Date, FactoryVacation> factoryVacationListForSelectMap996 = factoryVacationListForSelect996.stream().collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));
            //取出日期的list
            List<Date> dateListSelectForAuto996 = factoryVacationListForSelect996.stream().map(FactoryVacation::getVacationDate).collect(Collectors.toList());

            //循环日期判断是否包含假日
            for (Date date : dateListForAuto996) {
                if (dateListSelectForAuto996.contains(date)){
                    //删除
                    FactoryVacation factoryVacation = factoryVacationListForSelectMap996.get(date);
                    factoryVacationListForDelete.add(factoryVacation);
                }

            }

        }
    }

    /**
     * 法定节假日处理(供应方使用)
     * */
    //                       传进来的参数                 法定节假/补班的list            查询的有重合的节假日/补班
    private void buildList(FactoryWorkday factoryWorkday,AutoSubscribeParam param, List<Date> dateListForAuto, List<FactoryVacation> factoryVacationListForSelect,   List<FactoryVacation> factoryVacationListForSave, List<FactoryVacation> factoryVacationListForUpdate,Integer statusCode) {
            //按日期划分
            Map<Date, FactoryVacation> factoryVacationListForSelectMap996 = factoryVacationListForSelect.stream().collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));
            //取出日期的list
            List<Date> dateListSelectForAuto = factoryVacationListForSelect.stream().map(FactoryVacation::getVacationDate).collect(Collectors.toList());

            //循环日期创建或更新假日
            for (Date date : dateListForAuto) {
                if (dateListSelectForAuto.contains(date)){
                    //更新
                    FactoryVacation factoryVacation = factoryVacationListForSelectMap996.get(date);
                    factoryVacation.setSourceType(MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());
                    factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                    factoryVacation.setVacationType(MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode());
                    factoryVacationListForUpdate.add(factoryVacation);
                    continue;
                }

                //新增
                FactoryVacation factoryVacation = new FactoryVacation();
                factoryVacation.setBelongType(param.getBelongType());
                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());
                factoryVacation.setVacationDate(date);
                factoryVacation.setVacationType(MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode());
                factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(BuildDayUtil.dateToLocalDate(date)));
                factoryVacation.setStatus(statusCode);
                //所属信息
                factoryVacation.setBelongCode(param.getCode());
                factoryVacation.setFactoryNumber(param.getCode());
                //名字信息
                factoryVacation.setFactoryName(factoryWorkday.getFactoryName());
                factoryVacation.setFactoryId(factoryWorkday.getFactoryId());
                factoryVacation.setBelongName(factoryWorkday.getBelongName());
                factoryVacationListForSave.add(factoryVacation);
            }

    }
    /**
     * 法定节假日处理(采购方使用)
     * */
    //                       传进来的参数                 法定节假/补班的list            查询的有重合的节假日/补班
    private void buildListForPurchase(FactoryWorkday factoryWorkday,AutoSubscribeParam param, List<Date> dateListForAuto, List<FactoryVacation> factoryVacationListForSelect,   List<FactoryVacation> factoryVacationListForSave, List<FactoryVacation> factoryVacationListForUpdate,Integer statusCode,PurchaserVo purchaserVo) {
            //按日期划分
            Map<Date, FactoryVacation> factoryVacationListForSelectMap996 = factoryVacationListForSelect.stream().collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));
            //取出日期的list
            List<Date> dateListSelectForAuto = factoryVacationListForSelect.stream().map(FactoryVacation::getVacationDate).collect(Collectors.toList());

            //循环日期创建或更新假日
            for (Date date : dateListForAuto) {
                if (dateListSelectForAuto.contains(date)){
                    //更新
                    FactoryVacation factoryVacation = factoryVacationListForSelectMap996.get(date);
                    factoryVacation.setSourceType(MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());
                    factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                    factoryVacation.setVacationType(MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode());
                    factoryVacationListForUpdate.add(factoryVacation);
                    continue;
                }

                //新增
                FactoryVacation factoryVacation = new FactoryVacation();
                factoryVacation.setBelongType(param.getBelongType());

                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());
                factoryVacation.setVacationDate(date);
                factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(BuildDayUtil.dateToLocalDate(date)));
                factoryVacation.setStatus(statusCode);


                factoryVacation.setBelongCode(purchaserVo.getCode());
                factoryVacation.setFactoryNumber(purchaserVo.getCode());
                factoryVacation.setVacationType(MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode());

                factoryVacation.setFactoryName(factoryWorkday.getFactoryName());
                factoryVacation.setFactoryId(factoryWorkday.getFactoryId());
                factoryVacation.setBelongName(factoryWorkday.getBelongName());

                factoryVacationListForSave.add(factoryVacation);
            }

    }


    /**
     * 构建法定假日模型方法
     * */
    private FactoryVacation buildEntityForDefault(LocalDate date,FactoryInfo factoryInfo) {
        FactoryVacation factoryVacation = new FactoryVacation();
        factoryVacation.setBelongType(factoryInfo.getBelongType());
        factoryVacation.setBelongCode(factoryInfo.getBelongCode());
        factoryVacation.setSourceType(MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode());
        factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(date));
        factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(date));
        factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
        factoryVacation.setFactoryId(factoryInfo.getId());
        factoryVacation.setFactoryName(factoryInfo.getName());
        return factoryVacation;
    }


    /**
     * 添加和编辑日期的时候进行构建数据的方法
     * */
    @Override
    public void buildVacation(FactoryVacationSetParam param,Map<FactoryInfoParam, List<Date>> factoryInfoParamListHashMap,Map<FactoryInfoParam, List<FactoryVacation>> factoryInfoParamListUpdateHashMap) {
        List<FactoryVacation> factoryVacations = new ArrayList<>();
        List<FactoryVacation> factoryVacationsForUpdate = new ArrayList<>();
        List<FactoryInfoParam> factoryInfoParamList = param.getFactoryInfoParamList();
        for (FactoryInfoParam factoryInfoParam : factoryInfoParamList) {
            List<Date> dates = factoryInfoParamListHashMap.get(factoryInfoParam);
            for (Date date : dates) {
                FactoryVacation factoryVacation = new FactoryVacation();
                factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(BuildDayUtil.dateToLocalDate(date)));
                factoryVacation.setVacationDate(date);
                factoryVacation.setFactoryNumber(factoryInfoParam.getNumber());
                factoryVacation.setBelongCode(factoryInfoParam.getBelongCode());
                factoryVacation.setBelongName(factoryInfoParam.getBelongName());
                factoryVacation.setFactoryName(factoryInfoParam.getName());
                factoryVacation.setFactoryId(factoryInfoParam.getId());
                factoryVacation.setBelongType(param.getBelongType());
                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                factoryVacation.setVacationType(param.getVacationType());
                //添加remark 方便数据检验
                factoryVacation.setRemark("手动新增");
                factoryVacations.add(factoryVacation);

            }
            //需要更新(覆盖)
            List<FactoryVacation> factoryVacationListForCover = factoryInfoParamListUpdateHashMap.get(factoryInfoParam);
            for (FactoryVacation factoryVacation : factoryVacationListForCover) {
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                factoryVacation.setVacationType(param.getVacationType());
                factoryVacationsForUpdate.add(factoryVacation);

            }
        }
        if(CollectionUtil.isNotEmpty(factoryVacations)){
            this.saveBatch(factoryVacations);
        }
        if(CollectionUtil.isNotEmpty(factoryVacationsForUpdate)){
            this.updateBatchById(factoryVacationsForUpdate);
        }

    }

    @Override
    public void vacationButton(FactoryVacationParam param) {
        FactoryVacation copy = BeanUtil.copy(param, FactoryVacation.class);
        this.updateById(copy);
    }

    @Override
    @Transactional
    public void vacationUpdate(FactoryVacationSetParam param) {
        factoryVacationMapper.deleteByVacationDate(param.getId());
        this.vacationSave(param);
    }

    @Override
    @Transactional
    public void clearOverdueDate() {
        LocalDate day = BuildDayUtil.day();
        //取出当前时间之前的假日数据 (过期的数据)
        List<FactoryVacation> list = this.list(Wrappers.<FactoryVacation>lambdaQuery()
                .lt(FactoryVacation::getVacationDate, day)
                .eq(FactoryVacation::getSourceType,MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode()));
        List<Long> ids = list.stream().map(FactoryVacation::getId).collect(Collectors.toList());
        //通过id删除
        factoryVacationMapper.deleteByVacationIds(ids);
    }

    @Override
    @Transactional
    public void completionVacation() {
     /*   LocalDate day = BuildDayUtil.day();
        //获取今天是星期几
        int weekOfDay = BuildDayUtil.weekOfDay(BuildDayUtil.day());
        //获取配置
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());

        //取出所有的工厂(包括采购和供应的)
        List<FactoryInfo> factoryInfoList = factoryInfoMapper.selectList(Condition.getQueryWrapper(new FactoryInfo()));

        //取出所有的工厂工作日(包括采购和供应的)
        List<FactoryWorkday> factoryWorkdayList = factoryWorkdayMapper.selectList(Condition.getQueryWrapper(new FactoryWorkday()));

        //==========================================
        //取出采购方的
        List<FactoryInfo> purchaseFactoryInfoList = factoryInfoList.stream().filter(factoryInfo -> StrUtil.equals(factoryInfo.getBelongType(), WorkBench.PURCHASE.getCode())).collect(Collectors.toList());
        List<FactoryWorkday> purchaseFactoryWorkdayList = factoryWorkdayList.stream().filter(factoryWorkday -> StrUtil.equals(factoryWorkday.getBelongType(), WorkBench.PURCHASE.getCode())).collect(Collectors.toList());

        List<Long> purchaseCollectFactoryIdList = purchaseFactoryWorkdayList.stream().map(item -> item.getFactoryId()).collect(Collectors.toList());

        List<FactoryInfo> purchaseFactoryInfoDefautlList = purchaseFactoryInfoList.stream().filter(item -> !purchaseCollectFactoryIdList.contains(item.getId())).collect(Collectors.toList());

        Map<Long, FactoryInfo> factoryInfoMap = factoryInfoList.stream().collect(Collectors.toMap(FactoryInfo::getId, item -> item));

        List<FactoryVacation> factoryVacationArrayList = new ArrayList<>();

        //是否订阅节假日  todo  这里应该可以不用判断 因为只是补全 法定是一年公布一次,所以可以先不管
        JSONObject jsonObjectForAuto = allHolidayAndSubscribeConfig.getJsonObjectForAuto();
        if(holidayService.IsAutoSubscribe(jsonObjectForAuto, param.getCode())) {
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();
            localDates = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            localDatesFor996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());
        }

        allHolidayAndSubscribeConfig.getJsonObjectForAuto()

        Map<String, Integer> defaultWorkdayConfigMap = allHolidayAndSubscribeConfig.getDefaultWorkdayConfigMap();
        Integer start = defaultWorkdayConfigMap.get("start");
        Integer end = defaultWorkdayConfigMap.get("end");

        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(start, end);

        boolean flag=false;
        if(CollectionUtil.isNotEmpty(integerList)){
            //有休息日 创建默认休息日
            flag=integerList.contains(weekOfDay);
        }

        //采购的工厂使用默认全局的添加
        if(flag) {
            for (FactoryInfo factoryInfo : purchaseFactoryInfoDefautlList) {
                FactoryVacation factoryVacation = buildEntityForDefault(day, factoryInfo);
                factoryVacationArrayList.add(factoryVacation);
            }
        }

        //有自己配置默认工作日的采购和供应的工厂
        //List<FactoryWorkday> purchaseFactoryWorkday = purchaseFactoryWorkdayList.stream().filter(item -> StrUtil.equals(item.getBelongType(), WorkBench.PURCHASE.getCode())).collect(Collectors.toList());
        for (FactoryWorkday factoryWorkday : purchaseFactoryWorkdayList) {
            List<Integer> holidays = BuildDayUtil.returnHolidayForWeekArray(factoryWorkday.getWeekStartNum(), factoryWorkday.getWeekEndNum());

            boolean personalFlag=false;
            if(CollectionUtil.isNotEmpty(holidays)){
                //有休息日 创建默认休息日
                personalFlag=integerList.contains(weekOfDay);
            }

            //采购的工厂使用默认全局的添加
            if(personalFlag) {
                FactoryInfo factoryInfo = factoryInfoMap.get(factoryWorkday.getFactoryId());
                FactoryVacation factoryVacation = buildEntityForDefault(day, factoryInfo);
                factoryVacationArrayList.add(factoryVacation);
            }
        }

        //构建完毕 保存
        this.saveBatch(factoryVacationArrayList);*/
    }

    @Override
    public Boolean confirmCover(FactoryVacationSetParam param) {
        //默认不需要提示
        boolean flag=false;
        //计算一共有多少天日期
        List<Date> dateList = BuildDayUtil.howManyDayInDateList(param.getStartDate(), param.getEndDate());

        List<FactoryInfoParam> factoryInfoParamList = param.getFactoryInfoParamList();
        //按照工厂分组
        Map<Long, FactoryInfoParam> factoryInfoGroupByFactoryId = factoryInfoParamList.stream().collect(Collectors.toMap(FactoryInfoParam::getId, item -> item));

        //可以新增的手动节假日都放在这里
        Map<FactoryInfoParam, List<Date>> factoryInfoParamListHashMap = new HashMap<>();
        //发生冲突 需要覆盖原来的节假日的数据放这里
        Map<FactoryInfoParam, List<FactoryVacation>> factoryInfoParamListUpdateHashMap = new HashMap<>();
        for (Map.Entry<Long, FactoryInfoParam> entry : factoryInfoGroupByFactoryId.entrySet()) {

            //构造不同工厂的dateList
            FactoryInfoParam factoryInfoParam = entry.getValue();
            List<FactoryVacation> thisFactoryVacationList = factoryVacationMapper.listVacationByFactoryIdAndDateList(factoryInfoParam.getId(),dateList);

//            List<FactoryVacation> thisFactoryVacationList = this.list(Wrappers.<FactoryVacation>lambdaQuery()
//                    .in(FactoryVacation::getVacationDate, dateList))
//                    eq(FactoryVacation::getFactoryId, factoryInfoParam.getId());
            //.and(item->item.in(FactoryVacation::getVacationDate, dateList)));


            //查询之后取出为禁用状态的假期
            List<FactoryVacation> disableFactoryVacationList = thisFactoryVacationList.stream().filter(item -> item.getStatus().equals(MrpVacationStatusEnum.DISABLE.getCode())).collect(Collectors.toList());
            //遍历
            for (FactoryVacation factoryVacation : disableFactoryVacationList) {
                if( StrUtil.equals(factoryVacation.getSourceType(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode())  ||  StrUtil.equals(factoryVacation.getSourceType(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode()) ){
                    flag=true;
                    break;
                }
            }
            //跳出最外层循环
            if(flag){
                break;
            }
        }
        return flag;
    }

    @Override
    public FactoryVacationImportVo importVacationConflictMessage(MultipartFile file,String workBench) {
        //true 前端直接保存   false  前端提示之后才进行保存
        boolean flag = true;
        ImportFactoryVacationListener listener = new ImportFactoryVacationListener();
        try {
            EasyExcel.read(file.getInputStream(), listener)
                    .head(ImportFactoryVacationVo.class)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            log.error("error", e);
        }
        List<ImportFactoryVacationVo> returnList = listener.getReturnList();

        //数据准备
        List<FactoryVacation> factoryVacationForSave = new ArrayList<>();
        List<FactoryVacation> factoryVacationForUpdate = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(returnList)) {
            List<String> numberList = returnList.stream().map(item -> item.getNumber()).collect(Collectors.toList());
            //传入的数据按照工厂编号划分
            Map<String, List<ImportFactoryVacationVo>> returnListGroupByFactoryNumber = returnList.stream().collect(Collectors.groupingBy(ImportFactoryVacationVo::getNumber));

            List<FactoryInfo> list = factoryInfoService.list(Wrappers
                    .<FactoryInfo>lambdaQuery()
                    .eq(FactoryInfo::getBelongType,workBench)
                    .in(FactoryInfo::getNumber, numberList));

            if(CollectionUtil.isEmpty(list)){
                throw new RuntimeException("导入失败,不存在编码为" +numberList.toString() + "的工厂 导入了不存在的工厂编码的节假日信息");
            }

            //循环工厂 检验有没有冲突  有冲突并且开启(直接抛出异常)  有冲突但是全部关闭 构建保存和更新的list并返回结果
            for (FactoryInfo factoryInfo : list) {
                if (!numberList.contains(factoryInfo.getNumber())) {
                    throw new RuntimeException("不存在编码为" + factoryInfo.getNumber() + "的工厂 导入了不存在的工厂编码的节假日信息");
                }
            }

            for (FactoryInfo factoryInfo : list) {
                //取出该工厂需要手动新增的节假日的list
                List<ImportFactoryVacationVo> importFactoryVacationVos = returnListGroupByFactoryNumber.get(factoryInfo.getNumber());
                //把取出的list的所有日期取出来
                List<Date> dateList = importFactoryVacationVos.stream().map(item -> item.getVacationDate()).collect(Collectors.toList());
                //同时以date为key 创建map 方便下面使用
                Map<Date, ImportFactoryVacationVo> thisRunDateMap = importFactoryVacationVos.stream().collect(Collectors.toMap(ImportFactoryVacationVo::getVacationDate, item -> item));

                //根据日期list和工厂id查询可能会有冲突的假日数据
                List<FactoryVacation> factoryVacationListForthisFactoryList = factoryVacationMapper
                        .selectList(Wrappers
                                .<FactoryVacation>lambdaQuery()
                                .eq(FactoryVacation::getFactoryId, factoryInfo.getId())
                                .in(FactoryVacation::getVacationDate, dateList));
                //转换成map 方便在下面的循环中获取
                Map<Date, FactoryVacation> factoryVacationListForthisFactoryMap = factoryVacationListForthisFactoryList.stream().collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));

                //
                for (ImportFactoryVacationVo importFactoryVacationVo : importFactoryVacationVos) {
                    FactoryVacation factoryVacationInMap = factoryVacationListForthisFactoryMap.get(importFactoryVacationVo.getVacationDate());
                    ImportFactoryVacationVo importFactoryVacationVoForThisRun = thisRunDateMap.get(importFactoryVacationVo.getVacationDate());
                    //如果不为空 有冲突  处理
                    if (ObjectUtil.isNotEmpty(factoryVacationInMap)) {
                        Integer status = factoryVacationInMap.getStatus();
                        //status如果为开启 并且是法定节假日或默认非工作日  抛出异常
                        if (status.equals(MrpVacationStatusEnum.ENABLE.getCode())) {
                            if (StrUtil.equals(factoryVacationInMap.getSourceType(), MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode()) || StrUtil.equals(factoryVacationInMap.getSourceType(), MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode())) {
                                throw new ApiException(500, "导入的节假日" + factoryVacationInMap.getFactoryNumber() + " " + factoryVacationInMap.getVacationDate() + "与已有的法定节假日或非默认工作日发生冲突");
                            }

                        }

                        //为关闭  更新
                        flag = false;
                        factoryVacationInMap.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                        factoryVacationInMap.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                        factoryVacationInMap.setRemark(importFactoryVacationVoForThisRun.getRemark());

                        factoryVacationInMap.setVacationType( importFactoryVacationVoForThisRun.getVacationType());
                        factoryVacationForUpdate.add(factoryVacationInMap);
                    }
                    //为空 没有冲突 进行新增
                    else {
                        Date vacationDate = importFactoryVacationVo.getVacationDate();

                        FactoryVacation factoryVacation = new FactoryVacation();
                        factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                        factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                        factoryVacation.setFactoryName(factoryInfo.getName());
                        factoryVacation.setFactoryId(factoryInfo.getId());
                        factoryVacation.setBelongType(factoryInfo.getBelongType());
                        factoryVacation.setBelongCode(factoryInfo.getBelongCode());
                        factoryVacation.setBelongName(factoryInfo.getBelongName());
                        factoryVacation.setFactoryNumber(factoryInfo.getNumber());
                        //factoryVacation.setVacationType(MrpVacationTypeEnum.HOLIDAY.getCode());
                        factoryVacation.setVacationType(importFactoryVacationVoForThisRun.getVacationType());
                        factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(BuildDayUtil.dateToLocalDate(vacationDate)));
                        factoryVacation.setVacationDate(vacationDate);
                        factoryVacation.setRemark(importFactoryVacationVoForThisRun.getRemark());
                        factoryVacationForSave.add(factoryVacation);
                    }
                }
            }
        }
        FactoryVacationImportVo factoryVacationImportVo = new FactoryVacationImportVo();
        factoryVacationImportVo.setFlag(flag);
        factoryVacationImportVo.setListForSave(factoryVacationForSave);
        factoryVacationImportVo.setListForUpdate(factoryVacationForUpdate);
        return factoryVacationImportVo;
    }

    @Override
    public FactoryVacationImportVo importSupplierVacationConflictMessage(MultipartFile file, String workBench) {
        //true 前端直接保存   false  前端提示之后才进行保存
        boolean flag = true;
        ImportSupplierFactoryVacationListener listener = new ImportSupplierFactoryVacationListener();
        try {
            EasyExcel.read(file.getInputStream(), listener)
                    .head(ImportSupplierFactoryVacationVo.class)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            log.error("error", e);
        }
        List<ImportSupplierFactoryVacationVo> returnList = listener.getReturnList();

        //数据准备
        List<FactoryVacation> factoryVacationForSave = new ArrayList<>();
        List<FactoryVacation> factoryVacationForUpdate = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(returnList)) {
            List<String> numberList = returnList.stream().map(item -> item.getNumber()).collect(Collectors.toList());
            //传入的数据按照工厂编号划分
            Map<String, List<ImportSupplierFactoryVacationVo>> returnListGroupByFactoryNumber = returnList.stream().collect(Collectors.groupingBy(ImportSupplierFactoryVacationVo::getNumber));

            List<FactoryInfo> list = factoryInfoService.list(Wrappers
                    .<FactoryInfo>lambdaQuery()
                    .eq(FactoryInfo::getBelongType,workBench)
                    .in(FactoryInfo::getNumber, numberList));

            if(CollectionUtil.isEmpty(list)){
                throw new RuntimeException("导入失败,不存在编码为" +numberList.toString() + "的工厂 导入了不存在的工厂编码的节假日信息");
            }

            //循环工厂 检验有没有冲突  有冲突并且开启(直接抛出异常)  有冲突但是全部关闭 构建保存和更新的list并返回结果
            for (FactoryInfo factoryInfo : list) {
                if (!numberList.contains(factoryInfo.getNumber())) {
                    throw new RuntimeException("不存在编码为" + factoryInfo.getNumber() + "的工厂 导入了不存在的工厂编码的节假日信息");
                }
            }

            for (FactoryInfo factoryInfo : list) {
                //取出该工厂需要手动新增的节假日的list
                List<ImportSupplierFactoryVacationVo> importFactoryVacationVos = returnListGroupByFactoryNumber.get(factoryInfo.getNumber());
                //把取出的list的所有日期取出来
                List<Date> dateList = importFactoryVacationVos.stream().map(item -> item.getVacationDate()).collect(Collectors.toList());
                //同时以date为key 创建map 方便下面使用
                Map<Date, ImportSupplierFactoryVacationVo> thisRunDateMap = importFactoryVacationVos.stream().collect(Collectors.toMap(ImportSupplierFactoryVacationVo::getVacationDate, item -> item));

                //根据日期list和工厂id查询可能会有冲突的假日数据
                List<FactoryVacation> factoryVacationListForthisFactoryList = factoryVacationMapper
                        .selectList(Wrappers
                                .<FactoryVacation>lambdaQuery()
                                .eq(FactoryVacation::getFactoryId, factoryInfo.getId())
                                .in(FactoryVacation::getVacationDate, dateList));
                //转换成map 方便在下面的循环中获取
                Map<Date, FactoryVacation> factoryVacationListForthisFactoryMap = factoryVacationListForthisFactoryList.stream().collect(Collectors.toMap(FactoryVacation::getVacationDate, item -> item));

                //
                for (ImportSupplierFactoryVacationVo importFactoryVacationVo : importFactoryVacationVos) {
                    FactoryVacation factoryVacationInMap = factoryVacationListForthisFactoryMap.get(importFactoryVacationVo.getVacationDate());
                    ImportSupplierFactoryVacationVo importFactoryVacationVoForThisRun = thisRunDateMap.get(importFactoryVacationVo.getVacationDate());
                    //如果不为空 有冲突  处理
                    if (ObjectUtil.isNotEmpty(factoryVacationInMap)) {
                        Integer status = factoryVacationInMap.getStatus();
                        //status如果为开启 并且是法定节假日或默认非工作日  抛出异常
                        if (status.equals(MrpVacationStatusEnum.ENABLE.getCode())) {
                            if (StrUtil.equals(factoryVacationInMap.getSourceType(), MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode()) || StrUtil.equals(factoryVacationInMap.getSourceType(), MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode())) {
                                throw new ApiException(500, "导入的节假日" + factoryVacationInMap.getFactoryNumber() + " " + factoryVacationInMap.getVacationDate() + "与已有的法定节假日或非默认工作日发生冲突");
                            }

                        }

                        //为关闭  更新
                        flag = false;
                        factoryVacationInMap.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                        factoryVacationInMap.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                        factoryVacationInMap.setRemark(importFactoryVacationVoForThisRun.getRemark());

                        factoryVacationInMap.setVacationType( importFactoryVacationVoForThisRun.getVacationType());
                        factoryVacationForUpdate.add(factoryVacationInMap);
                    }
                    //为空 没有冲突 进行新增
                    else {
                        Date vacationDate = importFactoryVacationVo.getVacationDate();

                        FactoryVacation factoryVacation = new FactoryVacation();
                        factoryVacation.setSourceType(MrpVacationSourceTypeEnum.ADD_MANUALLY.getCode());
                        factoryVacation.setStatus(MrpVacationStatusEnum.ENABLE.getCode());
                        factoryVacation.setFactoryName(factoryInfo.getName());
                        factoryVacation.setFactoryId(factoryInfo.getId());
                        factoryVacation.setBelongType(factoryInfo.getBelongType());
                        factoryVacation.setBelongCode(factoryInfo.getBelongCode());
                        factoryVacation.setBelongName(factoryInfo.getBelongName());
                        factoryVacation.setFactoryNumber(factoryInfo.getNumber());
                        //factoryVacation.setVacationType(MrpVacationTypeEnum.HOLIDAY.getCode());
                        factoryVacation.setVacationType(importFactoryVacationVoForThisRun.getVacationType());
                        factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(BuildDayUtil.dateToLocalDate(vacationDate)));
                        factoryVacation.setVacationDate(vacationDate);
                        factoryVacation.setRemark(importFactoryVacationVoForThisRun.getRemark());
                        factoryVacationForSave.add(factoryVacation);
                    }
                }
            }
        }
        FactoryVacationImportVo factoryVacationImportVo = new FactoryVacationImportVo();
        factoryVacationImportVo.setFlag(flag);
        factoryVacationImportVo.setListForSave(factoryVacationForSave);
        factoryVacationImportVo.setListForUpdate(factoryVacationForUpdate);
        return factoryVacationImportVo;
    }

    @Override
    public List<FactoryVacation> listByBelongCodeCodeColl(Collection<String> belongCodeColl, FactoryBelongTypeEnum factoryBelongTypeEnum) {

        return this.list(
                Wrappers.<FactoryVacation>lambdaQuery()
                        .eq(FactoryVacation::getBelongType, factoryBelongTypeEnum.getCode())
                        .in(FactoryVacation::getBelongCode, belongCodeColl)
                        .ge( FactoryVacation::getVacationDate,new Date())
        );

    }


}
