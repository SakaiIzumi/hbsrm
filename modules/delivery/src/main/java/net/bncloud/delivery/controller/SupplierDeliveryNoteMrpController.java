package net.bncloud.delivery.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.delivery.param.DeliveryDetailBatchDetailParams;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryNoteServiceVS;
import net.bncloud.delivery.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * mrp按订单送货的送货单接口
 */
@RestController
@RequestMapping("/zy/delivery-note/mrp")
@RequiredArgsConstructor
@Slf4j
public class SupplierDeliveryNoteMrpController {


    private final DeliveryNoteServiceVS deliveryNoteService;

    private final DeliveryDetailService detailService;

    /**
     * 新增页 -- 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryNote")
    public R save(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveMrpDeliveryNote(deliveryNote));
    }

    /**
     * 1、新增页 -- 发布
     */
    @PostMapping("/saveSign")
    @ApiOperation(value = "确认发布", notes = "送货单")
    public R saveAndSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.mrpSaveAndSign(deliveryNote));
    }


    /**
     * 1、待签收编辑页 -- 发布 （会更新收料通知单）
     * 2、草稿详情编辑页 -- 保存（不创建收料通知单，因为无erpId）
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateById(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateMrpDeliveryNote(deliveryNote);
        return R.success();
    }

    /**
     * 草稿编辑页 -- 发布
     */
    @PutMapping("/updateSign")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.mrpUpdateSign(deliveryNote);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        deliveryNoteService.deleteMrpDeliveryNote(Long.parseLong(id));
        return R.success();
    }


    /**
     * 分批发货 --> 批次详情
     * @return
     */
    @GetMapping("deliveryDetail/batchDetail")
    public R<MrpDeliveryOrderDetailVo> getDeliveryDetailBatchDetail(@Validated DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams){
        return R.data(deliveryNoteService.getMrpDeliveryDetailBatchDetail(deliveryDetailBatchDetailParams));
    }

    /**
     * 分批发货 --> 批次详情保存
     * @return
     */
    @PostMapping("deliveryDetail/batchDetailSave")
    public R<DeliveryNoteVo> saveDeliveryDetailBatchDetailSave(@RequestBody @Validated @Valid DeliveryNoteSaveParam deliveryNoteSaveParam){
        DeliveryNoteVo deliveryNoteVo = deliveryNoteService.saveMrpDeliveryDetailBatchDetailSave(deliveryNoteSaveParam);
        return R.data( deliveryNoteVo);
    }

}
