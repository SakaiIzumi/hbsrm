package net.bncloud.financial.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.DateUtil;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.param.FinancialCostDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialCostDetailParam;
import net.bncloud.financial.service.FinancialCostDetailService;
import net.bncloud.financial.vo.FinancialCostDetailVo;
import net.bncloud.financial.wrapper.AccountCostDetailWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;


/**
 * 对账模块-费用明细
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/receivable-discount-detail")
public class FinancialCostDetailController {


    @Autowired
    private FinancialCostDetailService financialCostDetailService;




    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountReceivableDiscountDetail")
    public R<FinancialCostDetail> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialCostDetailService.getById(id));
    }

    /**
     * 获取组装的费用明细
     */
    @GetMapping("/getBuildCostDetail/{statementId}/{billId}")
    @ApiOperation(value = "获取组装的费用明细", notes = "传入AccountReceivableDiscountDetail")
    public R<FinancialCostDetailVo> getBuildCostDetail(@PathVariable(value = "statementId") Long statementId, @PathVariable(value = "billId") Long billId) {
        return R.data(financialCostDetailService.getBuildCostDetail(statementId,billId));
    }
    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountReceivableDiscountDetail")
    public R save(@RequestBody FinancialCostDetail financialCostDetail) {
        financialCostDetailService.save(financialCostDetail);
        return R.success();
    }

    /**
     * 批量新增
     */
    @PostMapping("/batchSave")
    @ApiOperation(value = "批量新增", notes = "传入AccountReceivableDiscountDetail")
    public R<String> batchSave(@RequestBody @Validated @Valid FinancialCostDetailBatchSaveParam batchSaveParam) {
        try {
            financialCostDetailService.batchSave(batchSaveParam);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialCostDetailService.deleteById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountReceivableDiscountDetail")
    public R updateById(@RequestBody @Validated({BaseEntity.Update.class}) FinancialCostDetail financialCostDetail) {
        financialCostDetailService.updateById(financialCostDetail);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountReceivableDiscountDetail")
    public R<List<FinancialCostDetail>> list(@RequestBody FinancialCostDetail financialCostDetail) {
        List<FinancialCostDetail> list = financialCostDetailService.list(Condition.getQueryWrapper(financialCostDetail));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入AccountReceivableDiscountDetailParam")
    public R<PageImpl<FinancialCostDetailVo>> page(Pageable pageable, @RequestBody QueryParam<FinancialCostDetailParam> pageParam) {

        final IPage<FinancialCostDetail> page = financialCostDetailService.selectPage(PageUtils.toPage(pageable), pageParam);
        IPage<FinancialCostDetailVo> pageVO = AccountCostDetailWrapper.build().pageVO(page);
        return R.data(PageUtils.result(pageVO));
    }

    /**
     * 对账单送货明细列表导出接口
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, Pageable pageable, @RequestBody QueryParam<FinancialCostDetailParam> pageParam ) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String timeStamp = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME_NUM);
        String fileName = URLEncoder.encode("对账单费用明细_"+timeStamp, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        final IPage<FinancialCostDetail> page = financialCostDetailService.selectPage(PageUtils.toPage(pageable), pageParam);
        List<FinancialCostDetailVo> pageVO = AccountCostDetailWrapper.build().listVO(page.getRecords());
        EasyExcel.write(response.getOutputStream(),FinancialCostDetailVo.class).sheet("费用明细").doWrite(pageVO);
    }


}
