package net.bncloud.financial.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.enums.OperationLogType;
import net.bncloud.financial.enums.WorkBench;
import net.bncloud.financial.param.FinancialCostBillParam;
import net.bncloud.financial.param.FinancialCostBillSaveParam;
import net.bncloud.financial.service.FinancialCostBillService;
import net.bncloud.financial.service.FinancialOperationLogService;
import net.bncloud.financial.vo.FinancialCostBillVo;
import net.bncloud.financial.vo.FinancialOperationLogVo;
import net.bncloud.financial.wrapper.AccountCostBillWrapper;
import net.bncloud.financial.wrapper.AccountOperationLogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对账模块-供应商费用单（采购方端）
 */
@RestController
@RequestMapping("/financial/cost_bill")
public class FinancialCostBillController {

    @Autowired
    private FinancialCostBillService costBillService;


    @Autowired
    private FinancialOperationLogService operationLogService;


    /**
     * 批量新建费用单
     *
     * @param paramList
     * @return
     */
    @PostMapping("/batchSave")
    @ApiOperation(value = "批量保存费用单信息")
    public R<Void> batchSave(@RequestBody List<FinancialCostBillSaveParam> paramList) {
        costBillService.batchSaveAccountCostBill(paramList);
        return R.success();
    }

    /**
     * 新建费用单
     *
     * @param accountCostBill
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增费用单")
    public R<Void> save(@RequestBody @Validated FinancialCostBillSaveParam accountCostBill) {
        costBillService.saveAccountCostBill(accountCostBill);
        return R.success();
    }

    /**
     * 通过id查询，获取费用单详情
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "费用单详情")
    @ResponseBody
    public R<FinancialCostBillVo> getById(@PathVariable(value = "id") Long id) {
        FinancialCostBillVo accountCostBillVo = costBillService.getAccountCostBillInfo(id, WorkBench.PURCHASE.getCode());
        return R.data(accountCostBillVo);
    }

    /**
     * 分页查询费用单列表
     *
     * @param pageable   page=1&size=10
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询费用单列表")
    @ResponseBody
    public R<PageImpl<FinancialCostBillVo>> page(Pageable pageable, @RequestBody @Validated QueryParam<FinancialCostBillParam> queryParam) {
        IPage<FinancialCostBill> page = costBillService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<FinancialCostBillVo> accountCostBillVoIPage = AccountCostBillWrapper.build().pageVO(page);
        List<FinancialCostBillVo> records = accountCostBillVoIPage.getRecords();
        accountCostBillVoIPage.setRecords(records);
        return R.data(PageUtils.result(accountCostBillVoIPage));
    }

    /**
     * 分页查询该费用单的操作记录
     *
     * @return
     */
    @PostMapping("/operationLog/page")
    @ApiOperation(value = "分页查询操作记录")
    @ResponseBody
    public R getOperationLogInfo(Pageable pageable, @RequestBody FinancialOperationLog param) {
        //查询费用单的操作日志
        LambdaQueryWrapper<FinancialOperationLog> queryWrapper = new LambdaQueryWrapper<FinancialOperationLog>()
                .eq(FinancialOperationLog::getBillType, OperationLogType.COST_BILL.getCode())
                .eq(FinancialOperationLog::getBillId, param.getBillId());
        IPage<FinancialOperationLog> page = operationLogService.page(PageUtils.toPage(pageable), queryWrapper);
        IPage<FinancialOperationLogVo> operationLogVoIPage = AccountOperationLogWrapper.build().pageVO(page);
        return R.data(PageUtils.result(operationLogVoIPage));
    }

}
