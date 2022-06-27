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

import net.bncloud.quotation.service.PricingRemarkService;
import net.bncloud.quotation.entity.PricingRemark;
import net.bncloud.quotation.param.PricingRemarkParam;
import net.bncloud.quotation.vo.PricingRemarkVo;
import net.bncloud.quotation.wrapper.PricingRemarkWrapper;

import java.util.*;


/**
 * <p>
 * 定价说明信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/pricing-remark")
public class PricingRemarkController {

    
    @Autowired
    private PricingRemarkService pricingRemarkService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入PricingRemark")
    public R<PricingRemark> getById(@PathVariable(value = "id") Long id){
        return R.data(pricingRemarkService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入PricingRemark")
    public R save(@RequestBody PricingRemark pricingRemark){
        pricingRemarkService.save(pricingRemark);
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
            pricingRemarkService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入pricingRemark")
    public R updateById(@RequestBody PricingRemark pricingRemark){
        pricingRemarkService.updateById(pricingRemark);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入pricingRemark")
    public R list(@RequestBody PricingRemark pricingRemark ){
        List<PricingRemark> list = pricingRemarkService.list(Condition.getQueryWrapper(pricingRemark));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入PricingRemarkParam")
    public R page(Pageable pageable, @RequestBody QueryParam<PricingRemarkParam> pageParam){
        final PricingRemarkParam param = pageParam.getParam();

        final IPage<PricingRemark> page = pricingRemarkService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<PricingRemarkVo> pricingRemarkVoIPage = PricingRemarkWrapper.build().pageVO(page);
		return R.data(PageUtils.result(pricingRemarkVoIPage));
    }





}
