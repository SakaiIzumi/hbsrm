package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.quotation.config.FormAndTemplatePageConfig;
import net.bncloud.quotation.service.MaterialFormExtService;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.MaterialFormService;
import net.bncloud.quotation.entity.MaterialForm;
import net.bncloud.quotation.param.MaterialFormParam;
import net.bncloud.quotation.vo.MaterialFormVo;
import net.bncloud.quotation.wrapper.MaterialFormWrapper;

import java.util.*;


/**
 * 物料表单信息表2liyh
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-form")
public class MaterialFormController {

    
    private final MaterialFormService materialFormService;
    private final MaterialFormExtService materialFormExtService;

    public MaterialFormController(MaterialFormService materialFormService,MaterialFormExtService materialFormExtService) {
        this.materialFormService = materialFormService;
        this.materialFormExtService = materialFormExtService;
    }

    @Autowired
    private FormAndTemplatePageConfig formAndTemplatePageConfig;


    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialForm")
    public R<MaterialFormVo> getById(@PathVariable(value = "id") Long id){
        return R.data(materialFormService.getInfoById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialForm")
    public void save(@RequestBody @Validated MaterialFormVo materialForm){
        materialForm.setCreateName(AuthUtil.getUser().getUserName());
        materialFormService.saveInfo(materialForm);
//        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        //删除物料表单设计器拓展信息
        materialFormExtService.deleteByFromId(Long.valueOf(id));
        materialFormService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入materialForm")
    public R updateById(@RequestBody MaterialFormVo materialForm){
        materialFormService.updateMaterialForm(materialForm);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入materialForm")
    public R list(@RequestBody MaterialForm materialForm ){
        List<MaterialForm> list = materialFormService.list(Condition.getQueryWrapper(materialForm));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialFormParam")
    public R<PageImpl<MaterialFormVo>> page(Pageable pageable, @RequestBody QueryParam<MaterialFormParam> pageParam){

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(pageNumber==0&&pageSize==20){
             pageable =  PageRequest.of(0,formAndTemplatePageConfig.getDefaultPageSize());
        }

        final IPage<MaterialForm> page = materialFormService.selectPage(PageUtils.toPage(pageable), pageParam);
		IPage<MaterialFormVo> pageVO = MaterialFormWrapper.build().pageVO(page);
		return R.data(PageUtils.result(pageVO));
    }





}
