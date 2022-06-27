package net.bncloud.oem.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.param.PurchaseOrderParam;
import net.bncloud.oem.domain.param.PurchaseOrderReceivingParam;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.domain.vo.ReceivingRecordsVo;
import net.bncloud.oem.domain.vo.ReturnedReceiptsVo;
import net.bncloud.oem.service.OperationLogService;
import net.bncloud.oem.service.PurchaseOrderReceivingService;
import net.bncloud.oem.service.PurchaseOrderService;
import net.bncloud.oem.service.ReceivingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 销售工作台-OEM甲供物料
 */
@Slf4j
@RestController
@RequestMapping("/zy/purchase-order")
public class SupplierPurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    @Lazy
    private PurchaseOrderReceivingService receivingService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private ReceivingAddressService receivingAddressService;


    /**
     * 已退回状态的数量
     *
     * @return
     */
    @GetMapping("/statistics")
    public R<Integer> statistics() {
        return R.data(receivingService.statisticsReturnedQuantity());
    }

    /**
     * 分页查询采购订单
     * 数据过滤：当前登录的OEM供应商只能看到自己的数据
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<PurchaseOrderVo>> page(Pageable pageable, @RequestBody QueryParam<PurchaseOrderParam> queryParam) {
        SecurityUtils.getCurrentSupplier().ifPresent(currentSupplier -> {
            queryParam.getParam().setOemSupplier(currentSupplier.getSupplierCode());
        });
        return R.data(purchaseOrderService.selectOemSupplierPage(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 被退回
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/returnedReceipts")
    public R<PageImpl<ReturnedReceiptsVo>> getReturnedReceipts(Pageable pageable, @RequestBody QueryParam<PurchaseOrderParam> queryParam) {
        SecurityUtils.getCurrentSupplier().ifPresent(currentSupplier -> {
            queryParam.getParam().setOemSupplier(currentSupplier.getSupplierCode());
        });
        return R.data(purchaseOrderService.getReturnedReceiptsPage(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 查询采购订单详情
     *
     * @param id 采购订单id
     * @return
     */
    @GetMapping("/getInfoById/{id}")
    public R<PurchaseOrderVo> getInfoById(@PathVariable("id") Long id) {
        return R.data(purchaseOrderService.getOrderInfo(id));
    }

    /**
     * 获取订单下面的所有收货
     *
     * @param id 采购订单id
     * @return
     */
    @GetMapping("/getReceivingInfo/{id}")
    public R<List<ReceivingRecordsVo>> getReceivingInfo(@PathVariable("id") Long id) {
        return R.data(purchaseOrderService.getReceivingByOrderId(id));
    }

    /**
     * 订单收货页面
     *
     * @param id 采购订单id
     * @return
     */
    @GetMapping("/getOrderReceiptInfo/{id}")
    public R<PurchaseOrderVo> getOrderReceiptInfo(@PathVariable("id") Long id) {
        return R.data(purchaseOrderService.getOrderReceipt(id));
    }

    /**
     * 批量编辑页面
     *
     * @param ids 收货id集合
     * @return
     */
    @PostMapping("/batchEditPage")
    public R<List<ReturnedReceiptsVo>> batchEditPage(@RequestBody List<Long> ids) {
        return R.data(purchaseOrderService.batchEditPageList(ids));
    }

    /**
     * 提交（批量保存/批量编辑）收货
     *
     * @param params 收货信息
     * @return
     */
    @PostMapping("/batchSave")
    public R<String> batchSave(@RequestBody List<PurchaseOrderReceivingParam> params) {
        try {
            purchaseOrderService.batchSaveReceiving(params);
        } catch (Exception e) {
            log.info(JSON.toJSONString(e));
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }


    /**
     * 批量删除收货
     *
     * @param ids 收货id
     * @return
     */
    @PostMapping("/batchDelete")
    public R<String> batchDelete(@RequestBody List<Long> ids) {
        try {
            purchaseOrderService.batchDeleteReceiving(ids);
        } catch (Exception e) {
            log.info(JSON.toJSONString(e));
            return R.fail(JSON.toJSONString(e));
        }
        return R.success("操作成功");
    }


    /**
     * 某一采购订单下的操作记录
     *
     * @param pageable        page=1&size=10
     * @param purchaseOrderId 采购订单id
     * @return
     */
    @PostMapping("/getOperationLogs")
    public R<PageImpl<OperationLog>> getOperationLogs(Pageable pageable, @RequestParam("purchaseOrderId") Long purchaseOrderId) {
        IPage<OperationLog> iPage = operationLogService.page(PageUtils.toPage(pageable)
                , Wrappers.<OperationLog>lambdaQuery().eq(OperationLog::getBillId, purchaseOrderId));
        return R.data(PageUtils.result(iPage));
    }

}
