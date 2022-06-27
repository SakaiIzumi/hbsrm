package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.entity.OrderChangeLog;
import net.bncloud.order.service.IOrderChangeLogService;
import net.bncloud.order.vo.OrderChangeLogVo;
import net.bncloud.order.wrapper.OrderChangeLogWrapper;
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
 * 修改日志表 前端控制器
 * </p>
 *
 * @author 吕享1义
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zc/order/change-log")
public class OrderChangeLogController {


    @Autowired
    private IOrderChangeLogService iOrderChangeLogService;


    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入OrderChangeLog")
    public R<OrderChangeLog> getById(@PathVariable(value = "id") Long id){
        return R.data(iOrderChangeLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入OrderChangeLog")
    public R save(@RequestBody OrderChangeLog orderChangeLog){
        iOrderChangeLogService.save(orderChangeLog);
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
            iOrderChangeLogService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入orderChangeLog")
    public R updateById(@RequestBody OrderChangeLog orderChangeLog){
        iOrderChangeLogService.updateById(orderChangeLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入orderChangeLog")
    public R list(@RequestBody OrderChangeLog orderChangeLog ){
        List<OrderChangeLog> list = iOrderChangeLogService.list(Condition.getQueryWrapper(orderChangeLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入OrderChangeLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<OrderChangeLog> queryParam){
        final OrderChangeLog param = queryParam.getParam();

        final IPage<OrderChangeLog> page = iOrderChangeLogService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param).lambda().orderByDesc(OrderChangeLog::getCreatedDate));
		IPage<OrderChangeLogVo> orderChangeLogVoIPage = OrderChangeLogWrapper.build().pageVO(page);

		return R.data(PageUtils.result(orderChangeLogVoIPage));
    }





}
