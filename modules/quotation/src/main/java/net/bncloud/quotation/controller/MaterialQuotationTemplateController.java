package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseEntity;
import net.bncloud.quotation.config.FormAndTemplatePageConfig;
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

import net.bncloud.quotation.service.MaterialQuotationTemplateService;
import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.quotation.param.MaterialQuotationTemplateParam;
import net.bncloud.quotation.vo.MaterialQuotationTemplateVo;
import net.bncloud.quotation.wrapper.MaterialQuotationTemplateWrapper;

import java.util.*;


/**
 * <p>
 * 物料报价模板 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-quotation-template")
public class MaterialQuotationTemplateController {

    
    private final MaterialQuotationTemplateService materialQuotationTemplateService;

    public MaterialQuotationTemplateController(MaterialQuotationTemplateService materialQuotationTemplateService) {
        this.materialQuotationTemplateService = materialQuotationTemplateService;
    }

    @Autowired
    private FormAndTemplatePageConfig formAndTemplatePageConfig;

    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialQuotationTemplate")
    public R<MaterialQuotationTemplateVo> getById(@PathVariable(value = "id") Long id){
        return R.data(materialQuotationTemplateService.getInfoById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialQuotationTemplate")
    public R save(@RequestBody MaterialQuotationTemplateVo materialQuotationTemplate){
        materialQuotationTemplateService.saveInfo(materialQuotationTemplate);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        materialQuotationTemplateService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入materialQuotationTemplate")
    public R updateById(@RequestBody @Validated(BaseEntity.Update.class) MaterialQuotationTemplateVo materialQuotationTemplate){
        materialQuotationTemplateService.updateInfo(materialQuotationTemplate);
        return R.success();
    }

    /**
     * 公式计算
     */
    @PostMapping("/calculate")
    @ApiOperation(value = "修改", notes = "传入materialQuotationTemplate")
    public R calculate(@RequestBody MaterialQuotationTemplateVo materialQuotationTemplate){
        materialQuotationTemplateService.validateExpression(materialQuotationTemplate);
        return R.data(materialQuotationTemplateService.calculate(materialQuotationTemplate));
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入materialQuotationTemplate")
    public R list(@RequestBody MaterialQuotationTemplate materialQuotationTemplate ){
        List<MaterialQuotationTemplate> list = materialQuotationTemplateService.list(Condition.getQueryWrapper(materialQuotationTemplate));

        return R.data(list);
    }


    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialQuotationTemplateParam")
    public R<PageImpl<MaterialQuotationTemplateVo>> page(Pageable pageable, @RequestBody QueryParam<MaterialQuotationTemplateParam> pageParam){
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(pageNumber==0&&pageSize==20){
            pageable =  PageRequest.of(0,formAndTemplatePageConfig.getDefaultPageSize());
        }
        final IPage<MaterialQuotationTemplate> page = materialQuotationTemplateService.selectPage(PageUtils.toPage(pageable), pageParam);
		IPage<MaterialQuotationTemplateVo> pageVO = MaterialQuotationTemplateWrapper.build().pageVO(page);
		return R.data(PageUtils.result(pageVO));
    }


    /**
     * 作废
     */
    @PostMapping("{id}/disable")
    @ApiOperation(value = "作废", notes = "传入MaterialQuotationTemplateParam")
    public R disable(@PathVariable(value = "id") String id){
        materialQuotationTemplateService.disable(Long.parseLong(id));
        return R.success();
    }



}
