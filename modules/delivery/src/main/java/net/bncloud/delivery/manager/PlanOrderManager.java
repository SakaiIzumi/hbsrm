package net.bncloud.delivery.manager;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.enums.DeliveryPlanSourceTypeEnum;
import net.bncloud.delivery.enums.DeliveryPlanStatus;
import net.bncloud.delivery.enums.DetailStatusEnum;
import net.bncloud.delivery.enums.FactoryBelongTypeEnum;
import net.bncloud.delivery.service.*;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.PreVersionDeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.dto.DemandDateDto;
import net.bncloud.service.api.delivery.dto.PlanOrderDto;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * desc: 计划订单的处理
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@Slf4j
@Component
public class PlanOrderManager {

    @Resource
    private PlnPlanOrderService plnPlanOrderService;
    @Resource
    private FactoryTransportationDurationService factoryTransportationDurationService;
    @Autowired
    private DeliveryPlanService  deliveryPlanService;
    @Autowired
    private DeliveryPlanDetailService deliveryPlanDetailService;
    @Autowired
    private DeliveryPlanDetailItemService deliveryPlanDetailItemService;
    @Autowired
    private NumberFactory numberFactory;
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private FactoryVacationService factoryVacationService;
    @Autowired
    private DeliveryDetailService deliveryDetailService;

    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;
    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    /**
     * 处理 计划订单数据
     * @param planOrderDtoList
     */
    @Transactional(rollbackFor = Exception.class)
    public void handlePlanOrderData(List<PlanOrderDto> planOrderDtoList) {

        log.info("[handlePlanOrderData] 计划订单同步 >> planOrderDtoList:{}",JSON.toJSONString(planOrderDtoList ));

        // 查询当前最新的运算编号
        Optional<PlanOrderDto> planOrderDtoOpt = planOrderDtoList.stream().max(Comparator.comparing(PlanOrderDto::getFApproveDate));
        String mrpPlanOrderComputerNo = planOrderDtoOpt.orElseThrow(() -> new RuntimeException("不应该阿！")).getFComputerNo();

        // 仅保存最新一版的数据
        planOrderDtoList = planOrderDtoList.stream().filter( planOrderDto -> mrpPlanOrderComputerNo.equals( planOrderDto.getFComputerNo() ) ).collect(Collectors.toList());
        Asserts.isTrue( CollectionUtil.isNotEmpty( planOrderDtoList ),"要处理的计划订单数据为空！" );
        plnPlanOrderService.savePlanOrderData( planOrderDtoList);

        // 设置上版运算编号
        String mrpPlanOrderComputerNoOnMysql = dataConfigService.getMrpPlanOrderComputerNo();
        if( mrpPlanOrderComputerNo.equals( mrpPlanOrderComputerNoOnMysql ) ){
            log.warn("[计划订单数据] 同步数据的运算编号与数据库中最新的运算编号一致，数据无需处理！ planOrderDtoList:{}", JSON.toJSONString( planOrderDtoList ));
        }
        Asserts.isFalse( mrpPlanOrderComputerNo.equals( mrpPlanOrderComputerNoOnMysql ) ,"同步数据的运算编号与数据库中最新的运算编号一致，请勿重复处理！" );

        dataConfigService.updatePreMrpPlanOrderComputerNo( mrpPlanOrderComputerNoOnMysql );

        //上一版本的所有项次的送货数量 （key=采购方编码 + 供应商编码 + 产品编码 + 预计到货日期(字符串) + 建议发货日期(字符串)， value=送货数量）
        Map<String, BigDecimal> groupKeyDeliveryPlanDetailItemTotalDeliveryQuantityMap = deliveryPlanService.getPreVersionItemDeliveryQuantity( mrpPlanOrderComputerNoOnMysql);

        // 查询采购方节假日数据
        Set<String> purchaserCodeSet = planOrderDtoList.stream().map(PlanOrderDto::getFSupplyOrgId).collect( Collectors.toSet());
        Set<String> supplierCodeSet = planOrderDtoList.stream().map(PlanOrderDto::getFAbcTest).collect( Collectors.toSet());

        // 假期数据
        List<FactoryVacation> purchaserFactoryVacation = factoryVacationService.listByBelongCodeCodeColl(purchaserCodeSet, FactoryBelongTypeEnum.PURCHASE);
        List<FactoryVacation> supplierFactoryVacation = factoryVacationService.listByBelongCodeCodeColl(supplierCodeSet, FactoryBelongTypeEnum.SUPPLIER);

        Map<String, Set<String>> purchaserFactoryVacationDateStrSetMap = purchaserFactoryVacation.stream().collect(Collectors.groupingBy(FactoryVacation::getBelongCode, Collectors.mapping(factoryVacation -> DateUtil.format(factoryVacation.getVacationDate(), DatePattern.NORM_DATE_PATTERN), Collectors.toSet())));
        Map<String, Set<String>> supplierFactoryVacationDateStrSetMap = supplierFactoryVacation.stream().collect(Collectors.groupingBy(FactoryVacation::getBelongCode, Collectors.mapping(factoryVacation -> DateUtil.format(factoryVacation.getVacationDate(), DatePattern.NORM_DATE_PATTERN), Collectors.toSet())));

        // 以供应商+采购方 分组计划订单
        Map<String, List<PlanOrderDto>> groupKeyPlanOrderListMap = planOrderDtoList.stream()
                .map( PlanOrderDto::updatePlanFinishDateSecond)
                .sorted( Comparator.comparingLong( PlanOrderDto::getPlanFinishDateEpochMilli ) )
                .collect( Collectors.groupingBy(PlanOrderDto::buildGroupKey, LinkedHashMap::new,Collectors.toList()) );


        // 一个供应商+一个采购方一张送货计划
        for (Map.Entry<String, List<PlanOrderDto>> materialIdSupplierIdPlanOrderListEntry : groupKeyPlanOrderListMap.entrySet()) {
            // 送货计划排程
            PlanOrderDto erpPlanOrder = materialIdSupplierIdPlanOrderListEntry.getValue().get(0);
            String supplierCode = erpPlanOrder.getFAbcTest();
            String purchaserCode = erpPlanOrder.getFSupplyOrgId();

            DeliveryPlan deliveryPlan = this.initBuildDeliveryPlan(purchaserCode,supplierCode);
            deliveryPlan.setMrpComputerNo( mrpPlanOrderComputerNo);
            Asserts.isTrue( deliveryPlanService.save( deliveryPlan ) ,"保存送货计划失败！");

            // 运输时长（天）
            Integer transportationDuration = factoryTransportationDurationService.getTransportationDuration(purchaserCode, supplierCode);

            // 查询当供应商、采购方与物料的在途数量
            Set<String> materialCodeSet = planOrderDtoList.stream().map(PlanOrderDto::getFMaterialId).collect(Collectors.toSet());
            List<DeliveryDetailVo> deliveryDetailVoList = deliveryDetailService.queryInTransitQuantity(supplierCode, purchaserCode, materialCodeSet);
            // 物料 的 在途运输数量
            Map<String, BigDecimal> materialCodeTotalRealDeliveryQuantityMap = deliveryDetailVoList.stream().collect(Collectors.groupingBy(DeliveryDetailVo::getProductCode, Collectors.mapping(DeliveryDetailVo::getRealDeliveryQuantity, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

            // 存储所有项次数据
            List<DeliveryPlanDetailItem> currentDeliveryPlanDeliveryPlanDetailItemList = new ArrayList<>();

            List<PlanOrderDto> materialPlanOrderList = materialIdSupplierIdPlanOrderListEntry.getValue();
            // 一个物料一个明细
            Map<String, List<PlanOrderDto>> materialPlanOrderListMap = materialPlanOrderList.stream().collect(Collectors.groupingBy(PlanOrderDto::getFMaterialId));
            for (Map.Entry<String, List<PlanOrderDto>> materialPlanOrderListEntry : materialPlanOrderListMap.entrySet()) {

                String materialCode = materialPlanOrderListEntry.getKey();
                // 一个计划需求日期一个项次
                List<PlanOrderDto> planDemandList = materialPlanOrderListEntry.getValue();
                PlanOrderDto materialPlanOrderDto = planDemandList.get(0);

                DeliveryPlanDetail deliveryPlanDetail = new DeliveryPlanDetail();
                deliveryPlanDetail.setDeliveryPlanId( deliveryPlan.getId());
                deliveryPlanDetail.setProductCode( materialCode );
                deliveryPlanDetail.setProductName( materialPlanOrderDto.getFMaterialName() );
                deliveryPlanDetail.setSupplierCode( deliveryPlan.getSupplierCode() );
                deliveryPlanDetail.setSupplierName( deliveryPlan.getSupplierName() );
                //计划单位
                deliveryPlanDetail.setPlanUnit(materialPlanOrderDto.getFUnitId());
                //产品规格
                deliveryPlanDetail.setProductSpecification(materialPlanOrderDto.getFSpecification());
                //在途数量
                BigDecimal transitQuantity = Optional.ofNullable(materialCodeTotalRealDeliveryQuantityMap.get(materialPlanOrderDto.getFMaterialId())).orElse(BigDecimal.ZERO);
                deliveryPlanDetail.setTransitQuantity( transitQuantity.longValue() );

                deliveryPlanDetail.setDetailStatus(DetailStatusEnum.DRAFT.getCode());

                OrgIdDTO orgIdDTO = this.getOrgIdDTO(purchaserCode, supplierCode);
                if (this.getAutoSendConfigValue(orgIdDTO.getOrgId())) {
                    deliveryPlanDetail.setDetailStatus(DetailStatusEnum.TO_BE_CONFIRM.getCode());
                }

                deliveryPlanDetail.setSourceType(  DeliveryPlanSourceTypeEnum.MRP.getCode() );
                Asserts.isTrue( deliveryPlanDetailService.save( deliveryPlanDetail ) ,"保存送货计划明细失败！") ;


                // 当前在途数量
                BigDecimal totalRealDeliveryQuantity = Optional.ofNullable( materialCodeTotalRealDeliveryQuantityMap.get(materialCode) ).orElse( BigDecimal.ZERO);

                List<DeliveryPlanDetailItem> deliveryPlanDetailItemList = new ArrayList<>();
                for (PlanOrderDto planDemand : planDemandList) {

                    // 处理需求数量过滤
                    BigDecimal orderQty = planDemand.getFOrderQty();
                    orderQty = orderQty.subtract( totalRealDeliveryQuantity );

                    if( BigDecimal.ZERO.compareTo( totalRealDeliveryQuantity ) < 0 ) {
                        totalRealDeliveryQuantity = totalRealDeliveryQuantity.subtract( orderQty );
                        if(BigDecimal.ZERO.compareTo( totalRealDeliveryQuantity) > 0 ){
                            // 置0
                            totalRealDeliveryQuantity = BigDecimal.ZERO;
                        }
                    }

                    if( BigDecimal.ZERO.compareTo( orderQty) > 0 || planDemand.getFPlanFinishDate() == null){
                        // 当前需求日期的需求数量已经被送了
                        continue;
                    }

                    DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                    deliveryPlanDetailItem.setDeliveryPlanDetailId( deliveryPlanDetail.getId() );
                    deliveryPlanDetailItem.setSourceType( DeliveryPlanSourceTypeEnum.MRP.getCode());
                    // 需求数
                    deliveryPlanDetailItem.setDeliveryQuantity( orderQty.toString() );

                    // 运算建议发货日期和预计到货日期
                    DemandDateDto demandDateDto = this.handlePlanFinishDate(DemandDateDto.builder().demandLocalDate(planDemand.planFinishDateToLocalDate()).build(), transportationDuration, purchaserFactoryVacationDateStrSetMap.get(purchaserCode), supplierFactoryVacationDateStrSetMap.get(supplierCode));
                    // 预计到货日期
                    deliveryPlanDetailItem.setDeliveryDate(demandDateDto.estimateReceiveDateToLocalDateTime());
                    // 建议发货日期
                    deliveryPlanDetailItem.setSuggestedDeliveryDate(demandDateDto.getProposalDeliveryDate());
                    // 上一版的送货数量
                    PreVersionDeliveryPlanDetailItemVo preVersionDeliveryPlanDetailItemVo = PreVersionDeliveryPlanDetailItemVo.builder().productCode(purchaserCode).supplierCode(supplierCode).productCode(planDemand.getFMaterialId()).deliveryDate(deliveryPlanDetailItem.getDeliveryDate()).suggestedDeliveryDate(deliveryPlanDetailItem.getSuggestedDeliveryDate()).build();
                    deliveryPlanDetailItem.setPreviousEditionPlanQuantity( groupKeyDeliveryPlanDetailItemTotalDeliveryQuantityMap.get( preVersionDeliveryPlanDetailItemVo.buildGroupKey() ));

                    deliveryPlanDetailItemList.add( deliveryPlanDetailItem );
                }

                Asserts.isTrue( deliveryPlanDetailItemService.saveBatch( deliveryPlanDetailItemList ),"送货计划明细行保存失败！") ;

                //更新计划明细的 最近交货日期(还没进行发货的距离当前时间最近的日期) 和 最近计划数量
                this.postUpdateDeliveryPlanDetail( deliveryPlanDetailItemList,deliveryPlanDetail );

                currentDeliveryPlanDeliveryPlanDetailItemList.addAll( deliveryPlanDetailItemList );
            }

            this.postUpdateDeliveryPlan( currentDeliveryPlanDeliveryPlanDetailItemList,deliveryPlan );

        }

        // 设置当前最新版本号
        dataConfigService.updateCurrentMrpPlanOrderComputerNo( mrpPlanOrderComputerNo);

    }

    /**
     * 后置更新送货计划明细
     * @param deliveryPlanDetailItemList
     * @param deliveryPlanDetail
     */
    private void postUpdateDeliveryPlanDetail(List<DeliveryPlanDetailItem> deliveryPlanDetailItemList, DeliveryPlanDetail deliveryPlanDetail) {
        Optional<DeliveryPlanDetailItem> minDeliveryDateDeliveryPlanDetailItemOpt = deliveryPlanDetailItemList.stream().min(Comparator.comparing(DeliveryPlanDetailItem::getDeliveryDate));
        minDeliveryDateDeliveryPlanDetailItemOpt.ifPresent( deliveryPlanDetailItem -> {
            deliveryPlanDetail.setLatestDeliveryDate( deliveryPlanDetailItem.getDeliveryDate() ).setLatestPlanQuantity( deliveryPlanDetailItem.getDeliveryQuantity());
        } );

        //MRP计划数量
        Optional<BigDecimal> totalDeliveryQuantityOpt = deliveryPlanDetailItemList.stream().map(DeliveryPlanDetailItem::getDeliveryQuantity).map(BigDecimal::new).reduce(BigDecimal::add);
        Asserts.isTrue( totalDeliveryQuantityOpt.isPresent(),"计算送货计划明细项次的计划数量总值获取失败！" );
        deliveryPlanDetail.setMrpPlanQuantity( totalDeliveryQuantityOpt.get().longValue() );

        //计划数量= MRP计划数量 - 在途数量
        long planQuantity = deliveryPlanDetail.getMrpPlanQuantity() - deliveryPlanDetail.getTransitQuantity();
        deliveryPlanDetail.setPlanQuantity( planQuantity+"");

        Asserts.isTrue( deliveryPlanDetailService.updateById( deliveryPlanDetail ),"更新送货计划明细的信息失败！" );
    }

    /**
     * 后置更新送货计划信息
     * @param currentDeliveryPlanDeliveryPlanDetailItemList
     * @param deliveryPlan
     */
    private void postUpdateDeliveryPlan(List<DeliveryPlanDetailItem> currentDeliveryPlanDeliveryPlanDetailItemList, DeliveryPlan deliveryPlan) {
        // 更新送货计划的 计划区间 ( 预计到货日期)
        Optional<LocalDateTime> maxDeliveryDateOpt = currentDeliveryPlanDeliveryPlanDetailItemList.stream().map(DeliveryPlanDetailItem::getDeliveryDate).max(LocalDateTime::compareTo);
        Optional<LocalDateTime> minDeliveryDateOpt = currentDeliveryPlanDeliveryPlanDetailItemList.stream().map(DeliveryPlanDetailItem::getDeliveryDate).min(LocalDateTime::compareTo);

        deliveryPlan.setPlanStartDate( minDeliveryDateOpt.orElse(LocalDateTime.now() ) );
        deliveryPlan.setPlanEndDate( maxDeliveryDateOpt.orElse( LocalDateTime.now() ) );
        Asserts.isTrue( deliveryPlanService.updateById( deliveryPlan ),"更新送货计划的信息失败！" );

    }

    /**
     * 初始构建送货计划对象 100，OEM0009_100
     * @param purchaserCode
     * @param supplierCode
     */
    private DeliveryPlan initBuildDeliveryPlan(String purchaserCode, String supplierCode) {
        DeliveryPlan deliveryPlan = new DeliveryPlan(numberFactory.buildNumber(NumberType.plan));

        // 查询组织号
        OrgIdDTO orgIdDto = getOrgIdDTO(purchaserCode, supplierCode);
        deliveryPlan.setOrgId( orgIdDto.getOrgId() );
        deliveryPlan.setPurchaseCode( purchaserCode );
        deliveryPlan.setPurchaseName( orgIdDto.getPurchaseName() );
        deliveryPlan.setSupplierCode( orgIdDto.getSupplierCode() );
        deliveryPlan.setSupplierName( orgIdDto.getSupplierame() );
        //计划状态
        deliveryPlan.setPlanStatus( DeliveryPlanStatus.DRAFT.getCode());

        if (this.getAutoSendConfigValue(orgIdDto.getOrgId())){
            deliveryPlan.setPlanStatus( DeliveryPlanStatus.TO_BE_CONFIRM.getCode() );
            deliveryPlan.setPublisher("系统");
            deliveryPlan.setPublishDate( LocalDateTime.now());
        }

        deliveryPlan.setSourceType( DeliveryPlanSourceTypeEnum.MRP.getCode() );
        return deliveryPlan;
    }


    /**
     * 获取组织信息-
     * @param purchaserCode
     * @param supplierCode
     * @return
     */
    private OrgIdDTO getOrgIdDTO(String purchaserCode, String supplierCode) {
        R<OrgIdDTO> orgIdDtoR = purchaserFeignClient.info(new OrgIdQuery().setSupplierCode(supplierCode).setPurchaseCode(purchaserCode));
        Asserts.isTrue( orgIdDtoR.isSuccess( ) ,"供应商与采购方的组织信息查询失败！");
        Asserts.notNull( orgIdDtoR.getData() ,"供应商与采购方的组织信息查询失败！");
        return orgIdDtoR.getData();
    }

    /**
     * 获取自动发送计划排程的配置的值
     *
     * @param orgId
     * @return
     */
    private Boolean getAutoSendConfigValue(Long orgId) {
        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.MRP_DELIVERY_AUTO_SEND, orgId);
        Asserts.isTrue( cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() != null,"查询计划排程送货计划是否自动发送配置失败！" );

        String value = cfgParamInfoR.getData().getValue();
        try {
            if( Boolean.parseBoolean(value)){
                return Boolean.parseBoolean(value);

            }
        } catch (Exception ex){
            log.error("[getDeliveryPlanStatus] 查询计划排程送货计划是否自动发送配置 解析配置值[{}]失败", value,ex );
        }

        return false;
    }

    /**
     * 处理需求日期
     *
     * @param demandDateDto 需求日期传输对象
     * @param transportationDuration 运输时长
     * @param purchaserFactoryVacationDateStrSet 采购方假期数据
     * @param supplierFactoryVacationDateStrSet 供应商假期数据
     * @return
     */
    private DemandDateDto handlePlanFinishDate(DemandDateDto demandDateDto, Integer transportationDuration, Set<String> purchaserFactoryVacationDateStrSet, Set<String> supplierFactoryVacationDateStrSet) {
        LocalDate now = LocalDate.now();
        // 如果需求日期已是过去则
        if( demandDateDto.getDemandLocalDate().isBefore( now) ){
            // 处理日期往后推的逻辑，比如从  22号 到 23号 到 24号
            demandDateDto.setDemandLocalDate( LocalDate.now() );
            return this.handlePlanOrderData2( demandDateDto,transportationDuration,purchaserFactoryVacationDateStrSet,supplierFactoryVacationDateStrSet );
        }

        // 预计到货日期
        LocalDate estimateReceiveDate = this.beforeDateWhetherCan( purchaserFactoryVacationDateStrSet, demandDateDto.getDemandLocalDate() );
        // 如果预计到货日期小于当前
        if( estimateReceiveDate.isBefore( now ) ){
            // 处理日期往后推的逻辑，比如从  22号 到 23号 到 24号
            demandDateDto.setDemandLocalDate( LocalDate.now() );
            return this.handlePlanOrderData2( demandDateDto,transportationDuration,purchaserFactoryVacationDateStrSet,supplierFactoryVacationDateStrSet );
        }

        // 处理日期往前推的逻辑，比如从  22号 到 21号 到 20号

        // 建议发货日期
        LocalDate proposalDeliveryDate = this.beforeDateWhetherCan( supplierFactoryVacationDateStrSet, estimateReceiveDate.minusDays( transportationDuration ));

        // 如果建议发货日期小于当前
        if( proposalDeliveryDate.isBefore( now ) ){
            // 处理日期往后推的逻辑，比如从  22号 到 23号 到 24号
            demandDateDto.setDemandLocalDate( LocalDate.now() );
            return this.handlePlanOrderData2( demandDateDto,transportationDuration,purchaserFactoryVacationDateStrSet,supplierFactoryVacationDateStrSet );
        }

        // 如果不相等 则说明 建议发货日期 被推后了 则需要计算
        if( proposalDeliveryDate.plusDays( transportationDuration ).compareTo( estimateReceiveDate ) != 0) {
            demandDateDto.setDemandLocalDate( proposalDeliveryDate.plusDays( transportationDuration ) );
            return this.handlePlanFinishDate( demandDateDto, transportationDuration, purchaserFactoryVacationDateStrSet,supplierFactoryVacationDateStrSet );
        }

        demandDateDto.setEstimateReceiveDate( estimateReceiveDate );
        demandDateDto.setProposalDeliveryDate( proposalDeliveryDate );
        return demandDateDto;

    }

    /**
     * 处理需求日期
     *
     * @param demandDateDto 需求日期传输对象
     * @param transportationDuration 运输时长 1
     * @param purchaserFactoryVacationDateStrSet 采购方假期数据
     * @param supplierFactoryVacationDateStrSet 供应商假期数据
     */
    private DemandDateDto handlePlanOrderData2(DemandDateDto demandDateDto, Integer transportationDuration, Set<String> purchaserFactoryVacationDateStrSet, Set<String> supplierFactoryVacationDateStrSet) {

        // 建议发货日期
        LocalDate proposalDeliveryDate = this.afterDateWhetherCan( supplierFactoryVacationDateStrSet, demandDateDto.getDemandLocalDate() );
        // 预计到货日期
        LocalDate estimateReceiveDate = this.afterDateWhetherCan(purchaserFactoryVacationDateStrSet, proposalDeliveryDate.plusDays(transportationDuration));
        // 如果存在偏移
        if( proposalDeliveryDate.plusDays( transportationDuration ).compareTo( estimateReceiveDate ) != 0) {

            // 如果预计到货日期减去运输时长的日期，供应商可以发货
            proposalDeliveryDate = estimateReceiveDate.minusDays(transportationDuration);
            if( this.theDateNonHoliday( supplierFactoryVacationDateStrSet,proposalDeliveryDate ) ){
                demandDateDto.setEstimateReceiveDate( estimateReceiveDate );
                demandDateDto.setProposalDeliveryDate( proposalDeliveryDate );
                return demandDateDto;
            }

            demandDateDto.setDemandLocalDate( proposalDeliveryDate.plusDays( 1)  );
            return this.handlePlanOrderData2( demandDateDto, transportationDuration, purchaserFactoryVacationDateStrSet,supplierFactoryVacationDateStrSet );
        }
        demandDateDto.setEstimateReceiveDate( estimateReceiveDate );
        demandDateDto.setProposalDeliveryDate( proposalDeliveryDate );

        return demandDateDto;
    }

    /**
     * 计算非假期的日期  （往前计算）
     * @param factoryVacationDateStrSet 工厂假期
     * @param demandDate 需求日期
     * @return
     */
    private LocalDate beforeDateWhetherCan(Set<String> factoryVacationDateStrSet, LocalDate demandDate ) {

        // 是否可以收货
        if ( factoryVacationDateStrSet.contains( demandDate.toString() ))  {
            // 日期向前 -1
            demandDate = demandDate.minusDays( 1 );
            return this.beforeDateWhetherCan( factoryVacationDateStrSet,demandDate);
        }
        return demandDate;
    }

    /**
     * 计算非假期的日期  （往前计算）
     * @param factoryVacationDateStrSet 工厂假期
     * @param demandDate 需求日期
     * @return
     */
    private LocalDate afterDateWhetherCan(Set<String> factoryVacationDateStrSet, LocalDate demandDate ) {

        // 存在说明不可以发货
        if ( factoryVacationDateStrSet.contains( demandDate.toString() ))  {
            // 日期向后 +1
            demandDate = demandDate.plusDays( 1 );
            return this.afterDateWhetherCan( factoryVacationDateStrSet,demandDate);
        }
        return demandDate;
    }

    /**
     * 当前日期不是假期
     */
    private boolean theDateNonHoliday( Set<String> factoryVacationDateStrSet, LocalDate demandDate ){
        return ! factoryVacationDateStrSet.contains( demandDate.toString() );
    }


}
