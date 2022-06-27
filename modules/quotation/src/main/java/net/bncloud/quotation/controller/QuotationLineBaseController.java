package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.QuotationLineBaseService;
import net.bncloud.quotation.entity.QuotationLineBase;
import net.bncloud.quotation.param.QuotationLineBaseParam;
import net.bncloud.quotation.vo.QuotationLineBaseVo;
import net.bncloud.quotation.wrapper.QuotationLineBaseWrapper;

import javax.validation.groups.Default;
import java.util.*;


/**
 * <p>
 * 询价行基础信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-line-base")
public class QuotationLineBaseController {

    
    private final QuotationLineBaseService quotationLineBaseService;

    public QuotationLineBaseController(QuotationLineBaseService quotationLineBaseService) {
        this.quotationLineBaseService = quotationLineBaseService;
    }


    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationLineBase")
    public R<QuotationLineBase> getById(@PathVariable(value = "id") Long id){
        return R.data(quotationLineBaseService.getById(id));
    }

    /**
     * 通过quotationBaseId查询询价行信息
     */
    @GetMapping("{quotationBaseId}/getByQuotationBaseId")
    @ApiOperation(value = "根据quotationBaseId查询", notes = "传入QuotationLineBase")
    public R<QuotationLineBaseVo> getByQuotationBaseId(@PathVariable(value = "quotationBaseId") Long quotationBaseId){
        return R.data(quotationLineBaseService.getByQuotationBaseId(quotationBaseId));
    }

    /**
    * 新增询价行基础信息
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationLineBase")
    public R save(@RequestBody @Validated QuotationLineBaseVo quotationLineBase){
        quotationLineBaseService.saveInfo(quotationLineBase);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        quotationLineBaseService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改询价行基础信息
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationLineBase")
    public R updateById(@RequestBody @Validated(value = {Default.class,BaseEntity.Update.class}) QuotationLineBaseVo quotationLineBase){
        quotationLineBaseService.updateInfo(quotationLineBase);
        return R.data(quotationLineBase.getId());
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationLineBase")
    public R list(@RequestBody QuotationLineBase quotationLineBase ){
        List<QuotationLineBase> list = quotationLineBaseService.list(Condition.getQueryWrapper(quotationLineBase));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationLineBaseParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationLineBaseParam> pageParam){
        final QuotationLineBaseParam param = pageParam.getParam();

        final IPage<QuotationLineBase> page = quotationLineBaseService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<QuotationLineBaseVo> quotationLineBaseVoIPage = QuotationLineBaseWrapper.build().pageVO(page);
		return R.data(PageUtils.result(quotationLineBaseVoIPage));
    }





}
