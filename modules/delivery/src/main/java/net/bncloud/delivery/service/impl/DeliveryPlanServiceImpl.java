package net.bncloud.delivery.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.sys.DictItemDTO;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.repeatrequest.RepeatRequestOperation;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.*;
import net.bncloud.delivery.entity.*;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.event.*;
import net.bncloud.delivery.excel.DeliveryPlanDetailItemConverter;
import net.bncloud.delivery.mapper.DeliveryPlanDetailMapper;
import net.bncloud.delivery.mapper.DeliveryPlanMapper;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.service.DeliveryOperationLogService;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.service.DeliveryPlanService;
import net.bncloud.delivery.vo.*;
import net.bncloud.delivery.vo.event.DeliveryPlanEvent;
import net.bncloud.delivery.vo.event.PlanSchedulingDetailEvent;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailItemEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanEntity;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.config.vo.configvo.DeliveryCollaborationMethod;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 送货计划基础信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
@Service
@Slf4j
@SuppressWarnings({"all"})
public class DeliveryPlanServiceImpl extends BaseServiceImpl<DeliveryPlanMapper, DeliveryPlan> implements DeliveryPlanService {


    private final DefaultEventPublisher defaultEventPublisher;

    private final DeliveryPlanDetailService planDetailService;

    private final DeliveryPlanDetailItemService planDetailItemService;

    private final DeliveryPlanMapper deliveryPlanMapper;

    @Autowired
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;

    @Resource
    private DeliveryPlanDetailMapper deliveryPlanDetailMapper;


    private final DeliveryOperationLogService operationLogService;

    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;
    @Autowired
    @Lazy
    private SupplierFeignClient supplierFeignClient;

    public DeliveryPlanServiceImpl(DefaultEventPublisher defaultEventPublisher, DeliveryPlanDetailService planDetailService, DeliveryPlanDetailItemService planDetailItemService, DeliveryPlanMapper deliveryPlanMapper, DeliveryOperationLogService operationLogService) {
        this.defaultEventPublisher = defaultEventPublisher;
        this.planDetailService = planDetailService;
        this.planDetailItemService = planDetailItemService;
        this.deliveryPlanMapper = deliveryPlanMapper;
        this.operationLogService = operationLogService;
    }

    public void getCurrentSupplierStatus(String code) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(code);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        if (oneSupplierByCode.isSuccess()) {
            SupplierDTO supplier = oneSupplierByCode.getData();
            if (supplier == null) {
                log.error("供应商" + supplier.getCode() + "不存在");
                throw new IllegalStateException("供应商\"+supplier.getCode()+\"不存在");
            }

            if (supplier.getRelevanceStatus().equals(SupplierRelevanceStatusEnum.FROZEN.getCode())) {
                log.error("供应商" + supplier.getCode() + "状态" + SupplierRelevanceStatusEnum.FROZEN.getName());
                throw new IllegalStateException("供应商\"+supplier.getCode()+\"不存在");
            }

        }
    }

    /**
     * 分页查询送货计划明细
     *
     * @param pageable  page=1&size=10
     * @param pageParam 请求参数
     * @return
     */
    @Override
    public IPage<PlanSchedulingDetailVo> getDeliveryPlanDetailList(Pageable pageable, QueryParam<DeliveryPlanDetailParam> pageParam) {

        return planDetailService.selectPage(PageUtils.toPage(pageable), pageParam);
    }

    /**
     * 根据明细id查询送货批次
     *
     * @param deliveryPlanDetailId 送货计划明细id
     * @return
     */
    @Override
    public R<List<DeliveryPlanDetailItem>> getDeliveryPlanDetailItemList(String deliveryPlanDetailId) {

        List<DeliveryPlanDetailItem> items = deliveryPlanMapper.getItemByDetailId(deliveryPlanDetailId);
        //新增和删除的按钮
        Map<String, Boolean> operationButton = Maps.newConcurrentMap();

        DeliveryPlanDetail planDetail = planDetailService.getById(deliveryPlanDetailId);
        //计划明细的状态
        String detailStatus = planDetail.getDetailStatus();

        //送货计划
        DeliveryPlan deliveryPlan = this.getById(planDetail.getDeliveryPlanId());
        String purchaseCode = deliveryPlan.getPurchaseCode();
        String supplierCode = deliveryPlan.getSupplierCode();

        //采购方是否启用计划排程
        Boolean enablePlanScheduling = this.getMrpAutoSendCfg();
        //计划排程自动发送
        Boolean isAutoSendDelivery = this.getButtonConfig(CfgParamKeyEnum.MRP_DELIVERY_AUTO_SEND, purchaseCode, supplierCode);
        //计划排程是否支持手工新增
        Boolean isSupportManualAddition = this.getButtonConfig(CfgParamKeyEnum.DELIVERY_IS_SUPPORT_MANUAL_ADDITION, purchaseCode, supplierCode);

        if (enablePlanScheduling && !isAutoSendDelivery && isSupportManualAddition && DeliveryPlanStatus.DRAFT.getCode().equals(detailStatus)) {
            operationButton.put("addition", true);
            operationButton.put("delete", true);
            operationButton.put("edit", true);
        }

        R<List<DeliveryPlanDetailItem>> r = new R<>();
        r.setOperationButtons(operationButton);
        r.setData(items);
        return r;
    }



    /**
     * 获取按钮类型的协同配置的值
     *
     * @param code
     * @return
     */
    public Boolean getButtonConfig(CfgParamKeyEnum cfgParamKeyEnum,String purchaseCode,String supplierCode) {

        Long orgId = this.getOrgIdByPurchaseCodeAndSupplierCode(purchaseCode, supplierCode);

        //按钮类型的配置
        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(cfgParamKeyEnum, orgId);
        Asserts.isTrue(cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() !=null,"获取["+cfgParamKeyEnum.getDesc()+"]的协同配置失败！");
        CfgParamInfo cfgParamInfo = cfgParamInfoR.getData();

        String type = cfgParamInfo.getType();
        Asserts.isTrue("BOOL".equals(type),"获取["+cfgParamKeyEnum.getDesc()+"]的协同配置不是按钮类型的！");
        return Boolean.parseBoolean(cfgParamInfo.getValue());
    }

    /**
     * 获取组织id
     * @param purchaseCode 采购方编码
     * @param supplierCode 供应商编码
     * @return
     */
    @Override
    public Long getOrgIdByPurchaseCodeAndSupplierCode(String purchaseCode, String supplierCode) {
        R<OrgIdDTO> orgIdDtoR = purchaserFeignClient.info(new OrgIdQuery().setSupplierCode(supplierCode).setPurchaseCode(purchaseCode));
        Asserts.isTrue(orgIdDtoR.isSuccess(), "供应商与采购方的组织信息查询失败！");
        Asserts.notNull(orgIdDtoR.getData(), "供应商与采购方的组织信息查询失败！");
        return orgIdDtoR.getData().getOrgId();
    }


    /**
     * 获取协同配置中的是否开启计划排程的配置的值
     * @return
     */
    public Boolean getMrpAutoSendCfg(){
        // 查询送货协同方式配置
        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.DELIVERY_DELIVERY_COLLABORATION_METHOD, 112L);
        Assert.isTrue( cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() != null,"获取送货协同方式配置失败！" );
        DeliveryCollaborationMethod deliveryCollaborationMethod = JSON.parseObject( cfgParamInfoR.getData().getValue(), DeliveryCollaborationMethod.class);
        //是否开启计划排程
        return Boolean.parseBoolean(deliveryCollaborationMethod.getPlanScheduling());
    }

    /**
     * 批量发送送货计划
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RepeatRequestOperation
    public void sendBatch(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            for (String id : ids) {
                send(Long.parseLong(id));
            }
        }
    }

    /**
     * 发送送货计划
     *
     * @param id
     */
    @Override
    public void send(Long id) {
        //和供应商的合作关系
        DeliveryPlan plan = getById(id);
        getCurrentSupplierStatus(plan.getSupplierCode());


        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            //发送消息通知
            DeliveryPlan deliveryPlan = getById(id);
            DeliveryPlanEvent planEvent = BeanUtil.copy(deliveryPlan, DeliveryPlanEvent.class);
            planEvent.setDeliveryPlanId(deliveryPlan.getId());
            planEvent.setBusinessId(deliveryPlan.getId());
            defaultEventPublisher.publishEvent(new DeliveryPlanSendEvent(this, loginInfo, planEvent, planEvent.getPurchaseCode(), planEvent.getPurchaseName()));

            //修改送货计划状态，记录发布人和发布时间
            update(Wrappers.<DeliveryPlan>update().lambda()
                    .set(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.TO_BE_CONFIRM.getCode())
                    .set(DeliveryPlan::getPublisher, AuthUtil.getUser().getUserName())
                    .set(DeliveryPlan::getPublishDate, new Date())
                    .eq(DeliveryPlan::getId, id));

            //修改计划明细的状态
            planDetailService.update(Wrappers.<DeliveryPlanDetail>lambdaUpdate()
                    .set(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.TO_BE_CONFIRM.getCode())
                    .eq(DeliveryPlanDetail::getDeliveryPlanId, id));

            //记录操作记录
            DeliveryOperationLog operationLog = new DeliveryOperationLog()
                    .setBillId(id)
                    .setOperatorContent(DeliveryPlanOperationRel.SEND.getMsg())
                    .setOperatorNo(AuthUtil.getUser().getUserId() + "")
                    .setOperatorName(AuthUtil.getUser().getUserName());
            operationLogService.save(operationLog);


        } else {
            log.warn("获取用户登录信息失败");
        }
    }

    /**
     * 提醒供应商确认送货计划
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RepeatRequestOperation
    public void remind(Long id) {
        DeliveryPlan plan = getById(id);
        getCurrentSupplierStatus(plan.getSupplierCode());
        //发送消息通知
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            DeliveryPlan deliveryPlan = getById(id);
            DeliveryPlanEvent planEvent = BeanUtil.copy(deliveryPlan, DeliveryPlanEvent.class);
            planEvent.setDeliveryPlanId(deliveryPlan.getId());
            planEvent.setBusinessId(deliveryPlan.getId());
            defaultEventPublisher.publishEvent(new DeliveryPlanRemindEvent(this, loginInfo, planEvent, planEvent.getPurchaseCode(), planEvent.getPurchaseName()));

            //记录操作记录
            DeliveryOperationLog operationLog = new DeliveryOperationLog()
                    .setBillId(id)
                    .setOperatorContent(DeliveryPlanOperationRel.REMIND.getMsg())
                    .setOperatorNo(AuthUtil.getUser().getUserId() + "")
                    .setOperatorName(AuthUtil.getUser().getUserName());
            operationLogService.save(operationLog);
        } else {
            log.warn("获取用户登录信息失败");
        }
    }

    /**
     * 供应商确认事件
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RepeatRequestOperation
    public void confirm(Long id) {
        DeliveryPlan plan = getById(id);
        getCurrentSupplierStatus(plan.getSupplierCode());
        //修改送货计划状态
        update(Wrappers.<DeliveryPlan>update().lambda()
                .set(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.CONFIRMED.getCode())
                .eq(DeliveryPlan::getId, id));

        List<DeliveryPlanDetail> detailList = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                .eq(DeliveryPlanDetail::getDetailStatus,DetailStatusEnum.TO_BE_CONFIRM.getCode())
                .eq(DeliveryPlanDetail::getDeliveryPlanId, id));
        Optional.ofNullable(detailList).ifPresent(details->{
            for (DeliveryPlanDetail detail : details) {
                //根据送货日期升序排序
                List<DeliveryPlanDetailItem> items = deliveryPlanMapper.getPlanDetailItemByPlanId(id).stream()
                        .sorted(Comparator.comparing(DeliveryPlanDetailItem::getDeliveryDate)).collect(Collectors.toList());
                items.forEach(
                        item -> {
                            //项次的确认日期（等于送货日期）和确认数量（等于送货数量）
                            item.setConfirmQuantity(item.getDeliveryQuantity());
                            item.setConfirmDate(item.getDeliveryDate());
                            item.setConfirmSuggestedDeliveryDate(item.getSuggestedDeliveryDate());
                            planDetailItemService.updateById(item);
                        }
                );

                List<DeliveryPlanDetailItem> itemList = planDetailItemService.list(Condition.getQueryWrapper(new DeliveryPlanDetailItem().setDeliveryPlanDetailId(detail.getId())));
                //计算项次中的送货数量的和
                double totalDeliveryQuantity = itemList.stream().mapToDouble(item -> Double.parseDouble(item.getDeliveryQuantity())).sum();
                //最近确认数量
                String deliveryQuantity = "";
                //没有最近交货日期，就没有最近计划数量和最近确认数量
                List<DeliveryPlanDetailItem> collect = itemList.stream().filter(item -> item.getDeliveryDate().equals(detail.getLatestDeliveryDate()))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)) {
                    deliveryQuantity = collect.get(0).getDeliveryQuantity();
                }

                planDetailService.update(Wrappers.<DeliveryPlanDetail>update().lambda()
                        //计划明细的状态
                        .set(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.CONFIRMED.getCode())
                        //最近确认数量()
                        .set(DeliveryPlanDetail::getLatestConfirmQuantity, deliveryQuantity)
                        //确认数量=项次的送货数量的总和
                        .set(DeliveryPlanDetail::getConfirmQuantity, totalDeliveryQuantity)
                        .eq(DeliveryPlanDetail::getId, detail.getId()));
            }
        });

        //发送消息通知
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            DeliveryPlan deliveryPlan = getById(id);
            DeliveryPlanEvent planEvent = BeanUtil.copy(deliveryPlan, DeliveryPlanEvent.class);
            planEvent.setDeliveryPlanId(deliveryPlan.getId());
            planEvent.setBusinessId(deliveryPlan.getId());
            defaultEventPublisher.publishEvent(new DeliveryPlanConfirmEvent(this, loginInfo, planEvent, planEvent.getPurchaseCode(), planEvent.getPurchaseName()));


            //记录操作记录
            DeliveryOperationLog operationLog = new DeliveryOperationLog()
                    .setBillId(id)
                    .setOperatorContent(DeliveryPlanOperationRel.CONFIRM.getMsg())
                    .setOperatorNo(AuthUtil.getUser().getUserId() + "")
                    .setOperatorName(AuthUtil.getUser().getUserName());
            operationLogService.save(operationLog);
        } else {
            log.warn("获取用户登录信息失败");
        }
    }


    /**
     * 根据送货计划id查询送货计划详情
     *
     * @param
     * @return
     */
    @Override
    public DeliveryPlan getDeliveryPlanInfo(String id) {
        return getById(id);
    }

    /**
     * 送货计划状态数量统计
     *
     * @return
     */
    @Override
    public DeliveryPlanStatisticsVo getStatisticsInfo(String workBench) {
        LambdaQueryWrapper<DeliveryPlan> toBeConfirmWrapper = Wrappers.<DeliveryPlan>lambdaQuery().eq(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.TO_BE_CONFIRM.getCode());
        LambdaQueryWrapper<DeliveryPlan> differenceToBeConfirmedWrapper = Wrappers.<DeliveryPlan>lambdaQuery().eq(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.DIFFERENCE_TO_BE_CONFIRMED.getCode());

        if (WorkBench.SUPPLIER.getCode().equals(workBench)) {

            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                //待确认
                toBeConfirmWrapper.eq(DeliveryPlan::getSupplierCode, supplier.getSupplierCode());
                //差异方确认
                differenceToBeConfirmedWrapper.eq(DeliveryPlan::getSupplierCode, supplier.getSupplierCode());
            });

        }
        //待确认数量
        int toBeConfirmNum = count(toBeConfirmWrapper);
        //差异待确认数量
        int differenceToBeConfirmedNum = count(differenceToBeConfirmedWrapper);
        return new DeliveryPlanStatisticsVo().setToBeConfirmNum(toBeConfirmNum).setDifferenceToBeConfirmedNum(differenceToBeConfirmedNum);
    }


    /**
     * 打印
     *
     * @param pageParam
     * @return
     */
    @Override
    public PrintDataVo<DeliveryPlanDetailItemVo> printData(QueryParam<DeliveryPlanBoardParam> pageParam) {
        List<DeliveryPlanDetailItemVo> itemVos = baseMapper.selectDeliveryPlanBoardDetail(null, pageParam);
        PrintDataVo<DeliveryPlanDetailItemVo> vo = new PrintDataVo<>();
        vo.setData(itemVos);
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            vo.setPrintBy(loginInfo.getName());
            vo.setPrintDate(new Date());
            if (loginInfo.getCurrentSupplier() != null) {
                vo.setSupplier(loginInfo.getCurrentSupplier().getSupplierName());
            }
        } else {
            log.warn("获取用户登录信息失败");
        }
        return vo;
    }

    /**
     * 送货计划看板详情
     *
     * @param page
     * @param pageParam
     * @return
     */
    @Override
    public IPage<DeliveryPlanDetailItemVo> getDeliveryPlanBoardDetail(IPage<DeliveryPlanDetailItemVo> page, QueryParam<DeliveryPlanBoardParam> pageParam) {
        List<DeliveryPlanDetailItemVo> itemVos = baseMapper.selectDeliveryPlanBoardDetail(page, pageParam);
        return page.setRecords(itemVos);
    }

    /**
     * 按计划送货
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public IPage<DeliveryPlanDetailItemVo> getDeliveryAsPlanList(IPage<DeliveryPlanDetailItemVo> page, QueryParam<DeliveryAsPlanParam> queryParam) {
        if (queryParam == null || queryParam.getParam() == null || queryParam.getParam().getPurchaseCode() == null) {
            throw new RuntimeException("请先选择客户再选择送货计划");
        }
        List<DeliveryPlanDetailItemVo> deliveryPlanDetailItemVos = baseMapper.selectDeliveryAsPlan(page, queryParam);
        return page.setRecords(deliveryPlanDetailItemVos);
    }


    /**
     * 计划看板
     *
     * @param dateStr
     * @param queryParam
     * @return
     */
    @Override
    public List<DeliveryPlanBoardVo> getDeliveryPlanBoard(String dateStr, QueryParam<DeliveryPlanDetailItemParam> queryParam) {
        ArrayList<DeliveryPlanBoardVo> deliveryPlanBoardVos = new ArrayList<>();
        List<ConcurrentHashMap<String, String>> list = getWeekInterval(dateStr);
        list.forEach(hashMap -> {
            ArrayList<DeliveryPlanBoard> deliveryPlanBoards = new ArrayList<>();
            List<DeliveryPlanBoard> planBoards = deliveryPlanMapper.selectPlanBoard(hashMap.get("date"), queryParam);
            //产品编码的列表（去重后的）
            planBoards.stream().map(DeliveryPlanBoard::getProductCode).distinct()
                    .forEach(productCode -> {
                        //根据产品编码查找该产品的列表
                        List<DeliveryPlanBoard> boards = planBoards.stream().filter(item -> productCode.equals(item.getProductCode())).collect(Collectors.toList());
                        //计算计划送货总数
                        if (boards.size() > 0) {
                            long totalDeliveryQuantityNum = boards.stream().mapToLong(s -> Long.parseLong(s.getDeliveryQuantity())).sum();
                            DeliveryPlanBoard board = new DeliveryPlanBoard().setProductCode(productCode)
                                    .setProductName(boards.get(0).getProductName())
                                    .setTotalPlanDeliveryNum(totalDeliveryQuantityNum);
                            deliveryPlanBoards.add(board);
                        }
                    });
            //计划送货总数倒叙排序，只查6条
            List<DeliveryPlanBoard> collect = deliveryPlanBoards.stream().sorted(Comparator.comparing(DeliveryPlanBoard::getTotalPlanDeliveryNum).reversed()).limit(6).collect(Collectors.toList());
            //某一天的数据
            DeliveryPlanBoardVo planBoardVo = new DeliveryPlanBoardVo()
                    .setDate(hashMap.get("date"))
                    .setWeekNum(hashMap.get("week"))
                    .setDeliveryPlanBoardList(collect)
                    .setTotalProductNum(deliveryPlanBoards.size());
            deliveryPlanBoardVos.add(planBoardVo);
        });
        return deliveryPlanBoardVos;
    }

    /**
     * 获取日期所在的周区间
     *
     * @param date
     * @return
     * @throws
     */
    public static List<ConcurrentHashMap<String, String>> getWeekInterval(String date) {
        ArrayList<ConcurrentHashMap<String, String>> list = null;
        try {
            list = new ArrayList<>();
            Date parse = DateUtil.parse(date, DateUtil.PATTERN_DATE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parse);
            cal.setFirstDayOfWeek(Calendar.SUNDAY);
            // 获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day - 1);
            for (int i = 0; i < 7; i++) {
                cal.add(Calendar.DATE, 1);
                int weekNum = cal.get(Calendar.DAY_OF_WEEK);
                String dateStr = DateUtil.format(cal.getTime(), DateUtil.PATTERN_DATE);
                ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
                hashMap.put("date", dateStr);
                hashMap.put("week", "" + weekNum);
                list.add(hashMap);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 分页查询送货计划
     *
     * @param page
     * @param pageParam
     * @param workBench
     * @return2
     */
    @Override
    public PageImpl<DeliveryPlan> selectPage(IPage<DeliveryPlan> page, QueryParam<DeliveryPlanParam> pageParam, String workBench) {
        List<DeliveryPlan> deliveryPlans = baseMapper.selectListPage(page, pageParam, workBench);
        //设置 送货计划来源
        Optional.ofNullable(deliveryPlans).ifPresent(deliveryPlanList -> {
            deliveryPlanList.forEach(deliveryPlan -> {
                if (DeliveryPlanSourceTypeEnum.MRP.getCode().equals(deliveryPlan.getSourceType())) {
                    deliveryPlan.setSourceType(DeliveryPlanSourceTypeEnum.MRP.getDesc());
                } else {
                    deliveryPlan.setSourceType(DeliveryPlanSourceTypeEnum.PURCHASE_ORDER.getDesc());
                }
            });
        });
        return PageUtils.result(page.setRecords(deliveryPlans));
    }


    /**
     * 接收送货计划
     *
     * @param deliveryPlanDTO 送货计划
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveDeliveryPlan(DeliveryPlanDTO deliveryPlanDTO) {
        DeliveryPlanEntity deliveryPlanEntity = deliveryPlanDTO.getDeliveryPlanEntity();
        DeliveryPlan deliveryPlan = BeanUtil.copy(deliveryPlanEntity, DeliveryPlan.class);

        //取最早的交货日期和最晚的交货日期
        ArrayList<LocalDateTime> deliveryDateList = new ArrayList<>();
        deliveryPlanDTO.getPlanDetailEntityList().forEach(detail -> {
            detail.getPlanDetailItemList().forEach(item -> {
                deliveryDateList.add(item.getDeliveryDate());
            });
        });
        Optional<LocalDateTime> minDeliveryDate = deliveryDateList.stream().min(LocalDateTime::compareTo);
        Optional<LocalDateTime> maxDeliveryDate = deliveryDateList.stream().max(LocalDateTime::compareTo);
        minDeliveryDate.ifPresent(deliveryPlan::setPlanStartDate);
        maxDeliveryDate.ifPresent(deliveryPlan::setPlanEndDate);


        DeliveryPlan deliveryPlanDb = this.baseMapper.selectOneBySourceId(deliveryPlan.getSourceId());
        if (deliveryPlanDb != null) {
            deliveryPlan.setId(deliveryPlanDb.getId());
            deliveryPlan.setPlanNo(deliveryPlanDb.getPlanNo());
        }
        //1、保存送货计划基础信息
        saveOrUpdate(deliveryPlan);

        //2、保存送货计划明细
        List<DeliveryPlanDetailEntity> planDetailEntityList = deliveryPlanDTO.getPlanDetailEntityList();
        planDetailEntityList.forEach(planDetail -> {
            LocalDateTime now = LocalDateTime.now();
            List<DeliveryPlanDetailItemEntity> sortedItems = planDetail.getPlanDetailItemList().stream()
                    .filter(item -> now.equals(item.getDeliveryDate()) || now.isBefore(item.getDeliveryDate()))
                    .sorted(Comparator.comparing(DeliveryPlanDetailItemEntity::getDeliveryDate)).collect(Collectors.toList());
            if (sortedItems.size() > 0) {
                LocalDateTime deliveryDate = sortedItems.get(0).getDeliveryDate();
                // 最近交货日期
                planDetail.setLatestDeliveryDate(deliveryDate);
/*                最近计划数量(同一个日期可能存在多个项次，要求和)
                long sum = sortedItems.stream().filter(item -> deliveryDate.equals(item.getDeliveryDate()))
                        .map(DeliveryPlanDetailItemEntity::getDeliveryQuantity)
                        .mapToLong(Long::parseLong).sum();
                planDetail.setLatestPlanQuantity(String.valueOf(sum));*/

                // 最近计划数量（需求变更，取首项）
                planDetail.setLatestPlanQuantity(sortedItems.get(0).getDeliveryQuantity());
            }
        });

        List<DeliveryPlanDetail> planDetailList = buildPlanDetailList(deliveryPlan.getId(), planDetailEntityList);
        if (CollectionUtil.isNotEmpty(planDetailList)) {
            for (DeliveryPlanDetail deliveryPlanDetail : planDetailList) {
                planDetailService.saveOrUpdate(deliveryPlanDetail);
            }
        }

        //3、保存送货计划明细项次信息
        List<DeliveryPlanDetailItem> planDetailItemList = buildPlanDetailItemList(planDetailEntityList);
        if (CollectionUtil.isNotEmpty(planDetailItemList)) {
            for (DeliveryPlanDetailItem deliveryPlanDetailItem : planDetailItemList) {
                //添加剩余数量默认值：默认和计划送货数量一致
                deliveryPlanDetailItem.setRemainingQuantity(deliveryPlanDetailItem.getDeliveryQuantity());
                planDetailItemService.saveOrUpdate(deliveryPlanDetailItem);
            }
        }


    }


    /**
     * 构建送货计划明细项次列表
     *
     * @param planDetailEntityList 送货计划明细
     * @return 送货计划项次列表
     */
    private List<DeliveryPlanDetailItem> buildPlanDetailItemList(List<DeliveryPlanDetailEntity> planDetailEntityList) {
        if (CollectionUtil.isNotEmpty(planDetailEntityList)) {
            List<DeliveryPlanDetailItem> detailItemList = new ArrayList<>();
            for (DeliveryPlanDetailEntity deliveryPlanDetailEntity : planDetailEntityList) {
                List<DeliveryPlanDetailItemEntity> planDetailItemList = deliveryPlanDetailEntity.getPlanDetailItemList();
                DeliveryPlanDetail planDetail = planDetailService.queryOneBySourceId(deliveryPlanDetailEntity.getSourceId());
                if (planDetail == null) {
                    log.warn("请先保存送货明细【planDetail】，sourceId={}", deliveryPlanDetailEntity.getSourceId());
                    continue;
                }
                if (CollectionUtil.isNotEmpty(planDetailItemList)) {
                    for (DeliveryPlanDetailItemEntity detailItemEntity : planDetailItemList) {
                        DeliveryPlanDetailItem planDetailItem = BeanUtil.copy(detailItemEntity, DeliveryPlanDetailItem.class);
                        planDetailItem.setDeliveryPlanDetailId(planDetail.getId());
                        DeliveryPlanDetailItem planDetailItemDb = planDetailItemService.queryOneBySourceId(planDetailItem.getSourceId());
                        if (planDetailItemDb != null) {
                            planDetailItem.setId(planDetailItemDb.getId());
                        }
                        detailItemList.add(planDetailItem);
                    }

                }
            }
            return detailItemList;
        }
        return null;
    }

    /**
     * 构建智采送货计划明细列表
     *
     * @param deliveryPlanId       送货计划ID
     * @param planDetailEntityList 送货计划明细列表
     * @return 智采送货计划明细列表
     */
    private List<DeliveryPlanDetail> buildPlanDetailList(Long deliveryPlanId, List<DeliveryPlanDetailEntity> planDetailEntityList) {
        if (deliveryPlanId != null && CollectionUtil.isNotEmpty(planDetailEntityList)) {
            List<DeliveryPlanDetail> planDetailList = new ArrayList<>();
            for (DeliveryPlanDetailEntity planDetailEntity : planDetailEntityList) {
                DeliveryPlanDetail planDetail = BeanUtil.copy(planDetailEntity, DeliveryPlanDetail.class);
                //送货计划id
                planDetail.setDeliveryPlanId(deliveryPlanId);


                DeliveryPlanDetail planDetailDb = planDetailService.queryOneBySourceId(planDetail.getSourceId());
                if (planDetailDb != null) {
                    planDetail.setId(planDetailDb.getId());
                }
                planDetailList.add(planDetail);
            }
            return planDetailList;
        }
        return null;
    }


    /**
     * 新增和修改
     *
     * @param items
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSaveOrUpdateItem(List<DeliveryPlanDetailItem> items) {
        if (CollectionUtil.isNotEmpty(items)) {
            //新增或保存项次
            items.forEach(planDetailItemService::saveOrUpdate);
            //查询计划明细
            Long detailId = items.get(0).getDeliveryPlanDetailId();
            DeliveryPlanDetail planDetail = planDetailService.getById(detailId);
            //计算该计划明细的所有项次的送货数量的和
            List<DeliveryPlanDetailItem> itemList = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                    .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, detailId));
            Optional.of(itemList).ifPresent(itemList1 -> {
                long sum = itemList1.stream().mapToLong(item -> Long.parseLong(item.getDeliveryQuantity())).sum();
                planDetail.setPlanQuantity(sum + "");
            });
            //修改计划明细
            planDetailService.updateById(planDetail);

        }

    }


    /**
     * 1.删除计划明细的项次
     * 2.同时扣减计划明细的计划数量
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDeleteItem(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            //删除项次
            deleteLogic(ids);
            //查询计划明细
            Long planDetailId = planDetailItemService.getById(ids.get(0)).getDeliveryPlanDetailId();
            DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(planDetailId);
            //重新计算该计划明细下面的项次的送货数量的和
            List<DeliveryPlanDetailItem> itemList = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                    .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, planDetailId));
            Optional.of(itemList).ifPresent(items -> {
                long sum = items.stream().mapToLong(item -> Long.parseLong(item.getDeliveryQuantity())).sum();
                deliveryPlanDetail.setPlanQuantity(sum + "");
            });
            //更新计划明细
            planDetailService.updateById(deliveryPlanDetail);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void differenceConfirmById(Long planId) {
        if (planId != null) {
            //修改送货计划明细的状态为已确认
            planDetailService.update(Wrappers.<DeliveryPlanDetail>lambdaUpdate()
                    .set(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.CONFIRMED.getCode())
                    .eq(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.DIFFERENCE_TO_BE_CONFIRMED.getCode())
                    .eq(DeliveryPlanDetail::getDeliveryPlanId, planId));
            //修改送货计划的状态为已确认
            this.update(Wrappers.<DeliveryPlan>lambdaUpdate()
                    .set(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.CONFIRMED.getCode())
                    .eq(DeliveryPlan::getId, planId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateItem(List<DeliveryPlanDetailItem> itemList) {
        if (CollectionUtil.isNotEmpty(itemList)) {
            itemList.forEach(item -> {
                planDetailItemService.saveOrUpdate(item);
            });
        }
    }

    /**
     * 采购方的计划排程的看板
     * 1.【最近一版】的计划【排程】
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<PlanSchedulingBoardVo> getZcPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, QueryParam<PlanSchedulingBoardParam> queryParam) {
        List<PlanSchedulingBoardVo> planSchedulingBoardVos = deliveryPlanDetailMapper.selectZcPlanSchedulingBoardPage(page, queryParam);



        //看板中的动态标题-展示当天开始的后29天，共30天的数据
        List<String> thirtyDayTitles = Lists.newArrayList();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate plusDay = now.plusDays(i);
            String plusDayStr = DateUtil.formatDate(plusDay);
            thirtyDayTitles.add(plusDayStr);
        }


        Optional.ofNullable(planSchedulingBoardVos).ifPresent(planSchedulingBoardVoList -> {
            planSchedulingBoardVoList.forEach(planSchedulingBoardVo -> {
                Map<String, DeliveryPlanDetailItem> hashMap = new HashMap<>();
                //计划明细的所有项次
                List<DeliveryPlanDetailItem> items = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                        .between(DeliveryPlanDetailItem::getDeliveryDate,LocalDate.now().atStartOfDay() ,LocalDateTime.now().plusDays(29))
                        .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, planSchedulingBoardVo.getPlanDetailId()));
                if (CollectionUtil.isNotEmpty(items)) {
                    items.forEach(item -> {
                        if (item.getDeliveryDate() == null) {
                            log.info("项次{}的预计到货日期为空", JSON.toJSONString(item));
                            throw new RuntimeException("预计到货日期为空！");
                        }
                        String formatDate = DateUtil.formatDate(item.getDeliveryDate());
                        hashMap.put(formatDate, item);
                    });
                }
                planSchedulingBoardVo.setDateTitle(thirtyDayTitles);
                planSchedulingBoardVo.setDynamicDataMap(hashMap);
            });

        });

        return PageUtils.result(page.setRecords(planSchedulingBoardVos));
    }

    /**
     * 销售工作台-计划排程看板
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<PlanSchedulingBoardVo> getZyPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, QueryParam<PlanSchedulingBoardParam> queryParam) {
        //供应商只能看到自己的计划明细
        @Valid PlanSchedulingBoardParam param = queryParam.getParam();
        if (ObjectUtil.isNotEmpty(param)) {
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                param.setSupplier(supplier.getSupplierCode());
            });
        }
        List<PlanSchedulingBoardVo> planSchedulingBoardVos = deliveryPlanDetailMapper.selectZyPlanSchedulingBoardPage(page, queryParam);


        //看板中的动态标题-展示当天开始的后29天，共30天的数据
        List<String> thirtyDayTitles = Lists.newArrayList();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate plusDay = now.plusDays(i);
            String plusDayStr = DateUtil.formatDate(plusDay);
            thirtyDayTitles.add(plusDayStr);
        }


        Optional.ofNullable(planSchedulingBoardVos).ifPresent(planSchedulingBoardVoList -> {
            planSchedulingBoardVoList.forEach(planSchedulingBoardVo -> {
                Map<String, DeliveryPlanDetailItem> hashMap = new HashMap<>();
                //计划明细的所有项次
                List<DeliveryPlanDetailItem> items = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                        .between(DeliveryPlanDetailItem::getSuggestedDeliveryDate, LocalDate.now(), LocalDate.now().plusDays(29))
                        .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, planSchedulingBoardVo.getPlanDetailId()));
                if (CollectionUtil.isNotEmpty(items)) {
                    items.forEach(item -> {
                        if (item.getSuggestedDeliveryDate() == null) {
                            log.info("项次{}的建议发货日期为空", JSON.toJSONString(item));
                            throw new RuntimeException("预计到货日期为空！");
                        }
                        String formatDate = DateUtil.formatDate(item.getSuggestedDeliveryDate());
                        hashMap.put(formatDate, item);
                    });
                }
                planSchedulingBoardVo.setDateTitle(thirtyDayTitles);
                planSchedulingBoardVo.setDynamicDataMap(hashMap);
            });
        });

        return PageUtils.result(page.setRecords(planSchedulingBoardVos));
    }

    /**
     * 提醒（看板-计划明细）
     * 1.对【待确认】状态的计划明细进行提醒
     *
     * @param planDetailIds
     */
    @Override
    public void remindPlanScheduling(List<Long> planDetailIds) {
        if (CollectionUtil.isNotEmpty(planDetailIds)) {
            for (Long id : planDetailIds) {
                //计划明细
                DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(id);
                //只对【待确认】状态的计划明细进行提醒
                if (!deliveryPlanDetail.getDetailStatus().equals(DetailStatusEnum.TO_BE_CONFIRM.getCode())) {
                    continue;
                }
                //送货计划
                DeliveryPlan deliveryPlan = getById(deliveryPlanDetail.getDeliveryPlanId());
                //发送消息
                this.planSchedulingDetailEvent(deliveryPlanDetail, deliveryPlan.getPurchaseCode(), deliveryPlan.getPurchaseName(), EventCode.plan_scheduling_detail_remind.getCode());
            }

        }

    }

    /**
     * 获取最新一版的所有【待发布】计划排程
     *
     * @return
     */
    private List<Long> getLatestVersionDeliveryPlan() {
        //当前的版本号
        String mrpComputerNo = deliveryPlanMapper.selectDeliveryPlanLatestVersion();
        List<DeliveryPlan> planSchedulingList = this.list(Wrappers.<DeliveryPlan>lambdaQuery().eq(DeliveryPlan::getMrpComputerNo, mrpComputerNo)
                .eq(DeliveryPlan::getSourceType, "mrp"));
        return planSchedulingList.stream().map(DeliveryPlan::getId).collect(Collectors.toList());

    }

    /**
     * 发布计划排程明细
     * 1.对最近一版的所有的【待发布】状态的计划排程进行发布
     * 2.修改所有的【待发布】的计划明细的状态为【待确认】
     * 3.发送消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishPlanScheduling() {
        List<Long> deliveryPlanIds = this.getLatestVersionDeliveryPlan();
        if (CollectionUtil.isNotEmpty(deliveryPlanIds)) {
            deliveryPlanIds.forEach(id -> {
                //送货计划
                DeliveryPlan deliveryPlan = getById(id);
                //修改计划状态为待确认,设置发布人和发布时间
                deliveryPlan.setPlanStatus(DeliveryPlanStatus.TO_BE_CONFIRM.getCode())
                        .setPublisher(AuthUtil.getUser().getUserName())
                        .setPublishDate(LocalDateTime.now());
                this.updateById(deliveryPlan);

                //DeliveryPlanEvent deliveryPlanEvent = BeanUtil.copy(deliveryPlan, DeliveryPlanEvent.class);
                //deliveryPlanEvent.setBusinessId(deliveryPlan.getOrgId());
                //deliveryPlanEvent.setDeliveryPlanId(deliveryPlan.getId());
                //SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
                //    defaultEventPublisher.publishEvent(new DeliveryPlanSendEvent(this, loginInfo, deliveryPlanEvent, deliveryPlanEvent.getPurchaseCode(), deliveryPlanEvent.getPurchaseName()));
                //});
                //
                ////记录操作记录
                //DeliveryOperationLog operationLog = new DeliveryOperationLog()
                //        .setBillId(id)
                //        .setOperatorContent(DeliveryPlanOperationRel.SEND.getMsg())
                //        .setOperatorNo(AuthUtil.getUser().getUserId() + "")
                //        .setOperatorName(AuthUtil.getUser().getUserName());
                //operationLogService.save(operationLog);

                //计划明细
                List<DeliveryPlanDetail> planDetails = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                        .eq(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.DRAFT.getCode())
                        .eq(DeliveryPlanDetail::getDeliveryPlanId, id));
                Optional.ofNullable(planDetails).ifPresent(planDetailList -> {
                    planDetailList.forEach(planDetail -> {
                        //修改计划明细状态为待确认
                        planDetail.setDetailStatus(DetailStatusEnum.TO_BE_CONFIRM.getCode());
                        planDetailService.updateById(planDetail);
                        //发送消息
                        this.planSchedulingDetailEvent(planDetail, deliveryPlan.getPurchaseCode(), deliveryPlan.getPurchaseName(), EventCode.plan_scheduling_detail_send.getCode());
                    });
                });
            });
        }
    }

    /**
     * 计划排程明细的消息
     */
    public void planSchedulingDetailEvent(DeliveryPlanDetail deliveryPlanDetail, String sourceCode, String sourceName, String messageType) {
        if (ObjectUtil.isNotEmpty(deliveryPlanDetail)) {
            //判断供应商的合作关系
            this.getCurrentSupplierStatus(deliveryPlanDetail.getSupplierCode());
            SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {

                PlanSchedulingDetailEvent planSchedulingDetailEvent = BeanUtil.copy(deliveryPlanDetail, PlanSchedulingDetailEvent.class);
                planSchedulingDetailEvent.setDeliveryPlanDetailId(deliveryPlanDetail.getId());
                planSchedulingDetailEvent.setBusinessId(deliveryPlanDetail.getDeliveryPlanId());

                String content = "";
                //发送站内消息
                if (EventCode.plan_scheduling_detail_send.getCode().equals(messageType)) { //发布
                    content = EventCode.plan_scheduling_detail_send.getName();
                    defaultEventPublisher.publishEvent(new PlanSchedulingDetailSendEvent(this, loginInfo, planSchedulingDetailEvent, sourceCode, sourceName));

                } else if (EventCode.plan_scheduling_detail_remind.getCode().equals(messageType)) { //提醒
                    content = EventCode.plan_scheduling_detail_remind.getName();
                    defaultEventPublisher.publishEvent(new PlanSchedulingDetailRemindEvent(this, loginInfo, planSchedulingDetailEvent, sourceCode, sourceName));

                } else if (EventCode.plan_scheduling_detail_confirm.getCode().equals(messageType)) { //确认
                    content = EventCode.plan_scheduling_detail_confirm.getName();
                    defaultEventPublisher.publishEvent(new PlanSchedulingDetailConfirmEvent(this, loginInfo, planSchedulingDetailEvent, sourceCode, sourceName));

                } else if (EventCode.plan_scheduling_detail_difference_confirm.getCode().equals(messageType)) { //差异确认
                    content = EventCode.plan_scheduling_detail_difference_confirm.getName();
                    defaultEventPublisher.publishEvent(new PlanSchedulingDetailDifferenceConfirmEvent(this, loginInfo, planSchedulingDetailEvent, sourceCode, sourceName));
                }

                //记录操作记录
                DeliveryOperationLog operationLog = new DeliveryOperationLog()
                        .setBillId(deliveryPlanDetail.getDeliveryPlanId())
                        .setOperatorContent(content)
                        .setOperatorNo(AuthUtil.getUser().getUserId() + "")
                        .setOperatorName(AuthUtil.getUser().getUserName());
                operationLogService.save(operationLog);

            });
        }
    }

    /**
     * 采购方差异确认
     * 1.修改计划排程明细为已确认，（如所有明细都是已确认，修改计划状态为已确认）
     * 2.发送消息
     *
     * @param planDetailIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void differenceConfirmPlanScheduling(List<Long> planDetailIds) {
        if (CollectionUtil.isNotEmpty(planDetailIds)) {

            for (Long planDetailId : planDetailIds) {
                //计划明细
                DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(planDetailId);
                //只对 【差异待确认】状态的计划明细进行差异确认
                if (!deliveryPlanDetail.getDetailStatus().equals(DetailStatusEnum.DIFFERENCE_TO_BE_CONFIRMED.getCode())) {
                    continue;
                }

                //送货计划
                DeliveryPlan deliveryPlan = this.getById(deliveryPlanDetail.getDeliveryPlanId());

                //修改计划排程明细为已确认
                deliveryPlanDetail.setDetailStatus(DetailStatusEnum.CONFIRMED.getCode());
                planDetailService.updateById(deliveryPlanDetail);
                this.planSchedulingDetailEvent(deliveryPlanDetail, deliveryPlan.getPurchaseCode(), deliveryPlan.getPurchaseName(), EventCode.plan_scheduling_detail_difference_confirm.getCode());

                //如所有明细都是已确认，修改计划状态为已确认
                List<DeliveryPlanDetail> detailList = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                        .eq(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlan.getId())
                        .ne(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.CONFIRMED.getCode()));
                if (CollectionUtil.isEmpty(detailList)) {
                    deliveryPlan.setPlanStatus(DeliveryPlanStatus.CONFIRMED.getCode());
                    this.updateById(deliveryPlan);
                }
            }

        }
    }

    /**
     * 确认计划排程明细
     * 1.修改计划明细状态为已确认，设置明细的确认数量和最近确认数量  （如所有明细都是已确认状态，修改计划状态为已确认）
     * 2.设置项次的确认到货日期、确认送货日期和确认数量
     * 3.发送站内信和记录操作日志
     *
     * @param planDetailIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPlanSchedulingDetail(List<Long> planDetailIds) {
        if (CollectionUtil.isNotEmpty(planDetailIds)) {

            for (Long planDetailId : planDetailIds) {
                DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(planDetailId);
                //只对 【待确认】状态的计划明细进行确认
                if (! DetailStatusEnum.TO_BE_CONFIRM.getCode().equals( deliveryPlanDetail.getDetailStatus() ) ) {
                    continue;
                }

                //计划明细项次
                List<DeliveryPlanDetailItem> detailItemList = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery().eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, planDetailId));
                Optional.ofNullable(detailItemList).ifPresent(detailItems -> {
                    detailItems.forEach(item -> {
                        item.setConfirmSuggestedDeliveryDate(item.getSuggestedDeliveryDate())
                                .setConfirmQuantity(item.getDeliveryQuantity())
                                .setConfirmDate(item.getDeliveryDate());
                        planDetailItemService.updateById(item);
                    });

                    //明细的确认数量=项次的送货数量的和
                    //long detailConfirmQuantity = detailItems.stream().mapToLong(item -> Double.parseDouble(item.getDeliveryQuantity())).sum();
                    double detailConfirmQuantity = detailItems.stream().mapToDouble(item -> Double.parseDouble(item.getDeliveryQuantity())).sum();
                    //明细的最近确认数量
                    String latestConfirmQuantity = "";
                    //没有最近交货日期，就没有最近计划数量和最近确认数量
                    List<DeliveryPlanDetailItem> collect = detailItems.stream().filter(item -> item.getDeliveryDate().equals(deliveryPlanDetail.getLatestDeliveryDate()))
                            .collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(collect)) {
                        latestConfirmQuantity = collect.get(0).getConfirmQuantity();
                    }

                    //修改明细
                    deliveryPlanDetail.setDetailStatus(DetailStatusEnum.CONFIRMED.getCode())
                            .setConfirmQuantity(detailConfirmQuantity + "")
                            .setLatestConfirmQuantity(latestConfirmQuantity);
                    planDetailService.updateById(deliveryPlanDetail);

                });

                //发送站内信 记录操作日志
                DeliveryPlan deliveryPlan = this.getById(deliveryPlanDetail.getDeliveryPlanId());
                this.planSchedulingDetailEvent(deliveryPlanDetail, deliveryPlan.getPurchaseCode(), deliveryPlan.getPurchaseName(), EventCode.plan_scheduling_detail_confirm.getCode());

                //如所有明细都是已确认，修改计划状态为已确认;
                List<DeliveryPlanDetail> detailList = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                        .ne(DeliveryPlanDetail::getDetailStatus, DetailStatusEnum.CONFIRMED.getCode())
                        .eq(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlan.getId()));


                List<String> detailStatuList = Lists.newArrayList();
                detailStatuList.add(DetailStatusEnum.TO_BE_CONFIRM.getCode());
                detailStatuList.add(DetailStatusEnum.DRAFT.getCode());
                List<DeliveryPlanDetail> detailList1 = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                        .in(DeliveryPlanDetail::getDetailStatus,detailStatuList)
                        .eq(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlan.getId()));

                List<DeliveryPlanDetail> detailList2 = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                        .eq(DeliveryPlanDetail::getDetailStatus,DetailStatusEnum.DIFFERENCE_TO_BE_CONFIRMED.getCode())
                        .eq(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlan.getId()));




                if (CollectionUtil.isEmpty(detailList)) {
                    deliveryPlan.setPlanStatus(DeliveryPlanStatus.CONFIRMED.getCode());
                    this.updateById(deliveryPlan);
                } else if (CollectionUtil.isEmpty(detailList1) && CollectionUtil.isNotEmpty(detailList2)) {
                    deliveryPlan.setPlanStatus(DeliveryPlanStatus.DIFFERENCE_TO_BE_CONFIRMED.getCode());
                    this.updateById(deliveryPlan);
                }
            }
        }
    }

    /**
     * 保存计划排程明细项次(供应商确认)
     * 供应商可以差异答复为前提
     * 1.修改【排程明细】状态为【差异答复】，设置明细的确认数量和最近确认数量  （如该计划排程的排程明细都是已确认和差异答复，修改【计划排程状态】为【差异待确认】）
     * 2.设置项次的确认到货日期、确认送货日期和确认数量
     * 3.发送站内信和记录操作日志
     *
     * @param itemList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSavePlanSchedulingDetailItem(List<PlanSchedulingDetailItem> itemList) {


       Long itemId = itemList.get(0).getId();
        Long planDetailId = planDetailItemService.getById(itemId).getDeliveryPlanDetailId();

        DeliveryPlanDetail planDetail = planDetailService.getById(planDetailId);
        DeliveryPlan deliveryPlan = this.getById(planDetail.getDeliveryPlanId());
        //供应商是否可以对送货计划进行差异答复
        Boolean flag = this.getButtonConfig(CfgParamKeyEnum.DELIVERY_SUPPLIER_DISCREPANCY_REPLY, deliveryPlan.getPurchaseCode(), deliveryPlan.getSupplierCode());

        log.info("供应商是否可以对送货计划进行差异答复,{}", flag);
        if (flag && CollectionUtil.isNotEmpty(itemList)) {

            //明细的状态
            AtomicReference<String> detailStatusAtomicReference = new AtomicReference<>(DetailStatusEnum.CONFIRMED.getCode());


            itemList.forEach(item -> {
                DeliveryPlanDetailItem planDetailItem = planDetailItemService.getById(item.getId());
                //如果确认数量不等于送货数量，证明修改了项次
                if (!planDetailItem.getDeliveryQuantity().equals(item.getConfirmQuantity())) {
                    //修改计划明细的状态为差异待确认
                    detailStatusAtomicReference.set(DetailStatusEnum.DIFFERENCE_TO_BE_CONFIRMED.getCode());
                }


                planDetailItemService.update(Wrappers.<DeliveryPlanDetailItem>lambdaUpdate()
                        //确认到货日期 = 预计到货日期
                        .set(DeliveryPlanDetailItem::getConfirmDate, planDetailItem.getDeliveryDate())
                        //确认发货日期 = 建议发货日期
                        .set(DeliveryPlanDetailItem::getConfirmSuggestedDeliveryDate, planDetailItem.getSuggestedDeliveryDate())
                        //有修改权限，确认数量 = 修改后的数量
                        .set(DeliveryPlanDetailItem::getConfirmQuantity, item.getConfirmQuantity())
                        //差异原因
                        .set(DeliveryPlanDetailItem::getDifferenceReason,item.getDifferenceReason())
                        //差异数
                        .set(DeliveryPlanDetailItem::getVarianceNumber,Double.parseDouble(planDetailItem.getDeliveryQuantity())-Double.parseDouble(item.getConfirmQuantity()))
                        .eq(DeliveryPlanDetailItem::getId, item.getId()));
            });


            List<DeliveryPlanDetailItem> planDetailItems = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                    .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, planDetailId));
            //明细的确认送货数量
            double totalDeliveryQuantity = planDetailItems.stream().mapToDouble(item -> Double.parseDouble(item.getConfirmQuantity())).sum();
            //明细的差异数 = 项次的差异数的和
            long varianceNumberSum = planDetailItems.stream().mapToLong(DeliveryPlanDetailItem::getVarianceNumber).sum();

            //明细的最近确认数量
            String latestConfirmQuantity = "";
            //计划明细
            Long deliveryPlanDetailId = planDetailItemService.getById(itemList.get(0).getId()).getDeliveryPlanDetailId();
            DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(deliveryPlanDetailId);
            //没有最近交货日期，就没有最近计划数量和最近确认数量
            if (deliveryPlanDetail.getLatestDeliveryDate()!=null){
                List<DeliveryPlanDetailItem> collect = itemList.stream().filter(item -> {
                    DeliveryPlanDetailItem deliveryPlanDetailItem = planDetailItemService.getById(item.getId());
                    return deliveryPlanDetailItem.getDeliveryDate().equals(deliveryPlanDetail.getLatestDeliveryDate());
                }).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)) {
                    latestConfirmQuantity = collect.get(0).getConfirmQuantity();
                }
                deliveryPlanDetail.setLatestConfirmQuantity(latestConfirmQuantity);
            }
            //计划明细状态
            deliveryPlanDetail.setDetailStatus(detailStatusAtomicReference.get());
            //计划明细的确认数量
            deliveryPlanDetail.setConfirmQuantity(totalDeliveryQuantity + "");
            //差异数
            deliveryPlanDetail.setVarianceNumber(varianceNumberSum);
            planDetailService.updateById(deliveryPlanDetail);



            List<String> detailStatuList = Lists.newArrayList();
            detailStatuList.add(DetailStatusEnum.TO_BE_CONFIRM.getCode());
            detailStatuList.add(DetailStatusEnum.DRAFT.getCode());
            //如该计划排程的排程明细都是 【已确认和差异待确认】 或 【都是差异待确认状态】，修改【计划排程状态】为【差异待确认】
            List<DeliveryPlanDetail> detailList = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                    .eq(DeliveryPlanDetail::getSourceType, DeliveryPlanSourceTypeEnum.MRP.getCode())
                    .eq(DeliveryPlanDetail::getDeliveryPlanId,deliveryPlanDetail.getDeliveryPlanId())
                    .in(DeliveryPlanDetail::getDetailStatus, detailStatuList));

            List<DeliveryPlanDetail> list = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery()
                    .eq(DeliveryPlanDetail::getSourceType, DeliveryPlanSourceTypeEnum.MRP.getCode())
                    .eq(DeliveryPlanDetail::getDeliveryPlanId,deliveryPlanDetail.getDeliveryPlanId())
                    .eq(DeliveryPlanDetail::getDetailStatus,DetailStatusEnum.DIFFERENCE_TO_BE_CONFIRMED.getCode()));

            //没有待发布和待确认的 同时 有差异待确认的 明细
            if (CollectionUtil.isEmpty(detailList) && CollectionUtil.isNotEmpty(list)) {
                update(Wrappers.<DeliveryPlan>lambdaUpdate().set(DeliveryPlan::getPlanStatus, DeliveryPlanStatus.DIFFERENCE_TO_BE_CONFIRMED.getCode())
                .eq(DeliveryPlan::getId,deliveryPlanDetail.getDeliveryPlanId()));
            }

            DeliveryPlan plan = this.getById(deliveryPlanDetail.getDeliveryPlanId());
            //发站内信，操作记录
            this.planSchedulingDetailEvent(deliveryPlanDetail, plan.getPurchaseCode(), plan.getPurchaseName(), EventCode.plan_scheduling_detail_confirm.getCode());
        }
    }


    /**
     * 采购工作台-导出计划排程看板
     * @param response
     * @param queryParam
     * @throws IOException
     */
    @Override
    public void exportZcPlanScheduling(HttpServletResponse response, QueryParam<PlanSchedulingBoardParam> queryParam) throws IOException {
        List<PlanSchedulingBoardVo> planSchedulingBoardVos = deliveryPlanDetailMapper.selectZcPlanSchedulingBoardPage(null, queryParam);

        //计划明细状态的字典
        this.convertDetailStatus(planSchedulingBoardVos);


        //看板中的动态标题-展示当天开始的后29天，共30天的数据
        List<String> thirtyDayTitles = Lists.newArrayList();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate plusDay = now.plusDays(i);
            String plusDayStr = DateUtil.formatDate(plusDay);
            thirtyDayTitles.add(plusDayStr);
        }


        String fileName = "计划排程看板" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");


        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setWrapped(true);
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();

        List<List<String>> excelHeads = this.excelHead(thirtyDayTitles,"zc");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(excelHeads)
                .registerConverter(new DeliveryPlanDetailItemConverter("zc"))
                .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                .sheet("计划排程看板")
                .useDefaultStyle(true)
                .relativeHeadRowIndex(0)
                .doWrite(this.dataList(planSchedulingBoardVos,excelHeads,"zc"));

    }

    /**
     * 将计划明细的状态转换成中文
     * @param planSchedulingBoardVos
     */
    private void convertDetailStatus(List<PlanSchedulingBoardVo> planSchedulingBoardVos) {
        R<List<DictItemDTO>> detailStatusR = cfgParamResourceFeignClient.findDictItemListByCode("detail_status_enum");
        if (!detailStatusR.isSuccess() || CollectionUtil.isEmpty(detailStatusR.getData())) {
            log.info("获取送货计划明细的状态失败！字典={detail_status_enum}");
            throw new RuntimeException("获取送货计划明细的状态失败！");
        }

        Map<String, String> detailStatusMap = detailStatusR.getData().stream().collect(Collectors.toMap(DictItemDTO::getValue, DictItemDTO::getLabel));
        //计划明细状态的 value 转换成 lable
        Optional.ofNullable(planSchedulingBoardVos).ifPresent(planSchedulingBoardVoList->{
            planSchedulingBoardVoList.forEach(vo->{
                vo.setDetailStatus(detailStatusMap.get(vo.getDetailStatus()));
            });
        });
    }


    /**
     * 导出的excel的标题头
     * @param dateList
     * @param workBench zc or zy
     * @return
     */
    private List<List<String>> excelHead(List<String> dateList,String workBench) {
        List<String> titles = ListUtils.newArrayList();
        titles.add("送货计划单号");
        titles.add("计划明细状态");
        titles.add("产品编码");
        titles.add("产品名称");
        titles.add("条码");
        if ("zc".equals(workBench)) {
            titles.add("供应商编码");
            titles.add("供应商名称");

            titles.add("MRP需求数量");
            titles.add("在途数量");
            titles.add("净需求数量");
            titles.add("差异数");
            titles.add("预计到货日期");
        }else{
            titles.add("采购方编码");
            titles.add("采购方名称");
            titles.add("建议发货日期");
        }
        titles.add("采购方备注");
        titles.add("供应商备注");
        titles.addAll(dateList);
        List<List<String>> headList = ListUtils.newArrayList();
        titles.forEach(title -> {
            List<String> list = ListUtils.newArrayList();
            list.add(title);
            headList.add(list);
        });
        return headList;
    }


    /**
     * 导出的主数据（不创建对象）
     * @param planSchedulingBoardVos
     * @param headList
     * @return
     */
    private List<List<Object>> dataList(List<PlanSchedulingBoardVo> planSchedulingBoardVos, List<List<String>> headList,String workBench) {
        List<List<Object>> datas = ListUtils.newArrayList();
        if (CollectionUtil.isNotEmpty(planSchedulingBoardVos)) {
            String tip = "";
            if ("zc".equals(workBench)){

                tip = "计划数量" + "\n" + "确认数量" + "\n" + "建议发货日期" + "\n" + "差异原因";
            }else {
                tip = "送货数量" + "\n" + "确认数量" + "\n" + "预计到货日期" + "\n" + "差异原因";
            }
            String finalTip = tip;
            planSchedulingBoardVos.forEach(vo -> {
                List<Object> list = ListUtils.newArrayList();
                list.add(vo.getPlanNo());
                list.add(vo.getDetailStatus());
                list.add(vo.getProductCode());
                list.add(vo.getProductName());
                list.add(vo.getMerchantCode());
                if ("zc".equals(workBench)) {
                    list.add(vo.getSupplierCode());
                    list.add(vo.getSupplierName());

                    list.add(vo.getMrpPlanQuantity());
                    list.add(vo.getTransitQuantity());
                    list.add(vo.getNetDemand());
                    list.add(vo.getVarianceNumber());
                }else {
                    list.add(vo.getPurchaseCode());
                    list.add(vo.getPurchaseName());
                }


                list.add(finalTip);
                list.add(vo.getPurchaseRemark());
                list.add(vo.getSupplierRemark());
                for (int i = 0; i < 30; i++) {
                    list.add(" ");
                }
                //计划明细在当天到后面29天范围的项次
                if ("zc".equals(workBench)) {
                    List<DeliveryPlanDetailItem> items = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                            .between(DeliveryPlanDetailItem::getDeliveryDate, LocalDateTime.now().toLocalDate(), LocalDateTime.now().plusDays(29).toLocalDate())
                            .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, vo.getPlanDetailId()));
                    Optional.ofNullable(items).ifPresent(itemList -> {
                        itemList.forEach(item -> {
                            String deliveryDateStr = DateUtil.formatDate(item.getDeliveryDate().toLocalDate());
                            Integer index = this.getIndex(headList, deliveryDateStr);
                            list.add(index, item);
                        });
                    });
                }else {
                    List<DeliveryPlanDetailItem> items = planDetailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery()
                            .between(DeliveryPlanDetailItem::getSuggestedDeliveryDate, LocalDate.now(), LocalDate.now().plusDays(29))
                            .eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, vo.getPlanDetailId()));
                    Optional.ofNullable(items).ifPresent(itemList -> {
                        itemList.forEach(item -> {
                            String dateStr = DateUtil.formatDate(item.getSuggestedDeliveryDate());
                            Integer index = this.getIndex(headList, dateStr);
                            list.add(index, item);
                        });
                    });

                }
                datas.add(list);

            });
        }

        return datas;
    }


    /**
     * 根据内容获取到该数据所在的标题头的下标
     *
     * @param headList        导出的标题头
     * @param dStr 项次的日期的字符串形式
     * @return
     */
    private Integer getIndex(List<List<String>> headList, String dateStr) {
        Integer index = -1;
        if (CollectionUtil.isNotEmpty(headList) && StringUtil.isNotBlank(dateStr)) {
            for (int i = 0; i < headList.size(); i++) {
                boolean flag = dateStr.equals(headList.get(i).get(0));
                //理论上，查询的项次数据都必须在当天到后29天这个范围的数据
                if (flag) {
                    index = i;
                }
            }
        }
        return index;
    }


    /**
     * 销售工作台-导出计划排程看板
     * @param response
     * @param queryParam
     * @throws IOException
     */
    @Override
    public void exportZyPlanScheduling(HttpServletResponse response, QueryParam<PlanSchedulingBoardParam> queryParam) throws IOException {

        //供应商只能看到自己的计划明细
        @Valid PlanSchedulingBoardParam param = queryParam.getParam();
        if (ObjectUtil.isNotEmpty(param)) {
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                param.setSupplier(supplier.getSupplierCode());
            });
        }

        List<PlanSchedulingBoardVo> planSchedulingBoardVos = deliveryPlanDetailMapper.selectZyPlanSchedulingBoardPage(null, queryParam);

        //计划明细状态的字典
        this.convertDetailStatus(planSchedulingBoardVos);


        //看板中的动态标题-展示当天开始的后29天，共30天的数据
        List<String> thirtyDayTitles = Lists.newArrayList();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate plusDay = now.plusDays(i);
            String plusDayStr = DateUtil.formatDate(plusDay);
            thirtyDayTitles.add(plusDayStr);
        }


        String fileName = "计划排程看板" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");


        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setWrapped(true);
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();

        List<List<String>> excelHeads = this.excelHead(thirtyDayTitles,"zy");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(excelHeads)
                .registerConverter(new DeliveryPlanDetailItemConverter("zy"))
                .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                .sheet("计划排程看板")
                .useDefaultStyle(true)
                .relativeHeadRowIndex(0)
                .doWrite(this.dataList(planSchedulingBoardVos,excelHeads,"zy"));
    }


    /**
     * 获取上一版的所有项次
     * @param mrpComputerNo 版号
     * @return
     */
    @Override
    public Map<String,BigDecimal> getPreVersionItemDeliveryQuantity(String mrpComputerNo) {
        if(StrUtil.isBlank( mrpComputerNo)  ) {
            return new HashMap<>();
        }
        List<PreVersionDeliveryPlanDetailItemVo> preVersionDeliveryPlanDetailItemVoList = deliveryPlanMapper.selectPreVersionItem(mrpComputerNo);
        return preVersionDeliveryPlanDetailItemVoList.stream().collect(Collectors.groupingBy(PreVersionDeliveryPlanDetailItemVo::buildGroupKey, Collectors.mapping(PreVersionDeliveryPlanDetailItemVo::getDeliveryQuantity, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    /**
     * 全部确认
     * 1.对当前版本的所有计划排程下的
     * 2.【待确认】状态的计划明细，进行确认
     * 3.发送站内信，记录操作日志
     */
    @Override
    public void confirmAllPlanSchedulingDetail() {
        List<Long> latestVersionDeliveryPlanIds = this.getLatestVersionDeliveryPlan();
        Optional.of(latestVersionDeliveryPlanIds).ifPresent(deliveryPlanIds -> {
            deliveryPlanIds.forEach(deliveryPlanId -> {
                List<Long> deliveryPlanDetailIds = planDetailService.list(Wrappers.<DeliveryPlanDetail>lambdaQuery().eq(DeliveryPlanDetail::getDeliveryPlanId, deliveryPlanId)).stream().map(DeliveryPlanDetail::getId).collect(Collectors.toList());
                //对计划明细进行确认
                this.confirmPlanSchedulingDetail(deliveryPlanDetailIds);

            });
        });


    }
}
