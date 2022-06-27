package net.bncloud.financial.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.DateUtil;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
import net.bncloud.financial.param.FinancialDeliveryDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialDeliveryDetailParam;
import net.bncloud.financial.service.FinancialDeliveryDetailService;
import net.bncloud.financial.vo.FinancialDeliveryDetailVo;
import net.bncloud.financial.wrapper.AccountDeliveryDetailWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;


/**
 * 对账模块-送货明细
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/delivery-detail")
public class FinancialDeliveryDetailController {


    @Autowired
    private FinancialDeliveryDetailService financialDeliveryDetailService;





    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountDeliveryDetail")
    public R<FinancialDeliveryDetail> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialDeliveryDetailService.getById(id));
    }

    /**
     * 获取组装的送货明细
     */
    @GetMapping("/getBuildDeliveryDetail/{statementId}/{billId}")
    @ApiOperation(value = "获取组装的送货明细", notes = "传入AccountDeliveryDetail")
    public R<FinancialDeliveryDetailVo> getBuildDeliveryDetail(@PathVariable(value = "statementId") Long statementId,@PathVariable(value = "billId") Long billId) {
        return R.data(financialDeliveryDetailService.getBuildDeliveryDetail(statementId,billId));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountDeliveryDetail")
    public R save(@RequestBody FinancialDeliveryDetail financialDeliveryDetail) {
        financialDeliveryDetailService.save(financialDeliveryDetail);
        return R.success();
    }

    /**
     * 批量新增
     */
    @PostMapping("/batchSave")
    @ApiOperation(value = "批量新增", notes = "传入AccountDeliveryDetail")
    public R<String> batchSave(@Validated @RequestBody FinancialDeliveryDetailBatchSaveParam batchSaveParam) {
        try {
            financialDeliveryDetailService.batchSave(batchSaveParam);
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
        financialDeliveryDetailService.deleteDetail(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountDeliveryDetail")
    public R updateById(@RequestBody @Validated({BaseEntity.Update.class}) FinancialDeliveryDetail financialDeliveryDetail) {
        financialDeliveryDetailService.updateById(financialDeliveryDetail);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountDeliveryDetail")
    public R<List<FinancialDeliveryDetail>> list(@RequestBody FinancialDeliveryDetail financialDeliveryDetail) {
        List<FinancialDeliveryDetail> list = financialDeliveryDetailService.list(Condition.getQueryWrapper(financialDeliveryDetail));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountDeliveryDetailParam")
    public R<PageImpl<FinancialDeliveryDetailVo>> page(Pageable pageable, @RequestBody QueryParam<FinancialDeliveryDetailParam> pageParam) {
        final IPage<FinancialDeliveryDetail> page = financialDeliveryDetailService.selectPage(PageUtils.toPage(pageable), pageParam);
        IPage<FinancialDeliveryDetailVo> pageVO = AccountDeliveryDetailWrapper.build().pageVO(page);
        return R.data(PageUtils.result(pageVO));
    }


    /**
     * 对账单送货明细列表导出接口
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, Pageable pageable, @RequestBody QueryParam<FinancialDeliveryDetailParam> pageParam ) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String timeStamp = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME_NUM);
        String fileName = URLEncoder.encode("对账单送货明细_"+timeStamp, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        final IPage<FinancialDeliveryDetail> page = financialDeliveryDetailService.selectPage(PageUtils.toPage(pageable), pageParam);
        List<FinancialDeliveryDetailVo> pageVO = AccountDeliveryDetailWrapper.build().listVO(page.getRecords());
        EasyExcel.write(response.getOutputStream(),FinancialDeliveryDetailVo.class).sheet("送货明细").doWrite(pageVO);
    }


}
