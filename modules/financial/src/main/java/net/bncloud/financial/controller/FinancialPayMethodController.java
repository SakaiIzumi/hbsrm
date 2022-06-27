package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialPayMethod;
import net.bncloud.financial.param.FinancialPayMethodParam;
import net.bncloud.financial.service.FinancialPayMethodService;
import net.bncloud.financial.vo.FinancialPayMethodVo;
import net.bncloud.financial.wrapper.AccountPayMethodWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * 对账模块-支付方式
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/pay-method")
public class FinancialPayMethodController {


    @Autowired
    private FinancialPayMethodService financialPayMethodService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountPayMethod")
    public R<FinancialPayMethod> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialPayMethodService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountPayMethod")
    public R save(@RequestBody FinancialPayMethod financialPayMethod) {
        financialPayMethodService.save(financialPayMethod);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialPayMethodService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountPayMethod")
    public R updateById(@RequestBody FinancialPayMethod financialPayMethod) {
        financialPayMethodService.updateById(financialPayMethod);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountPayMethod")
    public R list(@RequestBody FinancialPayMethod financialPayMethod) {
        List<FinancialPayMethod> list = financialPayMethodService.list(Condition.getQueryWrapper(financialPayMethod));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountPayMethodParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialPayMethodParam> pageParam) {
        final FinancialPayMethodParam param = pageParam.getParam();

        final IPage<FinancialPayMethod> page = financialPayMethodService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<FinancialPayMethodVo> accountPayMethodVoIPage = AccountPayMethodWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountPayMethodVoIPage));
    }


}
