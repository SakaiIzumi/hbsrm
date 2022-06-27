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

import net.bncloud.quotation.service.QuotationEquipmentService;
import net.bncloud.quotation.entity.QuotationEquipment;
import net.bncloud.quotation.param.QuotationEquipmentParam;
import net.bncloud.quotation.vo.QuotationEquipmentVo;
import net.bncloud.quotation.wrapper.QuotationEquipmentWrapper;

import java.util.*;


/**
 * <p>
 * 设备能力要求信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-equipment")
public class QuotationEquipmentController {

    
    @Autowired
    private QuotationEquipmentService quotationEquipmentService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationEquipment")
    public R<QuotationEquipment> getById(@PathVariable(value = "id") Long id){
        return R.data(quotationEquipmentService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationEquipment")
    public R save(@RequestBody QuotationEquipment quotationEquipment){
        quotationEquipmentService.save(quotationEquipment);
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
            quotationEquipmentService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationEquipment")
    public R updateById(@RequestBody QuotationEquipment quotationEquipment){
        quotationEquipmentService.updateById(quotationEquipment);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationEquipment")
    public R list(@RequestBody QuotationEquipment quotationEquipment ){
        List<QuotationEquipment> list = quotationEquipmentService.list(Condition.getQueryWrapper(quotationEquipment));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationEquipmentParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationEquipmentParam> pageParam){
        final QuotationEquipmentParam param = pageParam.getParam();

        final IPage<QuotationEquipment> page = quotationEquipmentService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<QuotationEquipmentVo> quotationEquipmentVoIPage = QuotationEquipmentWrapper.build().pageVO(page);
		return R.data(PageUtils.result(quotationEquipmentVoIPage));
    }





}
