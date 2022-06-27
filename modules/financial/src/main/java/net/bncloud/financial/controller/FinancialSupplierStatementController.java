package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.enums.WorkBench;
import net.bncloud.financial.param.FinancialStatementParam;
import net.bncloud.financial.param.FinancialStatementSaveParam;
import net.bncloud.financial.service.FinancialStatementService;
import net.bncloud.financial.vo.FinancialStatementStaticsVo;
import net.bncloud.financial.vo.FinancialStatementVo;
import net.bncloud.financial.vo.StatementPurchaseRejectVo;
import net.bncloud.financial.wrapper.AccountStatementWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;


/**
 * 对账模块-供应商对账单
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/zy/financial/statement")
public class FinancialSupplierStatementController {


    @Autowired
    private FinancialStatementService financialStatementService;


    @GetMapping("/count")
    @ApiOperation(value = "对账单状态数量统计")
    public R<FinancialStatementStaticsVo> count() {
        return R.data(financialStatementService.getStatisticsInfo(WorkBench.SUPPLIER.getCode()));
    }

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountStatement")
    public R<FinancialStatementVo> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialStatementService.getStatementInfo(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountStatement")
    public R save(@RequestBody @Validated FinancialStatementSaveParam accountStatementSaveParam) {
        financialStatementService.checkPurchaserStatus(Arrays.asList(accountStatementSaveParam.getCustomerCode()));
        FinancialStatementVo accountStatementVo = financialStatementService.saveStatement(accountStatementSaveParam);
        return R.data(accountStatementVo);
    }

    /**
     * 供应商发起批量对账
     *
     * @param ids 结算池id集合
     * @return
     */
    @PostMapping("/batchStatementGenerateAuto")
    @ApiOperation(value = "供应商发起批量对账", notes = "传入结算池id集合")
    public R<List<FinancialStatementVo>> batchStatementGenerateAuto(@ApiParam("结算池id集合") @RequestBody List<String> ids) {
        return R.data(financialStatementService.batchStatementGenerateAuto(ids, WorkBench.SUPPLIER.getCode()));
    }


    /**
     * 通过id删除对账单
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除对账单", notes = "传入ids")
    public R delete(@PathVariable(value = "id") Long id) {
        financialStatementService.deleteById(id);
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountStatement")
    public R updateById(@RequestBody @Validated({Default.class, BaseEntity.Update.class}) FinancialStatement financialStatement) {
        try {
            financialStatementService.updateStatementById(financialStatement);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountStatement")
    public R<List<FinancialStatement>> list(@RequestBody FinancialStatement financialStatement) {
        List<FinancialStatement> list = financialStatementService.list(Condition.getQueryWrapper(financialStatement));
        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入AccountStatementParam")
    public R<PageImpl<FinancialStatementVo>> page(Pageable pageable, @RequestBody QueryParam<FinancialStatementParam> queryParam) {
        final IPage<FinancialStatement> page = financialStatementService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<FinancialStatementVo> pageVO = AccountStatementWrapper.build().pageVO(page);
        financialStatementService.buildPermissionButtonBatch(pageVO.getRecords());
        return R.data(PageUtils.result(pageVO));
    }


    /**
     * 发送
     */
    @PostMapping("/{statementId}/supplierSend")
    @ApiOperation(value = "发送", notes = "传入对账单ID")
    public R supplierSend(@PathVariable(value = "statementId") Long statementId) {
        return R.data(financialStatementService.supplierSend(statementId));
    }

    /**
     * 撤回
     */
    @PostMapping("/{statementId}/supplierWithdraw")
    @ApiOperation(value = "撤回", notes = "传入对账单ID")
    public R purchaseWithdraw(@PathVariable(value = "statementId") Long statementId) {
        return R.data(financialStatementService.supplierWithdraw(statementId));
    }

    /**
     * 作废
     */
    @PostMapping("/{statementId}/supplierInvalid")
    @ApiOperation(value = "作废", notes = "传入对账单ID")
    public R supplierInvalid(@PathVariable(value = "statementId") Long statementId) {
        return R.data(financialStatementService.supplierInvalid(statementId));
    }

    /**
     * 确认
     */
    @PostMapping("/{statementId}/supplierConfirm")
    @ApiOperation(value = "确认", notes = "传入对账单ID")
    public R supplierConfirm(@PathVariable(value = "statementId") Long statementId) {
        return R.data(financialStatementService.supplierConfirm(statementId));
    }

    /**
     * 提醒
     */
    @PostMapping("/{statementId}/supplierRemind")
    @ApiOperation(value = "提醒", notes = "传入对账单ID")
    public R purchaseRemind(@PathVariable(value = "statementId") Long statementId) {
        return R.data(financialStatementService.supplierRemind(statementId));
    }

    /**
     * 退回
     */
    @PostMapping("/{statementId}/supplierReject")
    @ApiOperation(value = "退回", notes = "传入对账单ID")
    public R purchaseReject(@PathVariable(value = "statementId") Long statementId,@RequestBody StatementPurchaseRejectVo statementPurchaseRejectVo) {
        return R.data(financialStatementService.supplierReject(statementId,statementPurchaseRejectVo));
    }

    /**
     * 删除对账费用明细
     * @param costDetailId
     * @return
     */
    @GetMapping("/deleteCostDetail/{costDetailId}")
    public R<String> deleteCostDetail(@PathVariable("costDetailId") Long costDetailId){
        try {
            financialStatementService.deleteCostDetailById(costDetailId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

    /**
     * 删除对账送货明细
     * @param deliveryDetailId
     * @return
     */
    @GetMapping("/deleteDeliveryDetail/{deliveryDetailId}")
    public R<String> deleteDeliveryDetail(@PathVariable("deliveryDetailId") Long deliveryDetailId){
        try {
            financialStatementService.deleteDeliveryDetailById(deliveryDetailId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

}
