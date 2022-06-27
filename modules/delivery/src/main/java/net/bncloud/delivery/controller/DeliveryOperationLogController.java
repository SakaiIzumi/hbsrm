package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.DeliveryOperationLog;
import net.bncloud.delivery.param.DeliveryOperationLogParam;
import net.bncloud.delivery.service.DeliveryOperationLogService;
import net.bncloud.delivery.vo.DeliveryOperationLogVo;
import net.bncloud.delivery.wrapper.DeliveryOperationLogWrapper;
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
import java.util.Optional;


/**
 * <p>
 * 送货通知操作记录信息表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/delivery-operation-log")
@Api(tags = "送货通知操作记录控制器")
public class DeliveryOperationLogController {

    
    @Autowired
    private DeliveryOperationLogService deliveryOperationLogService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryOperationLog")
    public R<DeliveryOperationLog> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryOperationLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryOperationLog")
    public R save(@RequestBody DeliveryOperationLog deliveryOperationLog){
        deliveryOperationLogService.save(deliveryOperationLog);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        deliveryOperationLogService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryOperationLog")
    public R updateById(@RequestBody DeliveryOperationLog deliveryOperationLog){
        deliveryOperationLogService.updateById(deliveryOperationLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryOperationLog")
    public R list(@RequestBody DeliveryOperationLog deliveryOperationLog ){
        List<DeliveryOperationLog> list = deliveryOperationLogService.list(Condition.getQueryWrapper(deliveryOperationLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询列表", notes = "传入DeliveryOperationLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryOperationLogParam> queryParam){
        final DeliveryOperationLogParam param = queryParam.getParam( );
        DeliveryOperationLog deliveryOperationLog  = BeanUtil.copy(param, DeliveryOperationLog.class);
        LambdaQueryWrapper<DeliveryOperationLog> queryWrapper = Condition.getQueryWrapper(deliveryOperationLog).lambda();
        queryWrapper.orderByDesc(DeliveryOperationLog::getCreatedDate);
        final IPage<DeliveryOperationLog> page = deliveryOperationLogService.page(PageUtils.toPage(pageable), queryWrapper);
		IPage<DeliveryOperationLogVo> deliveryOperationLogVoIPage = DeliveryOperationLogWrapper.build().pageVO(page);
		return R.data(PageUtils.result(deliveryOperationLogVoIPage));
    }





}
