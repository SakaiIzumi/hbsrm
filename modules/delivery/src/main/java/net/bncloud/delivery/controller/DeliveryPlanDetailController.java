package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.param.DeliveryPlanDetailParam;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.vo.DeliveryPlanDetailVo;
import net.bncloud.delivery.wrapper.DeliveryPlanDetailWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 计划明细
 */
@RestController
@RequestMapping("/delivery-plan-detail")
public class DeliveryPlanDetailController {

    
    @Autowired
    private DeliveryPlanDetailService deliveryPlanDetailService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryPlanDetail")
    public R<DeliveryPlanDetail> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryPlanDetailService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryPlanDetail")
    public R save(@RequestBody DeliveryPlanDetail deliveryPlanDetail){
        deliveryPlanDetailService.save(deliveryPlanDetail);
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
            deliveryPlanDetailService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryPlanDetail")
    public R updateById(@RequestBody DeliveryPlanDetail deliveryPlanDetail){
        deliveryPlanDetailService.updateById(deliveryPlanDetail);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryPlanDetail")
    public R list(@RequestBody DeliveryPlanDetail deliveryPlanDetail ){
        List<DeliveryPlanDetail> list = deliveryPlanDetailService.list(Condition.getQueryWrapper(deliveryPlanDetail));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入DeliveryPlanDetailParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryPlanDetailParam> pageParam){
        final DeliveryPlanDetailParam param = pageParam.getParam();

        final IPage<DeliveryPlanDetail> page = deliveryPlanDetailService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<DeliveryPlanDetailVo> deliveryPlanDetailVoIPage = DeliveryPlanDetailWrapper.build().pageVO(page);
		return R.data(PageUtils.result(deliveryPlanDetailVoIPage));
    }





}
