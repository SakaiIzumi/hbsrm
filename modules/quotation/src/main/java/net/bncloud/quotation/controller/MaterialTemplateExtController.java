package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.MaterialTemplateExtService;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.param.MaterialTemplateExtParam;
import net.bncloud.quotation.vo.MaterialTemplateExtVo;
import net.bncloud.quotation.wrapper.MaterialTemplateExtWrapper;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 物料报价模板扩展信息（物料、公式信息） 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-template-ext")
public class MaterialTemplateExtController {

    
    @Autowired
    private MaterialTemplateExtService materialTemplateExtService;

    
    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialTemplateExt")
    public R<MaterialTemplateExt> getById(@PathVariable(value = "id") Long id){
        return R.data(materialTemplateExtService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialTemplateExt")
    public R save(@RequestBody MaterialTemplateExt materialTemplateExt){
        materialTemplateExtService.save(materialTemplateExt);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        materialTemplateExtService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入materialTemplateExt")
    public R updateById(@RequestBody MaterialTemplateExt materialTemplateExt){
        materialTemplateExtService.updateById(materialTemplateExt);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入materialTemplateExt")
    public R list(@RequestBody MaterialTemplateExt materialTemplateExt ){
        QueryWrapper<MaterialTemplateExt> queryWrapper = Condition.getQueryWrapper(materialTemplateExt);
        queryWrapper.orderByAsc("order_value");
        List<MaterialTemplateExt> list = materialTemplateExtService.list(queryWrapper);
        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialTemplateExtParam")
    public R page(Pageable pageable, @RequestBody QueryParam<MaterialTemplateExtParam> pageParam){
        final MaterialTemplateExtParam param = pageParam.getParam();

        final IPage<MaterialTemplateExt> page = materialTemplateExtService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<MaterialTemplateExtVo> materialTemplateExtVoIPage = MaterialTemplateExtWrapper.build().pageVO(page);
		return R.data(PageUtils.result(materialTemplateExtVoIPage));
    }





}
