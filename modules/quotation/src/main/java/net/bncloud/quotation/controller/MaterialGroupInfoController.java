package net.bncloud.quotation.controller;


import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.quotation.entity.MaterialGroupInfo;
import net.bncloud.quotation.param.GetLastGroupTreeParam;
import net.bncloud.quotation.param.MaterialGroupInfoParam;
import net.bncloud.quotation.service.MaterialGroupInfoService;
import net.bncloud.quotation.utils.tree.TreeFilterUtil;
import net.bncloud.quotation.vo.GetLastGroupTreeVo;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 物料分类表
 *
 * @author liyh-test
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/material-group-info")
public class MaterialGroupInfoController {

    
    @Autowired
    private MaterialGroupInfoService materialGroupInfoService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入MaterialGroupInfo")
    public R<MaterialGroupInfo> getById(@PathVariable(value = "id") Long id){
        return R.data(materialGroupInfoService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入MaterialGroupInfo")
    public R save(@RequestBody MaterialGroupInfo materialGroupInfo){
        materialGroupInfoService.save(materialGroupInfo);
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
            materialGroupInfoService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入MaterialGroupInfo")
    public R updateById(@RequestBody MaterialGroupInfo materialGroupInfo){
        materialGroupInfoService.updateById(materialGroupInfo);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入MaterialGroupInfo")
    public R<List<MaterialGroupInfo>> list(@RequestBody MaterialGroupInfo materialGroupInfo ){
        List<MaterialGroupInfo> list = materialGroupInfoService.list(Condition.getQueryWrapper(materialGroupInfo));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入MaterialGroupInfoParam")
    public R page(Pageable pageable, @RequestBody QueryParam<MaterialGroupInfoParam> pageParam){
        LambdaQueryWrapper<MaterialGroupInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        IPage<MaterialGroupInfo> pageVO = materialGroupInfoService.page(PageUtils.toPage(pageable),lambdaQueryWrapper);
		return R.data(PageUtils.result(pageVO));
    }

    /**
     * 获得分组树结构
     * @param param
     * @return
     */
    @PostMapping("/allGroupTree")
    @ApiOperation(value = "修改", notes = "传入param")
    public R<PageImpl<Tree<Long>>> allGroupTree(@RequestBody QueryParam<MaterialGroupInfo> param,Pageable pageable){
        return R.data(materialGroupInfoService.getAllGroupTree(param,PageUtils.toPage(pageable)));
    }

    /**
     * 获得分组中最底层的分组的数据,
     *
     * 比如说有三层数据 最底层是第三层  那么就返回第三层的数据 同时带上第三层的父名字(第二层)
     * @param param
     * @return
     */
    @PostMapping("/getLastGroupTree")
    @ApiOperation(value = "修改", notes = "传入param")
    public R<PageImpl<GetLastGroupTreeParam>> getLastGroupTree(@RequestBody QueryParam<GetLastGroupTreeParam> param, Pageable pageable){
        return R.data(PageUtils.result(materialGroupInfoService.getLastGroupTree(param,PageUtils.toPage(pageable))));
    }

}
