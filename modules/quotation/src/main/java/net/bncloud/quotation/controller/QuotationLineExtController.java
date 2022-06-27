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

import net.bncloud.quotation.service.QuotationLineExtService;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.param.QuotationLineExtParam;
import net.bncloud.quotation.vo.QuotationLineExtVo;
import net.bncloud.quotation.wrapper.QuotationLineExtWrapper;

import java.util.*;


/**
 * <p>
 * 询价行动态行扩展信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-line-ext")
public class QuotationLineExtController {

    
    @Autowired
    private QuotationLineExtService quotationLineExtService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationLineExt")
    public R<QuotationLineExt> getById(@PathVariable(value = "id") Long id){
        return R.data(quotationLineExtService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationLineExt")
    public R save(@RequestBody QuotationLineExt quotationLineExt){
        quotationLineExtService.save(quotationLineExt);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids){
        String[] idsStrs = ids.split(",");
        for (String id:idsStrs){
            quotationLineExtService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationLineExt")
    public R updateById(@RequestBody QuotationLineExt quotationLineExt){
        quotationLineExtService.updateById(quotationLineExt);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationLineExt")
    public R list(@RequestBody QuotationLineExt quotationLineExt ){
        List<QuotationLineExt> list = quotationLineExtService.list(Condition.getQueryWrapper(quotationLineExt));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationLineExtParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationLineExtParam> pageParam){
        final QuotationLineExtParam param = pageParam.getParam();

        final IPage<QuotationLineExt> page = quotationLineExtService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<QuotationLineExtVo> quotationLineExtVoIPage = QuotationLineExtWrapper.build().pageVO(page);
		return R.data(PageUtils.result(quotationLineExtVoIPage));
    }





}
