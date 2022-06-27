package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.quotation.entity.RestateBase;
import net.bncloud.quotation.param.RestateBaseParam;
import net.bncloud.quotation.service.IRestateBaseService;
import net.bncloud.quotation.vo.RestateBaseVo;
import net.bncloud.quotation.wrapper.RestateBaseWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 询价重报基础信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@RestController
@RequestMapping("/t-rfq-restate-base")
public class RestateBaseController {


    @Autowired
    private IRestateBaseService iRestateBaseService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入RestateBase")
    public R<RestateBase> getById(@PathVariable(value = "id") Long id) {
        return R.data(iRestateBaseService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入RestateBase")
    public R save(@RequestBody RestateBase restateBase) {
        iRestateBaseService.save(restateBase);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids) {
        String[] idsStrs = ids.split(",");
        for (String id : idsStrs) {
            iRestateBaseService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入restateBase")
    public R updateById(@RequestBody RestateBase restateBase) {
        iRestateBaseService.updateById(restateBase);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入restateBase")
    public R list(@RequestBody RestateBase restateBase) {
        List<RestateBase> list = iRestateBaseService.list(Condition.getQueryWrapper(restateBase));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入RestateBaseParam")
    public R page(Pageable pageable, @RequestBody QueryParam<RestateBaseParam> pageParam) {
        final RestateBaseParam param = pageParam.getParam();

        final IPage<RestateBase> page = iRestateBaseService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<RestateBaseVo> restateBaseVoIPage = RestateBaseWrapper.build().pageVO(page);
        return R.data(PageUtils.result(restateBaseVoIPage));
    }


}
