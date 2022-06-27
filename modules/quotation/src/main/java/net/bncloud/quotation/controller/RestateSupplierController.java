package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.quotation.entity.RestateSupplier;
import net.bncloud.quotation.param.RestateSupplierParam;
import net.bncloud.quotation.service.IRestateSupplierService;
import net.bncloud.quotation.vo.RestateSupplierVo;
import net.bncloud.quotation.wrapper.RestateSupplierWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 询价重报供应商邀请信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@RestController
@RequestMapping("/t-rfq-restate-supplier")
public class RestateSupplierController {

    
    @Autowired
    private IRestateSupplierService iRestateSupplierService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入RestateSupplier")
    public R<RestateSupplier> getById(@PathVariable(value = "id") Long id){
        return R.data(iRestateSupplierService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入RestateSupplier")
    public R save(@RequestBody RestateSupplier restateSupplier){
        iRestateSupplierService.save(restateSupplier);
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
            iRestateSupplierService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入restateSupplier")
    public R updateById(@RequestBody RestateSupplier restateSupplier){
        iRestateSupplierService.updateById(restateSupplier);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入restateSupplier")
    public R list(@RequestBody RestateSupplier restateSupplier ){
        List<RestateSupplier> list = iRestateSupplierService.list(Condition.getQueryWrapper(restateSupplier));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入RestateSupplierParam")
    public R page(Pageable pageable, @RequestBody QueryParam<RestateSupplierParam> pageParam){
        final RestateSupplierParam param = pageParam.getParam();

        final IPage<RestateSupplier> page = iRestateSupplierService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<RestateSupplierVo> restateSupplierVoIPage = RestateSupplierWrapper.build().pageVO(page);
		return R.data(PageUtils.result(restateSupplierVoIPage));
    }





}
