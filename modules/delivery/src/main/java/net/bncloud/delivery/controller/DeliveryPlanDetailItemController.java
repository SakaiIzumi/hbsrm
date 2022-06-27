package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.param.DeliveryPlanDetailItemParam;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.vo.DeliveryPlanDetailItemVo;
import net.bncloud.delivery.wrapper.DeliveryPlanDetailItemWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 送货计划明细批次表 前端控制器
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@RestController
@RequestMapping("/delivery-plan-detail-item")
public class DeliveryPlanDetailItemController {

    
    @Autowired
    private DeliveryPlanDetailItemService deliveryPlanDetailItemService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryPlanDetailItem")
    public R<DeliveryPlanDetailItem> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryPlanDetailItemService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryPlanDetailItem")
    public R save(@RequestBody DeliveryPlanDetailItem deliveryPlanDetailItem){
        deliveryPlanDetailItemService.save(deliveryPlanDetailItem);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids){
        String[] idsStrs = ids.split(",");
        for (String id:idsStrs){
            deliveryPlanDetailItemService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryPlanDetailItem")
    public R updateById(@RequestBody DeliveryPlanDetailItem deliveryPlanDetailItem){
        deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryPlanDetailItem")
    public R list(@RequestBody DeliveryPlanDetailItem deliveryPlanDetailItem ){
        List<DeliveryPlanDetailItem> list = deliveryPlanDetailItemService.list(Condition.getQueryWrapper(deliveryPlanDetailItem));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入DeliveryPlanDetailItemParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryPlanDetailItemParam> pageParam){
        final DeliveryPlanDetailItemParam param = pageParam.getParam();

        final IPage<DeliveryPlanDetailItem> page = deliveryPlanDetailItemService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<DeliveryPlanDetailItemVo> deliveryPlanDetailItemVoIPage = DeliveryPlanDetailItemWrapper.build().pageVO(page);
		return R.data(PageUtils.result(deliveryPlanDetailItemVoIPage));
    }





}
