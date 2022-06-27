package net.bncloud.delivery.controller;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.delivery.entity.OrderDeliverySupplier;
import net.bncloud.delivery.param.OrderDeliverySupplierParam;
import net.bncloud.delivery.service.OrderDeliverySupplierService;
import net.bncloud.delivery.vo.OrderDeliverySupplierVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 排程供应商接口
 *
 * @author ddh
 * @description
 * @since 2022/5/23
 */
@RestController
@RequestMapping("/order-delivery-supplier")
public class OrderDeliverySupplierController {

    private final OrderDeliverySupplierService orderDeliverySupplierService;

    public OrderDeliverySupplierController(OrderDeliverySupplierService orderDeliverySupplierService) {
        this.orderDeliverySupplierService = orderDeliverySupplierService;
    }


    /**
     * 排程供应商分页查询
     *
     * @param pageable
     * @param param
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<OrderDeliverySupplierVo>> page(Pageable pageable, @RequestBody QueryParam<OrderDeliverySupplierParam> param) {
        return R.data(orderDeliverySupplierService.selectPage(PageUtils.toPage(pageable), param));
    }

    /**
     * 排程供应商批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchDelete")
    public R<String> batchDelete(@RequestBody List<Long> ids) {
        orderDeliverySupplierService.deleteBatch(ids);
        return R.success("操作成功");
    }

    /**
     * 排程供应商保存
     *
     * @return
     */
    @PostMapping("/batchSave")
    public R<String> batchSave(@RequestBody OrderDeliverySupplierParam param) {
        orderDeliverySupplierService.saveSuppliers(param);
        return R.success("操作成功");
    }

    /**
     * 失效
     *
     * @param id
     * @return
     */
    /*@GetMapping("/changeStatus/{id}")
    public R<String> changeStatus(@PathVariable("id") @NotNull Long id) {
        OrderDeliverySupplier orderDeliverySupplier = orderDeliverySupplierService.getById(id);
        if (ObjectUtil.isNotEmpty(orderDeliverySupplier)) {
            orderDeliverySupplier.setStatus(!orderDeliverySupplier.getStatus());
            orderDeliverySupplierService.updateById(orderDeliverySupplier);
        }
        return R.success("操作成功");
    }*/

    /**
     * 当前供应商是否是按排程送货的供应商  (按订单送货按钮是否显示)
     * @return
     */
    @GetMapping("/isSchedulingSupplier")
    public R<Boolean> isSchedulingSupplier(){
        return R.data(orderDeliverySupplierService.isOrderDeliverySupplier());
    }
}
