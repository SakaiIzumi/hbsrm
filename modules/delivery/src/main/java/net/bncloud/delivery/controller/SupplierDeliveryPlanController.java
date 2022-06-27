package net.bncloud.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryOperationLog;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.enums.DeliveryStatusEnum;
import net.bncloud.delivery.enums.DetailStatusEnum;
import net.bncloud.delivery.enums.WorkBench;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.service.DeliveryOperationLogService;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.service.DeliveryPlanService;
import net.bncloud.delivery.vo.*;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.utils.AuthUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划（销售工作台）
 * @since 2022/1/19
 */

/**
 * 送货计划2.0（销售工作台）
 */
@Slf4j
@RestController
@RequestMapping("/zy/delivery-plan")
public class SupplierDeliveryPlanController {
    private final DeliveryPlanService deliveryPlanService;
    private final DeliveryOperationLogService operationLogService;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;

    @Resource
    private DeliveryPlanDetailService planDetailService;

    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    public SupplierDeliveryPlanController(DeliveryPlanService deliveryPlanService, DeliveryOperationLogService operationLogService) {
        this.deliveryPlanService = deliveryPlanService;
        this.operationLogService = operationLogService;
    }


    /**
     * 分页查询计划的操作日志
     *
     * @param pageable page=1&size=10
     * @param billId   计划id
     * @return
     */
    @PostMapping("/operationLog")
    @ApiOperation(value = "分页查询操作日志")
    @ResponseBody
    public R<PageImpl<DeliveryOperationLog>> getOperationLog(Pageable pageable, @RequestParam("billId") String billId) {
        LambdaQueryWrapper<DeliveryOperationLog> queryWrapper = new LambdaQueryWrapper<DeliveryOperationLog>().eq(DeliveryOperationLog::getBillId, billId);
        return R.data(PageUtils.result(operationLogService.page(PageUtils.toPage(pageable), queryWrapper)));
    }

    /**
     * 送货计划状态数量统计
     *
     * @return
     */
    @GetMapping("/count")
    @ApiOperation(value = "送货计划状态数量统计")
    @ResponseBody
    public R<DeliveryPlanStatisticsVo> getStatisticsInfo() {
        return R.data(deliveryPlanService.getStatisticsInfo(WorkBench.SUPPLIER.getCode()));
    }


    /**
     * 确认送货计划
     *
     * @param id 计划id
     * @return
     */
    @GetMapping("/confirm/{id}")
    @ApiOperation(value = "确认")
    public R confirm(@PathVariable("id") Long id) {
        deliveryPlanService.confirm(id);
        return R.success();
    }


    /**
     * 全部确认（计划明细）
     * @return
     */
    @GetMapping("/confirmAll")
    public R<String> confirmAll() {
        deliveryPlanService.confirmAllPlanSchedulingDetail();
        return R.success("操作成功");
    }


    /**
     * 修改项次
     * @param itemList
     * @return
     */
    @PostMapping("/updateItem")
    public R updateItem(@RequestBody List<DeliveryPlanDetailItem> itemList) {
        deliveryPlanService.batchUpdateItem(itemList);
        return R.success();
    }

    /**
     * 查询送货计划详情
     *
     * @param
     * @param id
     * @return
     */
    @GetMapping("/planDetail/{id}")
    @ApiOperation(value = "查询送货计划详情")
    @ResponseBody
    public R<DeliveryPlan> getDeliveryPlanInfo(@PathVariable("id") String id) {
        return R.data(deliveryPlanService.getDeliveryPlanInfo(id));
    }

    /**
     * 查询送货批次
     *
     * @param id 计划明细id
     * @return
     */
    @GetMapping("/deliveryPlanDetailItemList/{id}")
    @ApiOperation(value = "查询送货项次")
    @ResponseBody
    public R<List<DeliveryPlanDetailItem>> getDeliveryPlanDetailItemList(@PathVariable("id") String id) {
        //计划明细
        DeliveryPlanDetail deliveryPlanDetail = planDetailService.getById(id);
        String detailStatus = deliveryPlanDetail.getDetailStatus();

        //送货计划
        DeliveryPlan deliveryPlan = deliveryPlanService.getById(deliveryPlanDetail.getDeliveryPlanId());

        R<List<DeliveryPlanDetailItem>> deliveryPlanDetailItemListR = deliveryPlanService.getDeliveryPlanDetailItemList(id);

        Map<String, Boolean> map = Maps.newConcurrentMap();
        deliveryPlanDetailItemListR.setOperationButtons(map);

        if (deliveryPlanDetailItemListR.isSuccess() && CollectionUtil.isNotEmpty(deliveryPlanDetailItemListR.getData())) {
            //获取组织id
            Long orgId = deliveryPlanService.getOrgIdByPurchaseCodeAndSupplierCode(deliveryPlan.getPurchaseCode(), deliveryPlan.getSupplierCode());

            //送货协同方式中的：是否开启计划排程
            boolean enablePlanScheduling = deliveryPlanService.getMrpAutoSendCfg();

            //供应商是否可以差异答复
            R<CfgParamInfo> supplierDiscrepancyReplyCfgParam = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.DELIVERY_SUPPLIER_DISCREPANCY_REPLY, orgId);
            Asserts.isTrue(supplierDiscrepancyReplyCfgParam.isSuccess() && supplierDiscrepancyReplyCfgParam.getData() != null, "获取供应商是否可以差异答复的配置失败！");

            boolean supplierDiscrepancyReply = Boolean.parseBoolean(supplierDiscrepancyReplyCfgParam.getData().getValue());


            //供应商是否可以编辑的前提：【送货协同方式开启送货排程】 、【计划明细状态是待确认】 和 【供应商可以差异答复】
            if (enablePlanScheduling && supplierDiscrepancyReply && DetailStatusEnum.TO_BE_CONFIRM.getCode().equals(detailStatus)) {
                deliveryPlanDetailItemListR.getOperationButtons().put("edit", true);
            }

            //供应商可以看到所有状态的送货单
            List<DeliveryPlanDetailItem> itemList = deliveryPlanDetailItemListR.getData();
            itemList.forEach(item -> {
                if (StringUtil.equals(item.getDeliveryStatus(), DeliveryStatusEnum.TO_BE_SIGNED.getCode())) {
                    item.setDeliveryStatus(DeliveryStatusEnum.TO_BE_SIGNED.getName());
                } else if (StringUtil.equals(item.getDeliveryStatus(), DeliveryStatusEnum.COMPLETED.getCode())) {
                    item.setDeliveryStatus(DeliveryStatusEnum.COMPLETED.getName());
                } else if (StringUtil.equals(item.getDeliveryStatus(), DeliveryStatusEnum.DRAFT.getCode())) {
                    item.setDeliveryStatus(DeliveryStatusEnum.DRAFT.getName());
                }
            });
        }
        return deliveryPlanDetailItemListR;
    }





    /**
     * 分页查询送货计划
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询送货计划")
    @ResponseBody
    public R<PageImpl<DeliveryPlan>> page(Pageable pageable, @Validated @Valid @RequestBody QueryParam<DeliveryPlanParam> pageParam) throws ParseException {
        DeliveryPlanParam param = pageParam.getParam();
        param.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        if (param.getPlanEndDate() != null) {
            LocalDateTime planEndDate = param.getPlanEndDate();
            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((planEndDate.toLocalDate() + " 23:59:59"));
            param.setPlanEndDate(parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return R.data(deliveryPlanService.selectPage(PageUtils.toPage(pageable), pageParam, WorkBench.SUPPLIER.getCode()));
    }

    /**
     * 分页查询计划明细
     */
    @PostMapping("/detailPage")
    @ApiOperation(value = "分页查询计划明细")
    @ResponseBody
    public R<PageImpl<PlanSchedulingDetailVo>> detailPage(Pageable pageable, @Validated @Valid @RequestBody QueryParam<DeliveryPlanDetailParam> pageParam) {
        return R.data(PageUtils.result(deliveryPlanService.getDeliveryPlanDetailList(pageable, pageParam)));
    }


    /**
     * 查询计划看板
     *
     * @param dateStr
     * @param queryParam
     * @return
     * @throws Exception
     */
    @PostMapping("/deliveryPlanBoard")
    @ApiOperation(value = "查询计划看板")
    @ResponseBody
    public R<List<DeliveryPlanBoardVo>> getDeliveryPlanBoard(String dateStr, @Validated @Valid @RequestBody QueryParam<DeliveryPlanDetailItemParam> queryParam) {
        DeliveryPlanDetailItemParam param = queryParam.getParam();
        param.setSupplierCondition(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        return R.data(deliveryPlanService.getDeliveryPlanBoard(dateStr, queryParam));
    }

    /**
     * 查询计划看板详情
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/deliveryPlanBoardDetail")
    @ApiOperation(value = "查询计划看板详情")
    @ResponseBody
    public R<PageImpl<DeliveryPlanDetailItemVo>> getDeliveryPlanBoardDetail(Pageable pageable, @Validated @Valid @RequestBody QueryParam<DeliveryPlanBoardParam> queryParam) {
        queryParam.getParam().setSupplierCondition(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        return R.data(PageUtils.result(deliveryPlanService.getDeliveryPlanBoardDetail(PageUtils.toPage(pageable), queryParam)));
    }


    /**
     * 打印
     *
     * @param queryParam
     * @return
     */
    @PostMapping("/printPdf")
    @ApiOperation(value = "打印")
    @ResponseBody
    public R<PrintDataVo<DeliveryPlanDetailItemVo>> printPdf(@Validated @Valid @RequestBody QueryParam<DeliveryPlanBoardParam> queryParam) {
        return R.data(deliveryPlanService.printData(queryParam));
    }


    /**
     * 按计划送货
     *
     * @param pageable   page=1&size=10
     * @param queryParam
     * @return
     */
    @PostMapping("/deliveryAsPlan")
    @ApiOperation(value = "按计划送货")
    @ResponseBody
    public R<PageImpl<DeliveryPlanDetailItemVo>> deliveryAsPlan(Pageable pageable, @Validated @Valid @RequestBody QueryParam<DeliveryAsPlanParam> queryParam) {
        @Valid DeliveryAsPlanParam param = queryParam.getParam();
        param.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        if (param.getDeliveryTimeEnd() != null) {
            String deliveryTimeEnd = param.getDeliveryTimeEnd();
            String replace = deliveryTimeEnd.replace("00:00:00","23:59:59");
            param.setDeliveryTimeEnd(replace);
        }
        return R.data(PageUtils.result(deliveryPlanService.getDeliveryAsPlanList(PageUtils.toPage(pageable), queryParam)));
    }


    /**
     * 销售工作台-计划排程看板
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/getPlanSchedulingBoard")
    public R<PageImpl<PlanSchedulingBoardVo>> getPlanSchedulingBoard(Pageable pageable,@RequestBody QueryParam<PlanSchedulingBoardParam> queryParam){
        @Valid PlanSchedulingBoardParam param = queryParam.getParam();
        String[] split = param.getStatus().split(",");
        List<String> status = Arrays.asList(split);
        param.setStateList(status);
        return R.data(deliveryPlanService.getZyPlanSchedulingBoardPage(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 确认计划排程明细(部分确认)
     * @param deliveryPlanIds
     * @return
     */
    @PostMapping("/confirmPlanSchedulingDetail")
    public R<String> confirmPlanSchedulingDetail(@RequestBody List<Long> deliveryPlanIds){
        deliveryPlanService.confirmPlanSchedulingDetail(deliveryPlanIds);
        return R.success("操作成功");
    }

    /**
     * 保存项次
     * @param itemList
     * @return
     */
    @PostMapping("/savePlanSchedulingDetailItem")
    public R<String> savePlanSchedulingDetailItem(@RequestBody List<PlanSchedulingDetailItem> itemList) {
        deliveryPlanService.batchSavePlanSchedulingDetailItem(itemList);
        return R.success("操作成功");
    }


    /**
     * 销售工作台-导出排程看板
     * @param response
     * @param queryParam
     */
    @PostMapping("/exportPlanSchedulingBoard")
    public void exportPlanSchedulingBoard(HttpServletResponse response, @RequestBody QueryParam<PlanSchedulingBoardParam> queryParam){
        try {
            @Valid PlanSchedulingBoardParam param = queryParam.getParam();
            String[] split = param.getStatus().split(",");
            List<String> status = Arrays.asList(split);
            param.setStateList(status);
            deliveryPlanService.exportZyPlanScheduling(response,queryParam);
        } catch (IOException e) {
            log.info("导出计划排程看板失败",e);
        }
    }


    /**
     * 更新供应商备注
     */
    @PostMapping("/updateRemark")
    public void updatePlanSchedulingDetailRemark(@RequestBody @Validated @Valid UpdateRemarkParam updateRemarkParam) {
        planDetailService.update(Wrappers.<DeliveryPlanDetail>lambdaUpdate()
                .set(DeliveryPlanDetail::getSupplierRemark,updateRemarkParam.getRemark())
                .eq(DeliveryPlanDetail::getId,updateRemarkParam.getDeliveryPlanDetailId()));
    }
}
