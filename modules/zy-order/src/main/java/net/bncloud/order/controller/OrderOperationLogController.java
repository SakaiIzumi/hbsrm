package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.entity.OrderOperationLog;
import net.bncloud.order.service.IOrderOperationLogService;
import net.bncloud.order.vo.OrderOperationLogVo;
import net.bncloud.order.wrapper.OrderOperationLogWrapper;
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
 * 订单操作记录表 前端控制器
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zy/order/operation-log")
public class OrderOperationLogController {

    
    @Autowired
    private IOrderOperationLogService iOrderOperationLogService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入OrderOperationLog")
    public R<OrderOperationLog> getById(@PathVariable(value = "id") Long id){
        return R.data(iOrderOperationLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入OrderOperationLog")
    public R save(@RequestBody OrderOperationLog orderOperationLog){
        iOrderOperationLogService.save(orderOperationLog);
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
            iOrderOperationLogService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入orderOperationLog")
    public R updateById(@RequestBody OrderOperationLog orderOperationLog){
        iOrderOperationLogService.updateById(orderOperationLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入orderOperationLog")
    public R list(@RequestBody OrderOperationLog orderOperationLog ){
        List<OrderOperationLog> list = iOrderOperationLogService.list(Condition.getQueryWrapper(orderOperationLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入OrderOperationLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<OrderOperationLog> queryParam){
        final OrderOperationLog param = queryParam.getParam();

        final IPage<OrderOperationLog> page = iOrderOperationLogService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param).lambda().orderByDesc(OrderOperationLog::getCreatedDate));
		IPage<OrderOperationLogVo> orderOperationLogVoIPage = OrderOperationLogWrapper.build().pageVO(page);
		return R.data(PageUtils.result(orderOperationLogVoIPage));
    }





}
