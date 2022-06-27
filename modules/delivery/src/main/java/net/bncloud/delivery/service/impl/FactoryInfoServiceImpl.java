package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.baidu.api.BaiduMapFeignClient;
import net.bncloud.baidu.model.dto.BaiduMapGeoCodingDto;
import net.bncloud.baidu.model.vo.BaiduMapGeoCodingResult;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.repeatrequest.RepeatRequestOperation;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.*;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.mapper.FactoryInfoMapper;
import net.bncloud.delivery.mapper.FactoryVacationMapper;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.HolidayAndSubscribeConfigParam;
import net.bncloud.delivery.param.OverAllAdjustParam;
import net.bncloud.delivery.param.OverallsituationConfigChangeParam;
import net.bncloud.delivery.service.*;
import net.bncloud.delivery.utils.BuildDayUtil;
import net.bncloud.delivery.vo.FactoryInfoVo;
import net.bncloud.delivery.vo.SupplierDeliveryConfigVo;
import net.bncloud.delivery.wrapper.FactoryInfoWrapper;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.support.Condition;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
@Slf4j
@Service
@SuppressWarnings({"all"})
public class FactoryInfoServiceImpl extends BaseServiceImpl<FactoryInfoMapper, FactoryInfo> implements FactoryInfoService {
    @Resource
    private FactoryInfoMapper factoryInfoMapper;
    @Resource
    private FactoryTransportationDurationService transportationDurationService;
    @Resource
    private BaiduMapFeignClient baiduMapFeignClient;
    @Resource
    private HolidayService holidayService;
    @Resource
    private HolidayDateService holidayDateService;
    @Resource
    @Lazy
    private FactoryVacationService factoryVacationService;
    @Resource
    private FactoryVacationMapper factoryVacationMapper;
    @Resource
    @Lazy
    private NewFactoryWorkdayService factoryWorkdayService;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;
    @Resource
    @Lazy
    private SupplierDeliveryConfigService supplierDeliveryConfigService;

    /**
     * 分页列表
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<FactoryInfoVo> getPageList(IPage<FactoryInfoParam> page, QueryParam<FactoryInfoParam> queryParam) {
        IPage<FactoryInfoVo> factoryInfoVoIPage = factoryInfoMapper.selectPlantListPage(page, queryParam);
        factoryInfoVoIPage.getRecords().forEach(item->{
            item.getAreas().add(item.getProvince());
            item.getAreas().add(item.getCity());
            item.getAreas().add(item.getDistrict());
            item.getAreas().add(item.getStreet());
        });
        return PageUtils.result(factoryInfoVoIPage);
    }

    /**
     * 删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public void batchDelete(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            factoryInfoMapper.deleteAllByIds(ids);
            //deleteLogic(ids);
            ids.forEach(id -> {
                transportationDurationService.remove(Wrappers.<FactoryTransportationDuration>lambdaQuery()
                        .and(queryWrapper -> queryWrapper
                                .eq(FactoryTransportationDuration::getDeliveryFactoryId, id)
                                .or()
                                .eq(FactoryTransportationDuration::getReceiptFactoryId, id)));
            });
        }
        //同时要删除该工厂的默认工作日和假期的数据
        factoryVacationService.deleteAllVacationFromFactoryId(ids);
        factoryWorkdayService.deleteAllWorkDayFromFactoryId(ids);
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> function) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
    }
    /**
     * 编辑和保存
     *
     * @param factoryInfos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public void batchUpdate(List<FactoryInfo> factoryInfos) {
        if (CollectionUtil.isNotEmpty(factoryInfos)) {

            //校验；一个供应商只会对应一个工厂
            long count = factoryInfos.stream().map(FactoryInfo::getBelongCode).count();
            long disCount = factoryInfos.stream().map(FactoryInfo::getBelongCode).distinct().count();
            if (disCount != count) {
                log.info("一个供应商只能对应一个工厂");
                throw new RuntimeException("一个供应商只能对应一个工厂");
            }
            factoryInfos.forEach(factoryInfo -> {
                //修改工厂
                if (ObjectUtil.isNotEmpty(factoryInfo.getId())) {
                    FactoryInfo factoryInfo1 = getById(factoryInfo.getId());
                    //修改了belongCode
                    if (!factoryInfo1.getBelongCode().equals(factoryInfo.getBelongCode())) {
                        FactoryInfo info = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongCode, factoryInfo.getBelongCode())
                                .eq(FactoryInfo::getBelongType, FactoryBelongTypeEnum.SUPPLIER.getCode()));
                        if (ObjectUtil.isNotEmpty(info)) {
                            log.info("一个供应商只能对应一个工厂");
                            throw new RuntimeException("一个供应商只能对应一个工厂");
                        }
                    }

                }else {//新增工厂
                    FactoryInfo info = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongCode, factoryInfo.getBelongCode())
                            .eq(FactoryInfo::getBelongType, FactoryBelongTypeEnum.SUPPLIER.getCode()));
                    if (ObjectUtil.isNotEmpty(info)) {
                        log.info("一个供应商只能对应一个工厂");
                        throw new RuntimeException("一个供应商只能对应一个工厂");
                    }
                }
            });

            //获取所有供应商信息
            R<List<SuppliersDTO>> allSupplierR = supplierFeignClient.getSupplierInfoAll();
            if (!allSupplierR.isSuccess() && allSupplierR.getData()==null){
                throw new RuntimeException("获取供应商信息失败");
            }
            Map<String, String> map = allSupplierR.getData().stream().filter(CollectionUtil.distinctByKey(SuppliersDTO::getCode))
                    .collect(Collectors.toMap(SuppliersDTO::getCode, suppliersDTO -> StrUtil.isNotBlank( suppliersDTO.getName() ) ? suppliersDTO.getName() : suppliersDTO.getCode() ));
            //根据供应商编码设置供应商名称
            factoryInfos.forEach(factoryInfo -> {
                if ( FactoryBelongTypeEnum.SUPPLIER.getCode().equals(factoryInfo.getBelongType())) {
                    factoryInfo.setBelongName(map.get(factoryInfo.getBelongCode()));
                }
            });

            //联动修改工厂的默认工作日和假期的信息  数据准备
            List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
            List<LocalDate> localDates = new ArrayList<>();
            List<LocalDate> localDatesFor996 = new ArrayList<>();
            List<LocalDate> localDateForDefault=new ArrayList<>();
            String belongType = factoryInfos.get(0).getBelongType();
            //供应商默认1-7(供应商现在原型是没有默认全局的工作日配置的)
            Integer startNum=1;
            Integer endNum=7;

            List<String> supplierCodeList = factoryInfos.stream().map(item -> item.getBelongCode()).collect(Collectors.toList());

            //获取供应方配置
            List<SupplierDeliveryConfig> supplierConfigList = supplierDeliveryConfigService
                    .list(Wrappers
                            .<SupplierDeliveryConfig>lambdaQuery()
                            .in(SupplierDeliveryConfig::getSupplierCode, supplierCodeList));

            Map<String, SupplierDeliveryConfig> supplierConfigMap = supplierConfigList.stream().collect(Collectors.toMap(SupplierDeliveryConfig::getCode, item -> item));


            //获取远程的默认工作日配置 订阅法定节假日开关  虽然这里是获取采购方的,但是法定节假日数据可以共用
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();
            localDates = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            localDatesFor996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());

            //循环遍历对每一个工厂进行操作
            for (FactoryInfo factoryInfo : factoryInfos) {

                //设置经纬度
                //省市区街道
                StringBuffer buffer = new StringBuffer(factoryInfo.getProvince()).append(factoryInfo.getCity()).append(factoryInfo.getDistrict()).append(factoryInfo.getStreet());
                //详细地址
                String address = buffer.append(factoryInfo.getDetailedAddress()).toString();
                BaiduMapGeoCodingResult baiduMapGeoCodingResult = baiduMapFeignClient.getGeoCoding(BaiduMapGeoCodingDto
                        .builder().city(buffer.toString()).address(address.trim()).build());
                if (baiduMapGeoCodingResult.getResult()!=null){
                    double lng = baiduMapGeoCodingResult.getResult().getLocation().getLng();
                    double lat = baiduMapGeoCodingResult.getResult().getLocation().getLat();
                    StringBuffer stringBuffer = new StringBuffer().append("(" + lng + ",").append(lat + ")");
                    factoryInfo.setAddressLongitudeLatitude(stringBuffer.toString());
                }

                if (factoryInfo.getId()==null){
                    FactoryInfo factoryInfoByNumber = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getNumber, factoryInfo.getNumber()));
                    FactoryInfo factoryInfoByName = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getName, factoryInfo.getName()));
                    if (!(factoryInfoByNumber == null && factoryInfoByName == null)) {
                        throw new ApiException(ResultCode.FAILURE.getCode(), "重复的工厂编码或者是工厂名称！");
                    }
                    save(factoryInfo);
                }else {
                    FactoryInfo plant = getById(factoryInfo.getId());
                    if (!(plant.getNumber().equals(factoryInfo.getNumber()) && plant.getName().equals(factoryInfo.getName()))) {
                        FactoryInfo factoryInfoByNumber = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getNumber, factoryInfo.getNumber()).ne(FactoryInfo::getId,factoryInfo.getId()));
                        FactoryInfo factoryInfoByName = getOne(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getName, factoryInfo.getName()).ne(FactoryInfo::getId,factoryInfo.getId()));
                        if (!(factoryInfoByNumber == null && factoryInfoByName == null)) {
                            throw new ApiException(ResultCode.FAILURE.getCode(), "重复的工厂编码或者是工厂名称！");
                        }
                    }
                    updateById(factoryInfo);
                }

                //创建该工厂假期
                //供应商默认就是1-7,所以不需要创建1年的默认节假日了 但是要注意是否订阅了法定节假日
                SupplierDeliveryConfig supplierDeliveryConfig = supplierConfigMap.get(factoryInfo.getBelongCode());
                //判断当前的供应方是否订阅了法定节假日  包含
                if(ObjectUtil.isNotEmpty(supplierDeliveryConfig)){
                    Boolean config = Boolean.valueOf(supplierDeliveryConfig.getValue());
                    //开关打开
                    if(config){
                        //如果有值
                        if( CollectionUtil.isNotEmpty(localDates)){
                            factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),belongType,factoryInfo,MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode()));
                        }
                    }
                }
                //不包含  就是没有开启也没有初始化,默认没有开启,不用处理
            }

            if(CollectionUtil.isNotEmpty(factoryVacationArrayList)){
                factoryVacationService.saveBatch(factoryVacationArrayList);
            }

        }
    }

    private List<FactoryVacation> buildEntity( List<LocalDate> localDates, Integer status, String sourceType,String belongType,FactoryInfo factoryInfo,String vacationTypeCode) {
        List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
        for (LocalDate localDate : localDates) {

            FactoryVacation factoryVacation = new FactoryVacation();
            factoryVacation.setStatus(status);
            factoryVacation.setSourceType(sourceType);
            factoryVacation.setBelongType(belongType);
            factoryVacation.setFactoryId(factoryInfo.getId());
            factoryVacation.setFactoryName(factoryInfo.getName());
            factoryVacation.setFactoryNumber(factoryInfo.getNumber());
            factoryVacation.setBelongCode(factoryInfo.getBelongCode());
            factoryVacation.setBelongName(factoryInfo.getBelongName());
            factoryVacation.setVacationDate(BuildDayUtil.localdateToDate(localDate));
            factoryVacation.setVacationType(vacationTypeCode);
            factoryVacation.setDayInWeekNum(BuildDayUtil.weekOfDay(localDate));

            factoryVacationArrayList.add(factoryVacation);
        }
        return factoryVacationArrayList;
    }

    @Override
    public List<FactoryInfoVo> selectForCreate(String code) {
        List<FactoryInfo> list = this.list(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongCode, code));
        List<FactoryWorkday> listWorkday = factoryWorkdayService.list(Wrappers.<FactoryWorkday>lambdaQuery().eq(FactoryWorkday::getBelongCode, code));
        List<Long> factoryIdForfactoryWorkday = listWorkday.stream().map(FactoryWorkday::getFactoryId).collect(Collectors.toList());
        List<FactoryInfo> collectForReturn = list.stream().filter(item -> !factoryIdForfactoryWorkday.contains(item.getId())).collect(Collectors.toList());
        List<FactoryInfoVo> factoryInfoVos = FactoryInfoWrapper.build().listVO(collectForReturn);
        return factoryInfoVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void overallsituationConfigChange(OverallsituationConfigChangeParam param) {
        List<FactoryInfo> factoryInfoList = this.list(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongType, WorkBench.PURCHASE.getCode()));

        List<FactoryWorkday> list = factoryWorkdayService.list(Condition.getQueryWrapper(new FactoryWorkday())
                .lambda()
                .eq(FactoryWorkday::getBelongType, WorkBench.PURCHASE.getCode()));
        List<Long> workdayIds = list.stream().map(FactoryWorkday::getFactoryId).collect(Collectors.toList());

        List<FactoryInfo> defaultFactoryInfoList = factoryInfoList.stream().filter(factoryInfo -> !workdayIds.contains(factoryInfo)).collect(Collectors.toList());

        //删除这些使用全局默认节假日的工厂的默认非工作日的假期
        List<Long> defaultIds = defaultFactoryInfoList.stream().map(FactoryInfo::getId).collect(Collectors.toList());
        factoryVacationMapper.deleteInFactoryId(defaultIds);

        Integer startNum = Integer.valueOf(param.getWeekStartNum());
        Integer endNum = Integer.valueOf(param.getWeekEndNum());

        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(startNum, endNum);

        List<LocalDate> localDateForDefault=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(integerList)){
            //有休息日 创建默认休息日
            localDateForDefault = BuildDayUtil.calHolidayForYear(integerList, BuildDayUtil.day());
        }

        List<FactoryWorkday> factoryWorkdayList = new ArrayList<>();
        for (FactoryInfo factoryInfo : defaultFactoryInfoList) {
            FactoryWorkday factoryWorkday = new FactoryWorkday();
            factoryWorkday.setWeekEndNum(endNum);
            factoryWorkday.setWeekStartNum(startNum);
            factoryWorkday.setFactoryId(factoryInfo.getId());
            factoryWorkday.setFactoryName(factoryInfo.getName());
            factoryWorkday.setBelongCode(factoryInfo.getBelongCode());
            factoryWorkday.setBelongType(factoryInfo.getBelongType());
            factoryWorkday.setBelongName(factoryInfo.getBelongName());
            factoryWorkdayList.add(factoryWorkday);

        }

        factoryWorkdayService.holidayAdjustment(factoryWorkdayList);

        //factoryVacationService.deleteAllVacationFromFactoryId(defaultIds);

        //重新构建使用全局默认节假日的工厂的假期
        /*Integer startNum = Integer.valueOf(param.getWeekStartNum());
        Integer endNum = Integer.valueOf(param.getWeekEndNum());

        List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
        List<LocalDate> localDates = new ArrayList<>();
        List<LocalDate> localDatesFor996 = new ArrayList<>();
        List<LocalDate> localDateForDefault=new ArrayList<>();

        //获取远程的默认工作日配置 订阅法定节假日开关
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig();
        JSONObject jsonObjectForAuto = allHolidayAndSubscribeConfig.getJsonObjectForAuto();
        if(holidayService.IsAutoSubscribe(jsonObjectForAuto, param.getCode())) {
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();
            localDates = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            localDatesFor996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());
        }

        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(startNum, endNum);

        if(CollectionUtil.isNotEmpty(integerList)){
            //有休息日 创建默认休息日
            localDateForDefault = BuildDayUtil.calHolidayForYear(integerList, BuildDayUtil.day());
        }

        //循环工厂,开始构建
        for (FactoryInfo factoryInfo : defaultFactoryInfoList) {

            if (!localDates.isEmpty() || !localDatesFor996.isEmpty()) {
                localDateForDefault = holidayService.cover(localDates, localDateForDefault);
                localDateForDefault = holidayService.cover(localDates, localDateForDefault);

                factoryVacationArrayList.addAll(buildEntity(localDates, MrpVacationStatusEnum.ENABLE.getCode(), MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(), WorkBench.PURCHASE.getCode(), factoryInfo));
                factoryVacationArrayList.addAll(buildEntity(localDatesFor996, MrpVacationStatusEnum.IS_996.getCode(), MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(), WorkBench.PURCHASE.getCode(), factoryInfo));

            }

            if (!localDateForDefault.isEmpty()) {
                factoryVacationArrayList.addAll(buildEntity(localDates, MrpVacationStatusEnum.ENABLE.getCode(), MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode(), WorkBench.PURCHASE.getCode(), factoryInfo));
            }

        }*/

        //构建完毕 保存
        //factoryVacationService.saveBatch(factoryVacationArrayList);
    }

    @Override
    public void syncPurchaseInfoToFactoryInfo() {

        // 查询采购方信息到工厂
        R<List<PurchaserVo>> allPurchaserR = purchaserFeignClient.getAllPurchaserByOrgId(112L);
        Asserts.isTrue( allPurchaserR.isSuccess(), allPurchaserR.getMsg() );
        Asserts.notNull( allPurchaserR.getData(), allPurchaserR.getMsg() );

        //假期数据准备
        List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
        HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());

        List<PurchaserVo> allPurchaserList = allPurchaserR.getData();

        log.info( "[syncPurchaseInfoToFactoryInfo] allPurchaserList :{}", JSON.toJSONString( allPurchaserList ));

        //先删除采购方的工厂数据
        List<FactoryInfo> factoryInfos = this.list(Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongType, WorkBench.PURCHASE.getCode()));
        if(CollectionUtil.isNotEmpty(factoryInfos)){
            List<Long> factoryInfoIds = factoryInfos.stream().map(item -> item.getId()).collect(Collectors.toList());
            this.batchDelete(factoryInfoIds);
        }

        allPurchaserList.forEach( purchaserVo -> {

            FactoryInfo factoryInfoWillInsert = new FactoryInfo()
                    .setNumber(purchaserVo.getCode())
                    .setName(purchaserVo.getName())
                    .setBelongType(FactoryBelongTypeEnum.PURCHASE.getCode())
                    .setBelongCode(purchaserVo.getCode())
                    .setBelongName(purchaserVo.getName())
                    .setType(FactoryTypeEnum.receipt.name());

            this.save( factoryInfoWillInsert );

            //构造采购工厂假期
            List<FactoryVacation> factoryVacationList = buildHoliday(factoryInfoWillInsert,allHolidayAndSubscribeConfig);
            factoryVacationArrayList.addAll(factoryVacationList);
        } );

        if(CollectionUtil.isNotEmpty(factoryVacationArrayList)){
            factoryVacationService.saveBatch(factoryVacationArrayList);
        }

    }

    /**
     * 采购方调用 构建主数据工厂的假日
     * */
    private List<FactoryVacation> buildHoliday(FactoryInfo factoryInfo,HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig) {
        //假期数据准备
        List<FactoryVacation> factoryVacationArrayList = new ArrayList<FactoryVacation>();
        //构造采购工厂假期
        //联动修改工厂的默认工作日和假期的信息  数据准备
        List<LocalDate> localDates = new ArrayList<>();
        List<LocalDate> localDatesFor996 = new ArrayList<>();
        List<LocalDate> localDateForDefault=new ArrayList<>();

        //获取配置 1 全局默认工作日配置 生成默认非工作日假期  2获取法定节假日配置
        //HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());
        Boolean jsonObjectForAuto = allHolidayAndSubscribeConfig.getJsonObjectForAuto();
        Map<String, Integer> defaultWorkdayConfigMap = allHolidayAndSubscribeConfig.getDefaultWorkdayConfigMap();
        //获取工作日开始和结束时间
        Integer startNum = defaultWorkdayConfigMap.get("start");
        Integer endNum = defaultWorkdayConfigMap.get("end");
        //构建休息日
        List<Integer> integerList = BuildDayUtil.returnHolidayForWeekArray(startNum, endNum);
        if(CollectionUtil.isNotEmpty(integerList)){
            //有休息日 创建默认休息日
            localDateForDefault = BuildDayUtil.calHolidayForYear(integerList, BuildDayUtil.day());
        }
        //如果全局配置-订阅法定节假日打开了 那就取出法定节假日数据
        if(allHolidayAndSubscribeConfig.getJsonObjectForAuto()){
            Map<String, List<LocalDate>> holidayDateMap = holidayDateService.selectAllHoliday();
            localDates = holidayDateMap.get(HolidayDateEnum.HOLIDAY_DAY.getCode());
            localDatesFor996 = holidayDateMap.get(HolidayDateEnum.IS_996.getCode());
        }

        //开始保存
        if( CollectionUtil.isNotEmpty(localDates)||CollectionUtil.isNotEmpty(localDatesFor996)){
            //冲突处理
            localDateForDefault = holidayService.cover(localDates, localDateForDefault);
            localDateForDefault = holidayService.cover(localDatesFor996, localDateForDefault);

            factoryVacationArrayList.addAll(buildEntity(localDates,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.AUTO_SUBSCRIBE.getCode(),WorkBench.PURCHASE.getCode(),factoryInfo,MrpVacationTypeEnum.AUTO_SUBSCRIBE.getCode()));
        }
        factoryVacationArrayList.addAll(buildEntity(localDateForDefault,MrpVacationStatusEnum.ENABLE.getCode(),MrpVacationSourceTypeEnum.DEFAULT_HOLIDAY.getCode(),WorkBench.PURCHASE.getCode(),factoryInfo,MrpVacationTypeEnum.DEFAULT_HOLIDAY.getCode()));
        return factoryVacationArrayList;
    }




    @Override
    public void overAllAdjustment(OverAllAdjustParam param) {
        List<FactoryInfo> factoryList = this.list(Wrappers
                .<FactoryInfo>lambdaQuery()
                .eq(FactoryInfo::getBelongType, WorkBench.PURCHASE.getCode()));

        List<FactoryWorkday> workdayList = factoryWorkdayService.list(Wrappers
                .<FactoryWorkday>lambdaQuery()
                .eq(FactoryWorkday::getBelongType, WorkBench.PURCHASE.getCode()));
        List<Long> ids = workdayList.stream().map(item -> item.getFactoryId()).collect(Collectors.toList());

        List<FactoryInfo> factoryInfoHaveNoWorkDay = factoryList.stream().filter(factory -> !ids.contains(factory.getId())).collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(factoryInfoHaveNoWorkDay)){
            //先删除
            List<Long> factoryInfoIdsHaveNoWorkDay = factoryInfoHaveNoWorkDay.stream().map(item -> item.getId()).collect(Collectors.toList());
            factoryVacationService.deleteAllVacationFromFactoryId(factoryInfoIdsHaveNoWorkDay);
            //保存数据准备
            List<FactoryVacation> factoryVacationForSaveList = new ArrayList<>();
            //先获取配置 避免下面每一次循环都要获取
            HolidayAndSubscribeConfigParam allHolidayAndSubscribeConfig = holidayService.getAllHolidayAndSubscribeConfig(WorkBench.PURCHASE.getCode());

            //循环构造所有采购工厂的假期
            for (FactoryInfo factoryInfo : factoryInfoHaveNoWorkDay) {
                List<FactoryVacation> factoryVacationList = buildHoliday(factoryInfo,allHolidayAndSubscribeConfig);
                factoryVacationForSaveList.addAll(factoryVacationList);
            }

            //批量保存
            if(CollectionUtil.isNotEmpty(factoryVacationForSaveList)){
                factoryVacationService.saveBatch(factoryVacationForSaveList);
            }

        }
    }

}
