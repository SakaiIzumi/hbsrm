package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialCostBillLine;
import net.bncloud.financial.param.FinancialCostBillLineParam;
import net.bncloud.financial.service.FinancialCostBillLineService;
import net.bncloud.financial.vo.FinancialCostBillLineVo;
import net.bncloud.financial.wrapper.AccountCostBillLineWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 费用明细信息表 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/cost-bill-line")
public class FinancialCostBillLineController {


    @Autowired
    private FinancialCostBillLineService financialCostBillLineService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountCostBillLine")
    public R<FinancialCostBillLine> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialCostBillLineService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountCostBillLine")
    public R save(@RequestBody FinancialCostBillLine financialCostBillLine) {
        financialCostBillLineService.save(financialCostBillLine);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialCostBillLineService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountCostBillLine")
    public R updateById(@RequestBody FinancialCostBillLine financialCostBillLine) {
        financialCostBillLineService.updateById(financialCostBillLine);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountCostBillLine")
    public R list(@RequestBody FinancialCostBillLine financialCostBillLine) {
        List<FinancialCostBillLine> list = financialCostBillLineService.list(Condition.getQueryWrapper(financialCostBillLine));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountCostBillLineParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialCostBillLineParam> pageParam) {
        final FinancialCostBillLineParam param = pageParam.getParam();

        final IPage<FinancialCostBillLine> page = financialCostBillLineService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<FinancialCostBillLineVo> accountCostBillLineVoIPage = AccountCostBillLineWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountCostBillLineVoIPage));
    }


}
