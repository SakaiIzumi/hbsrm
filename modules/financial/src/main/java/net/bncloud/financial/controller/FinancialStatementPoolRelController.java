package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialStatementPoolRel;
import net.bncloud.financial.param.FinancialStatementPoolRelParam;
import net.bncloud.financial.service.FinancialStatementPoolRelService;
import net.bncloud.financial.vo.FinancialStatementPoolRelVo;
import net.bncloud.financial.wrapper.AccountStatementPoolRelWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 对账单与结算单池单据关联关系表 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/statement-pool-rel")
public class FinancialStatementPoolRelController {


    @Autowired
    private FinancialStatementPoolRelService financialStatementPoolRelService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountStatementPoolRel")
    public R<FinancialStatementPoolRel> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialStatementPoolRelService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountStatementPoolRel")
    public R save(@RequestBody FinancialStatementPoolRel financialStatementPoolRel) {
        financialStatementPoolRelService.save(financialStatementPoolRel);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialStatementPoolRelService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountStatementPoolRel")
    public R updateById(@RequestBody FinancialStatementPoolRel financialStatementPoolRel) {
        financialStatementPoolRelService.updateById(financialStatementPoolRel);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountStatementPoolRel")
    public R list(@RequestBody FinancialStatementPoolRel financialStatementPoolRel) {
        List<FinancialStatementPoolRel> list = financialStatementPoolRelService.list(Condition.getQueryWrapper(financialStatementPoolRel));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountStatementPoolRelParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialStatementPoolRelParam> pageParam) {
        final FinancialStatementPoolRelParam param = pageParam.getParam();

        final IPage<FinancialStatementPoolRel> page = financialStatementPoolRelService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<FinancialStatementPoolRelVo> accountStatementPoolRelVoIPage = AccountStatementPoolRelWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountStatementPoolRelVoIPage));
    }


}
