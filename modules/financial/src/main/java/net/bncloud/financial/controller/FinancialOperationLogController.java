package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.param.FinancialOperationLogParam;
import net.bncloud.financial.service.FinancialOperationLogService;
import net.bncloud.financial.wrapper.AccountOperationLogWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 对账模块-单据操作记录日志接口
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/operation-log")
public class FinancialOperationLogController {


    @Autowired
    private FinancialOperationLogService financialOperationLogService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountOperationLog")
    public R<FinancialOperationLog> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialOperationLogService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountOperationLog")
    public R save(@RequestBody FinancialOperationLog financialOperationLog) {
        financialOperationLogService.save(financialOperationLog);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialOperationLogService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountOperationLog")
    public R updateById(@RequestBody FinancialOperationLog financialOperationLog) {
        financialOperationLogService.updateById(financialOperationLog);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountOperationLog")
    public R list(@RequestBody FinancialOperationLog financialOperationLog) {
        List<FinancialOperationLog> list = financialOperationLogService.list(Condition.getQueryWrapper(financialOperationLog));

        return R.data(list);
    }

    /**
     * 查询操作记录分页（传递类型参数）
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountOperationLogParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialOperationLogParam> pageParam) {
        final FinancialOperationLogParam param = pageParam.getParam();
        FinancialOperationLog operationLog = BeanUtil.copy(param, FinancialOperationLog.class);
        LambdaQueryWrapper<FinancialOperationLog> queryWrapper = Condition.getQueryWrapper(operationLog).lambda();
        queryWrapper.orderByDesc(FinancialOperationLog::getCreatedDate);
        final IPage<FinancialOperationLog> page = financialOperationLogService.page(PageUtils.toPage(pageable), queryWrapper);
        return R.data(PageUtils.result(AccountOperationLogWrapper.build().pageVO(page)));
    }


}
