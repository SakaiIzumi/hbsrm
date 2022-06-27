package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.param.DeliveryDetailParam;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.DeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.PrintDataVo;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 送货明细表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/delivery-detail")
@Api(tags = "送货明细信息控制器")
public class DeliveryDetailController {

    
    @Autowired
    private DeliveryDetailService deliveryDetailService;


    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryDetail")
    public R<DeliveryDetail> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryDetailService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryDetail")
    public R save(@RequestBody DeliveryDetail deliveryDetail){
        deliveryDetailService.save(deliveryDetail);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        deliveryDetailService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryDetail")
    public R updateById(@RequestBody DeliveryDetail deliveryDetail){
        deliveryDetailService.updateById(deliveryDetail);
        return R.success();
    }

    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryDetail")
    public R list(@RequestBody DeliveryDetail deliveryDetail ){
        List<DeliveryDetail> list = deliveryDetailService.list(Condition.getQueryWrapper(deliveryDetail));
        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询列表", notes = "传入DeliveryDetailParam")
    public R<List<DeliveryDetail>> page(Pageable pageable, @RequestBody QueryParam<DeliveryDetailParam> queryParam){
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.getDeliveryDetailList(pageable, queryParam);
        return R.data(deliveryDetailList);
    }

    /**
     * 打印
     *
     * @param queryParam
     * @return
     */
    @PostMapping("/printPdf")
    @ApiOperation(value = "打印")
    public R<PrintDataVo<DeliveryDetailVo>> printPdf(@RequestBody QueryParam<DeliveryDetailParam> queryParam) {
        return R.data(deliveryDetailService.printData(queryParam));
    }






}
