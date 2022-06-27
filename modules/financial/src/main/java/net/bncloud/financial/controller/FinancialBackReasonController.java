package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialBackReason;
import net.bncloud.financial.param.FinancialBackReasonParam;
import net.bncloud.financial.service.FinancialBackReasonService;
import net.bncloud.financial.vo.FinancialBackReasonVo;
import net.bncloud.financial.wrapper.AccountBackReasonWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 对账模块-对账单退回原因
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/back-reason")
public class FinancialBackReasonController {


    @Autowired
    private FinancialBackReasonService financialBackReasonService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountBackReason")
    public R<FinancialBackReason> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialBackReasonService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountBackReason")
    public R save(@RequestBody FinancialBackReason financialBackReason) {
        financialBackReasonService.save(financialBackReason);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialBackReasonService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountBackReason")
    public R updateById(@RequestBody FinancialBackReason financialBackReason) {
        financialBackReasonService.updateById(financialBackReason);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountBackReason")
    public R list(@RequestBody FinancialBackReason financialBackReason) {
        List<FinancialBackReason> list = financialBackReasonService.list(Condition.getQueryWrapper(financialBackReason));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入AccountBackReasonParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialBackReasonParam> pageParam) {
        final FinancialBackReasonParam param = pageParam.getParam();

        final IPage<FinancialBackReason> page = financialBackReasonService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<FinancialBackReasonVo> accountBackReasonVoIPage = AccountBackReasonWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountBackReasonVoIPage));
    }


}
