package net.bncloud.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.param.DeliveryNoteParam;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.service.AttachmentRelService;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryNoteService;
import net.bncloud.delivery.service.DeliveryNoteSupplierService;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.DeliveryNoteVo;
import net.bncloud.delivery.wrapper.DeliveryNoteWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyh
 * @version 1.0.0
 * @description 送货单（销售工作台）
 * @since 2022/1/19
 */
@Deprecated
@RestController
@RequestMapping("/zy/delivery-note")
public class SupplierDeliveryNoteController {

    @Autowired
    private DeliveryNoteSupplierService deliveryNoteSupplierService;

    @Autowired
    private DeliveryNoteService deliveryNoteService;
    @Autowired
    private DeliveryDetailService detailService;

    /**
     * deliveryNoteService分页查询
     */
    /*@PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入DeliveryNoteParam")
    public R page(Pageable pageable, QueryParam<DeliveryNoteSupplierParam> pageParam) {
        IPage<DeliveryNoteSupplier> page = deliveryNoteSupplierService.selectPage(PageUtils.toPage(pageable), pageParam);
        IPage<DeliveryNoteSupplierVo> deliveryNoteVoPage = DeliveryNoteSupplierWrapper.build().pageVO(page);
        return R.data(PageUtils.result(deliveryNoteVoPage));
    }*/

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryNote")
    public R<DeliveryNoteVo> getById(@PathVariable(value = "id") Long id) {
        return R.data(deliveryNoteService.getDeliveryNoteInfo(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryNote")
    public R save(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveDeliveryNote(deliveryNote));
    }

    /**
     * 确认发布（把送货单状态改成待签收的状态）
     */
    @PostMapping("/saveSign")
    @ApiOperation(value = "确认发布", notes = "")
    public R saveAndSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveAndSign(deliveryNote));
    }


    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateById(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateDeliveryNote(deliveryNote);
        return R.success();
    }

    /**
     * 编辑页面的发布按钮，（集成update和updateStatus的功能）
     */
    @PutMapping("/updateSign")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateSign(deliveryNote);
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/updateV")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateByIdV2(Long id) {
        deliveryNoteService.updateDeliveryNoteV2(id);
        return R.success();
    }

    /**
     * 确认发货
     */
    @PutMapping("/updateStatus/{id}")
    @ApiOperation(value = "确认发货", notes = "")
    public R updateStatus(@PathVariable("id") Long id) {
        deliveryNoteService.updateStatus(id);
        return R.success();
    }


    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        deliveryNoteService.deleteDeliveryNote(Long.parseLong(id));
        return R.success();
    }

    @Autowired
    private AttachmentRelService attachmentRelService;


    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入DeliveryNoteParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryNoteParam> queryParam) {
        IPage<DeliveryNote> page = deliveryNoteService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<DeliveryNoteVo> deliveryNoteVoIPage = DeliveryNoteWrapper.build().pageVO(page);

        //设置送货明细数据
        deliveryNoteVoIPage.getRecords().forEach(deliveryNoteVo -> {
            List<DeliveryDetail> detailList = detailService.list(new LambdaQueryWrapper<DeliveryDetail>().eq(DeliveryDetail::getDeliveryId, deliveryNoteVo.getId()));
            List<DeliveryDetailVo> detailVoList = BeanUtil.copy(detailList, DeliveryDetailVo.class);
            //设置明细中的COA附件列表
            deliveryNoteService.batchSetDeliveryDetailAttachment(detailVoList);
            //设置 是否有COA附件 属性的值
            detailVoList.forEach(detail -> {
                if (detail.getAttachmentList().size() > 0)
                    detail.setAttachment("Y");
                else if (detail.getAttachmentList().size() <= 0)
                    detail.setAttachment("N");
            });
            deliveryNoteVo.setDeliveryDetailList(detailVoList);
        });

        deliveryNoteService.buildPermissionButtonBatch(deliveryNoteVoIPage.getRecords());
        return R.data(PageUtils.result(deliveryNoteVoIPage));
    }

    /**
     * 待签收数量统计
     *
     * @return
     */
    @GetMapping("/count")
    @ApiOperation(value = "送货单数量统计", notes = "")
    public R count() {
        return R.data(deliveryNoteService.selectToBeSignCount());
    }

    /**
     * 全部的数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "全部的数量统计", notes = "")
    public R statistics() {
        return R.data(deliveryNoteService.selectStatistics());
    }

    /**
     * 撤回送货
     */
    @PostMapping("/withdrawDelivery/{id}")
    @ApiOperation(value = "撤回送货", notes = "传入deliveryNote")
    public R withdrawDelivery(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.withdrawDelivery(id);
    }


    /**
     * 批量发货
     *
     * @return
     */
    @PostMapping("/batchShipment")
    @ApiOperation(value = "批量发货")
    public R<Long> batchShipment(@RequestBody List<String> itemIds) {
        return R.data(deliveryNoteService.batchDelivery(itemIds));
    }
}
