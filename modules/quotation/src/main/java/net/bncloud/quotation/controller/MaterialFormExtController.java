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

import net.bncloud.quotation.service.MaterialFormExtService;
import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.param.MaterialFormExtParam;
import net.bncloud.quotation.vo.MaterialFormExtVo;
import net.bncloud.quotation.wrapper.MaterialFormExtWrapper;

import java.util.*;


/**
 * <p>
 * 物料表单扩展信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-form-ext")
public class MaterialFormExtController {

    
    @Autowired
    private MaterialFormExtService materialFormExtService;

    
    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialFormExt")
    public R<MaterialFormExt> getById(@PathVariable(value = "id") Long id){
        return R.data(materialFormExtService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialFormExt")
    public R save(@RequestBody MaterialFormExt materialFormExt){
        materialFormExtService.save(materialFormExt);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        materialFormExtService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入materialFormExt")
    public R updateById(@RequestBody MaterialFormExt materialFormExt){
        materialFormExtService.updateById(materialFormExt);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入materialFormExt")
    public R list(@RequestBody MaterialFormExt materialFormExt ){
        List<MaterialFormExt> list = materialFormExtService.list(Condition.getQueryWrapper(materialFormExt));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialFormExtParam")
    public R page(Pageable pageable, @RequestBody QueryParam<MaterialFormExtParam> pageParam){
        final MaterialFormExtParam param = pageParam.getParam();

        final IPage<MaterialFormExt> page = materialFormExtService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<MaterialFormExtVo> materialFormExtVoIPage = MaterialFormExtWrapper.build().pageVO(page);
		return R.data(PageUtils.result(materialFormExtVoIPage));
    }





}
