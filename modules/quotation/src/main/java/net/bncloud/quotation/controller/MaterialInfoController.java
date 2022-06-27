package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.MaterialInfoService;
import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.param.MaterialInfoParam;

import java.util.*;


/**
 * 物料信息表
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-info")
public class MaterialInfoController {

    
    @Autowired
    private MaterialInfoService materialInfoService;

    
    /**
    * 物料详细信息 根据ID获取
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialInfo")
    public R<MaterialInfo> getById(@PathVariable(value = "id") Long id){
        return R.data(materialInfoService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialInfo")
    public R save(@RequestBody MaterialInfo materialInfo){
        materialInfoService.save(materialInfo);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        materialInfoService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入materialInfo")
    public R updateById(@RequestBody MaterialInfo materialInfo){
        materialInfoService.updateById(materialInfo);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入materialInfo")
    public R list(@RequestBody MaterialInfo materialInfo ){
        List<MaterialInfo> list = materialInfoService.list(Condition.getQueryWrapper(materialInfo));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialInfoParam")
    public R<PageImpl<MaterialInfo>> page(Pageable pageable, @RequestBody(required = false) QueryParam<MaterialInfoParam> pageParam){
        LambdaQueryWrapper<MaterialInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(pageParam == null){
            pageParam = new QueryParam<>();
        }
        if(pageParam.getParam() == null){
            pageParam.setParam(new MaterialInfoParam());
        }
        if (StringUtils.isNotEmpty(pageParam.getSearchValue())) {
            QueryParam<MaterialInfoParam> finalPageParam = pageParam;
            lambdaQueryWrapper.or(materialInfoLambdaQueryWrapper -> {
                materialInfoLambdaQueryWrapper
                        .or().like(MaterialInfo::getMaterialCode, finalPageParam.getSearchValue())
                        .or().like(MaterialInfo::getMaterialName, finalPageParam.getSearchValue())
                        .or().like(MaterialInfo::getProperty, finalPageParam.getSearchValue())
                        .or().like(MaterialInfo::getSpecial, finalPageParam.getSearchValue());
            });
//            lambdaQueryWrapper.like(MaterialInfo::getMaterialCode, finalPageParam.getSearchValue()).or().like(MaterialInfo::getMaterialName, finalPageParam.getSearchValue()).or().like(MaterialInfo::getProperty, finalPageParam.getSearchValue());
        }
        if(StringUtils.isNotEmpty(pageParam.getParam().getItemClass())){
            QueryParam<MaterialInfoParam> finalPageParam = pageParam;
            lambdaQueryWrapper.and(materialInfoLambdaQueryWrapper -> {
                materialInfoLambdaQueryWrapper.eq(MaterialInfo::getGenreCode, finalPageParam.getParam().getItemClass());
            });
        }
        //条件构造器：禁用状态
        if (StringUtils.isNotEmpty(pageParam.getParam().getForbiddenStatus())){
            lambdaQueryWrapper.like(MaterialInfo::getForbiddenStatus,pageParam.getParam().getForbiddenStatus());
        }
        IPage<MaterialInfo> pageVO = materialInfoService.page(PageUtils.toPage(pageable),lambdaQueryWrapper);
		return R.data(PageUtils.result(pageVO));
    }


    /**
     * 批量删除物料信息
     * @param idList 物料主键ID List 例如数据信息[112312,2312,12312]
     * @return
     */
    @PostMapping("/batchDeleteByIdList")
    public R<String> batchDeleteByIdList(@RequestBody List<Long> idList){
        materialInfoService.batchDeleteByIdList(idList);
        return R.success();
    }
}
