package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.QuotationMarkService;
import net.bncloud.quotation.entity.QuotationMark;
import net.bncloud.quotation.param.QuotationMarkParam;
import net.bncloud.quotation.vo.QuotationMarkVo;
import net.bncloud.quotation.wrapper.QuotationMarkWrapper;

import java.util.*;


/**
 * 询价单应标关联表 前端控制器
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/quotation-mark")
public class QuotationMarkController {


    @Autowired
    private QuotationMarkService iQuotationMarkService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationMark")
    public R<QuotationMark> getById(@PathVariable(value = "id") Long id) {
        return R.data(iQuotationMarkService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationMark")
    public R save(@RequestBody QuotationMark quotationMark) {
        iQuotationMarkService.save(quotationMark);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids) {
        String[] idsStrs = ids.split(",");
        for (String id : idsStrs) {
            iQuotationMarkService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationMark")
    public R updateById(@RequestBody QuotationMark quotationMark) {
        iQuotationMarkService.updateById(quotationMark);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationMark")
    public R list(@RequestBody QuotationMark quotationMark) {
        List<QuotationMark> list = iQuotationMarkService.list(Condition.getQueryWrapper(quotationMark));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationMarkParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationMarkParam> pageParam) {
        final QuotationMarkParam param = pageParam.getParam();

        final IPage<QuotationMark> page = iQuotationMarkService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<QuotationMarkVo> quotationMarkVoIPage = QuotationMarkWrapper.build().pageVO(page);
        return R.data(PageUtils.result(quotationMarkVoIPage));
    }

    /**
     * 拒绝报价-郑湘侠
     */
    @PostMapping("/reject")
    @ApiOperation(value = "拒绝", notes = "传入QuotationMark")
    public R reject(@RequestBody QuotationMark quotationMark) {
        iQuotationMarkService.reject(quotationMark);
        return R.success();
    }

    /**
     * 查看详情校验接口-郑湘侠
     */
    @PostMapping("/check")
    @ApiOperation(value = "查看详情校验", notes = "传入QuotationMark")
    public R check(@RequestBody QuotationMark quotationMark) {
        return R.success(iQuotationMarkService.check(quotationMark)+"");
    }

    /**
     * 查看应标的供应商列表-郑湘侠
     */
    @PostMapping("/markedSupplier")
    @ApiOperation(value = "查看应标的供应商列表", notes = "传入QuotationMark")
    public R markedSupplier(@RequestBody QuotationMark quotationMark) {
        return R.data(iQuotationMarkService.markedSupplier(quotationMark));
    }


}
