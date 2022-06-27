package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryOperationLog;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.enums.DeliveryStatusEnum;
import net.bncloud.delivery.enums.WorkBench;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.service.DeliveryOperationLogService;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.service.DeliveryPlanService;
import net.bncloud.delivery.vo.*;
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
import java.util.Optional;


/**
 * 送货计划2.0 (采购工作台)
 */
@Slf4j
@RestController
@RequestMapping("/zc/delivery-plan")
public class PurchaseDeliveryPlanController {
    private final DeliveryPlanService deliveryPlanService;
    private final DeliveryOperationLogService operationLogService;
    private final PurchaseOrderFeignClient purchaseOrderFeignClient;

    @Resource
    private DeliveryPlanDetailService deliveryPlanDetailService;
    @Resource
    private DeliveryPlanDetailItemService detailItemService;

    public PurchaseDeliveryPlanController(DeliveryPlanService deliveryPlanService, DeliveryOperationLogService operationLogService, PurchaseOrderFeignClient purchaseOrderFeignClient) {
        this.deliveryPlanService = deliveryPlanService;
        this.operationLogService = operationLogService;
        this.purchaseOrderFeignClient = purchaseOrderFeignClient;
    }

    /**
     * 批量发布
     *
     * @param ids
     * @return
     */
    @PostMapping("/sendBatch")
    @ApiOperation(value = "批量发送送货计划")
    public R sendBatch(@RequestBody List<String> ids) {
        deliveryPlanService.sendBatch(ids);
        return R.success();
    }

    /**
     * 发布
     * @param param
     * @return
     */
    @PostMapping("/publishDeliveryPlan")
    public R<String> publishDeliveryPlan(@RequestBody PublishDeliveryPlanParam param) {
        Optional.ofNullable(param.getPlanDescription()).ifPresent(remark->{
            deliveryPlanService.update(Wrappers.<DeliveryPlan>lambdaUpdate()
                    .set(DeliveryPlan::getPlanDescription, remark)
                    .eq(DeliveryPlan::getId, param.getDeliveryPlanId()));
        });
        deliveryPlanService.send(param.getDeliveryPlanId());
        return R.success("操作成功");

    }

    /**
     * 提醒（计划）
     *
     * @param id
     * @return
     */
    @GetMapping("/remind/{id}")
    @ApiOperation(value = "提醒供应商提醒送货计划")
    public R remind(@PathVariable("id") Long id) {
        deliveryPlanService.remind(id);
        return R.success();
    }

    /**
     * 分页查询计划的操作记录
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
        return R.data(deliveryPlanService.getStatisticsInfo(WorkBench.PURCHASE.getCode()));
    }


    /**
     * 根据计划id查询送货计划详情
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
     * 根据明细id查询送货批次
     *
     * @param id
     * @return
     */
    @GetMapping("/deliveryPlanDetailItemList/{id}")
    @ApiOperation(value = "根据明细id查询送货批次")
    @ResponseBody
    public R<List<DeliveryPlanDetailItem>> getDeliveryPlanDetailItemList(@PathVariable("id") String id) {
        R<List<DeliveryPlanDetailItem>> deliveryPlanDetailItemListR = deliveryPlanService.getDeliveryPlanDetailItemList(id);
        if (deliveryPlanDetailItemListR.isSuccess()&&CollectionUtil.isNotEmpty(deliveryPlanDetailItemListR.getData())){
            List<DeliveryPlanDetailItem> itemList = deliveryPlanDetailItemListR.getData();
            //采购方不能看到草稿状态的送货单
            itemList.forEach(item -> {
                if ("1".equals(item.getDeliveryStatus())) {
                    item.setDeliveryStatus("");
                    item.setDeliveryNoteNo("");
                }
                if (StringUtil.equals(item.getDeliveryStatus(), DeliveryStatusEnum.TO_BE_SIGNED.getCode())) {
                    item.setDeliveryStatus(DeliveryStatusEnum.TO_BE_SIGNED.getName());
                } else if (StringUtil.equals(item.getDeliveryStatus(), DeliveryStatusEnum.COMPLETED.getCode())) {
                    item.setDeliveryStatus(DeliveryStatusEnum.COMPLETED.getName());
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
        @Valid DeliveryPlanParam param = pageParam.getParam();
        if (param.getPlanEndDate() != null) {
            LocalDateTime planEndDate = param.getPlanEndDate();
            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((planEndDate.toLocalDate() + " 23:59:59"));
            param.setPlanEndDate(parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return R.data(deliveryPlanService.selectPage(PageUtils.toPage(pageable), pageParam, WorkBench.PURCHASE.getCode()));
    }

    /**
     * 分页查询计划明细
     */
    @PostMapping("/detailPage")
    @ApiOperation(value = "分页查询计划明细")
    @ResponseBody
    public R<PageImpl<PlanSchedulingDetailVo>> detailPage(Pageable pageable, @Valid @Validated @RequestBody QueryParam<DeliveryPlanDetailParam> pageParam) {
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
    public R<List<DeliveryPlanBoardVo>> getDeliveryPlanBoard(String dateStr, @Valid @Validated @RequestBody QueryParam<DeliveryPlanDetailItemParam> queryParam) {
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
    public R<PageImpl<DeliveryPlanDetailItemVo>> getDeliveryPlanBoardDetail(Pageable pageable, @Valid @Validated @RequestBody QueryParam<DeliveryPlanBoardParam> queryParam) {
        //采购方不能看到草稿状态下的送货单号
        IPage<DeliveryPlanDetailItemVo> result = deliveryPlanService.getDeliveryPlanBoardDetail(PageUtils.toPage(pageable), queryParam);
        result.getRecords()
                .forEach(item -> {
                    if ("1".equals(item.getDeliveryStatus())) {
                        item.setDeliveryNoteNo("");
                        item.setDeliveryStatus("");
                        item.setDeliveryNoteId("");
                    }
                });
        return R.data(PageUtils.result(result));
    }



    /**
     * 同步ERP计划
     */
    @GetMapping("syncDeliveryPlan")
    @ApiOperation(value = "同步ERP计划")
    public R syncDeliveryPlan() {
        purchaseOrderFeignClient.syncDeliveryPlan();
        return R.success();
    }


    /**
     * 新增或修改项次（保存）
     * @param items
     * @return
     */
    @PostMapping("/saveOrUpdateItem")
    public R<String> saveOrUpdateItem(List<DeliveryPlanDetailItem> items){
        deliveryPlanService.batchSaveOrUpdateItem(items);
        return R.success("操作成功");
    }

    /**
     * 删除项次
     * @param ids
     * @return
     */
    @PostMapping("/deleteItem")
    public R<String> deleteItem(@RequestBody List<Long> ids){
        deliveryPlanService.batchDeleteItem(ids);
        return R.success("操作成功");
    }

    /**
     * 差异确认（计划）
     * @param planId
     * @return
     */
    @GetMapping("/differenceConfirm/{planId}")
    public R<String> differenceConfirm(@PathVariable("planId") Long planId){
        deliveryPlanService.differenceConfirmById(planId);
        return R.success("操作成功");
    }

    /**
     * 采购工作台-计划排程看板
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
        return R.data(deliveryPlanService.getZcPlanSchedulingBoardPage(PageUtils.toPage(pageable), queryParam));
    }


    /**
     * 计划差异项详情
     * @param deliveryPlanDetailId
     * @return
     */
    @GetMapping("/getPlanSchedulingBoardDetail/{deliveryPlanDetailId}")
    public R<List<PlanVarianceItemDetailsVo>> getPlanSchedulingBoardDetail(@PathVariable("deliveryPlanDetailId") Long deliveryPlanDetailId) {
        List<DeliveryPlanDetailItem> itemList = detailItemService.list(Wrappers.<DeliveryPlanDetailItem>lambdaQuery().eq(DeliveryPlanDetailItem::getDeliveryPlanDetailId, deliveryPlanDetailId));
       List<PlanVarianceItemDetailsVo> result = Lists.newArrayList();
        Optional.ofNullable(itemList).ifPresent(items->{
            items.forEach(item -> {
                PlanVarianceItemDetailsVo planVarianceItemDetailsVo = new PlanVarianceItemDetailsVo().setDeliveryDate(item.getDeliveryDate().toLocalDate())
                        .setDeliveryQuantity(item.getDeliveryQuantity())
                        .setConfirmQuantity(item.getConfirmQuantity())
                        .setVarianceNumber(item.getVarianceNumber() + "")
                        .setDifferenceReason(item.getDifferenceReason());
                result.add(planVarianceItemDetailsVo);

            });
        });
        return R.data(result);
    }


    /**
     * 发布计划排程明细
     * @return
     */
    @PostMapping("/batchConfirmPlanDetail")
    public R<String> batchPublishPlanDetail(){
        deliveryPlanService.publishPlanScheduling();
        return R.success("操作成功");
    }

    /**
     * 提醒（计划明细）
     * 1、发送消息
     * @param planDetailIds
     * @return
     */
    @PostMapping("/batchRemindPlanDetail")
    public R<String> batchRemindPlanDetail(@RequestBody List<Long> planDetailIds) {
        deliveryPlanService.remindPlanScheduling(planDetailIds);
        return R.success("操作成功");
    }

    /**
     * 差异确认（计划明细）
     * @param planDetailIds
     * @return
     */
    @PostMapping("/differenceConfirmPlanDetail")
    public R<String> differenceConfirmPlanDetail(@RequestBody List<Long> planDetailIds) {
        deliveryPlanService.differenceConfirmPlanScheduling(planDetailIds);
        return R.success("操作成功");
    }

    /**
     * 导出计划排程看板
     * @param queryParam
     * @return
     */
    @PostMapping("/exportPlanSchedulingBoard")
    public void exportPlanSchedulingBoard(HttpServletResponse response,@RequestBody QueryParam<PlanSchedulingBoardParam> queryParam){
        try {
            @Valid PlanSchedulingBoardParam param = queryParam.getParam();
            String[] split = param.getStatus().split(",");
            List<String> status = Arrays.asList(split);
            param.setStateList(status);
            deliveryPlanService.exportZcPlanScheduling(response,queryParam);
        } catch (IOException e) {
            log.info("导出计划排程看板失败",e);
        }
    }

    /**
     * 更新采购方备注
     */
    @PostMapping("/updateRemark")
    public void updatePlanSchedulingDetailRemark(@RequestBody @Validated @Valid UpdateRemarkParam updateRemarkParam) {
        deliveryPlanDetailService.update(Wrappers.<DeliveryPlanDetail>lambdaUpdate()
                .set(DeliveryPlanDetail::getPurchaseRemark,updateRemarkParam.getRemark())
        .eq(DeliveryPlanDetail::getId,updateRemarkParam.getDeliveryPlanDetailId()));
    }
}