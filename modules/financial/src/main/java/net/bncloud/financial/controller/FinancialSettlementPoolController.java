package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.param.FinancialSettlementPoolParam;
import net.bncloud.financial.service.FinancialSettlementPoolService;
import net.bncloud.financial.vo.FinancialSettlementPoolVo;
import net.bncloud.financial.wrapper.AccountSettlementPoolWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 对账模块-结算池单据接口
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/settlement-pool")
public class FinancialSettlementPoolController {


    @Autowired
    private FinancialSettlementPoolService financialSettlementPoolService;

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountSettlementPool")
    public R<FinancialSettlementPool> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialSettlementPoolService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/batchSave")
    @ApiOperation(value = "批量新增", notes = "传入AccountSettlementPool")
    public R batchSaveSettlementPool(@RequestBody List<FinancialSettlementPool> paramList) {
        financialSettlementPoolService.saveBatch(paramList);
        return R.success();
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountSettlementPool")
    public R save(@RequestBody FinancialSettlementPool financialSettlementPool) {
        financialSettlementPoolService.save(financialSettlementPool);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialSettlementPoolService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountSettlementPool")
    public R updateById(@RequestBody FinancialSettlementPool financialSettlementPool) {
        financialSettlementPoolService.updateById(financialSettlementPool);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountSettlementPool")
    public R<List<FinancialSettlementPool>> list(@RequestBody FinancialSettlementPool financialSettlementPool) {
        List<FinancialSettlementPool> list = financialSettlementPoolService.list(Condition.getQueryWrapper(financialSettlementPool));

        return R.data(list);
    }

    /**
     * 分页条件查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountSettlementPoolParam")
    public R<PageImpl<FinancialSettlementPoolVo>> page(Pageable pageable, @RequestBody QueryParam<FinancialSettlementPoolParam> pageParam) {
        final IPage<FinancialSettlementPool> page = financialSettlementPoolService.selectPage(PageUtils.toPage(pageable), pageParam);
        IPage<FinancialSettlementPoolVo> accountSettlementPoolVoIPage = AccountSettlementPoolWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountSettlementPoolVoIPage));
    }


}
