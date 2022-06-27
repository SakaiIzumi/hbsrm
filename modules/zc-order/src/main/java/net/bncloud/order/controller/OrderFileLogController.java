package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;



import net.bncloud.order.service.IOrderFileLogService;
import net.bncloud.order.entity.OrderFileLog;
import net.bncloud.order.vo.OrderFileLogVo;
import net.bncloud.order.wrapper.OrderFileLogWrapper;

import java.util.*;


/**
 * <p>
 * 订单文件下载日志表 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zc/order/file-log")
public class OrderFileLogController {

    
    @Autowired
    private IOrderFileLogService iOrderFileLogService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入OrderFileLog")
    public R<OrderFileLog> getById(@PathVariable(value = "id") Long id){
        return R.data(iOrderFileLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入OrderFileLog")
    public R save(@RequestBody OrderFileLog orderFileLog){
        iOrderFileLogService.save(orderFileLog);
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
            iOrderFileLogService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入orderFileLog")
    public R updateById(@RequestBody OrderFileLog orderFileLog){
        iOrderFileLogService.updateById(orderFileLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入orderFileLog")
    public R list(@RequestBody OrderFileLog orderFileLog ){
        List<OrderFileLog> list = iOrderFileLogService.list(Condition.getQueryWrapper(orderFileLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入OrderFileLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<OrderFileLog> queryParam){
        final OrderFileLog param = queryParam.getParam();

        final IPage<OrderFileLog> page = iOrderFileLogService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param).lambda().orderByDesc(OrderFileLog::getCreatedDate));
		IPage<OrderFileLogVo> orderFileLogVoIPage = OrderFileLogWrapper.build().pageVO(page);
		return R.data(PageUtils.result(orderFileLogVoIPage));
    }





}
