package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.constants.CommunicateCode;
import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.service.IOrderCommunicateLogService;
import net.bncloud.order.vo.OrderCommunicateLogVo;
import net.bncloud.order.wrapper.OrderCommunicateLogWrapper;
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
 * 订单答交日志表 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zc/order/communicate-log")
public class OrderCommunicateLogController {

    
    @Autowired
    private IOrderCommunicateLogService iOrderCommunicateLogService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入OrderCommunicateLog")
    public R<OrderCommunicateLog> getById(@PathVariable(value = "id") Long id){
        return R.data(iOrderCommunicateLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入OrderCommunicateLog")
    public R save(@RequestBody OrderCommunicateLog orderCommunicateLog){
        iOrderCommunicateLogService.save(orderCommunicateLog);
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
            OrderCommunicateLog orderCommunicateLog = iOrderCommunicateLogService.getById(Long.parseLong(id));
            if(CommunicateCode.DIFFERENCE_STATUS_SAVE.getCode() == orderCommunicateLog.getStatus()){
                iOrderCommunicateLogService.removeById(Long.parseLong(id));
            }else {
                throw new BizException(ResultCode.PARAM_VALID_ERROR);
            }
            
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入orderCommunicateLog")
    public R updateById(@RequestBody OrderCommunicateLog orderCommunicateLog){
        iOrderCommunicateLogService.updateById(orderCommunicateLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入orderCommunicateLog")
    public R list(@RequestBody OrderCommunicateLog orderCommunicateLog ){
        List<OrderCommunicateLog> list = iOrderCommunicateLogService.list(Condition.getQueryWrapper(orderCommunicateLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入OrderCommunicateLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<OrderCommunicateLog> queryParam){
        final OrderCommunicateLog param = queryParam.getParam();

        final IPage<OrderCommunicateLog> page = iOrderCommunicateLogService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param).lambda().orderByDesc(OrderCommunicateLog::getCreatedDate));
		IPage<OrderCommunicateLogVo> orderCommunicateLogVoIPage = OrderCommunicateLogWrapper.build().pageVO(page);

		return R.data(PageUtils.result(orderCommunicateLogVoIPage));
    }





}
