package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.enums.WorkBench;
import net.bncloud.delivery.param.BatchDeleteWorkDayParam;
import net.bncloud.delivery.param.FactoryWorkdayBatchParam;
import net.bncloud.delivery.param.FactoryWorkdayParam;
import net.bncloud.delivery.param.OverAllAdjustParam;
import net.bncloud.delivery.service.FactoryWorkdayService;
import net.bncloud.delivery.service.NewFactoryWorkdayService;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 *收货/送货工厂 工作日 controller
 *
 * @author liyh
 * @since 2022-05-16
 */
@Validated
@RestController
@RequestMapping("/factory-workday/new")
@Api(tags = "收货/送货工厂 工作日 controller")
public class NewFactoryWorkdayController {

    
    @Autowired
    private NewFactoryWorkdayService factoryWorkdayService;

    /**
     * 采购-工作日分页查询
     * 参数:前端能否获取采购的编码?
    */
    @PostMapping("/zc/page")
    @ApiOperation(value = "分页查询列表", notes = "")
    public R<PageImpl<FactoryWorkdayVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryWorkdayParam> queryParam){
        IPage<FactoryWorkdayVo> page= factoryWorkdayService.selectListPage(PageUtils.toPage(pageable),queryParam, WorkBench.PURCHASE.getCode());
        return R.data(PageUtils.result(page));
    }

    /**
    * 供应商-工作日分页查询
    * 也是采购方查询供应商的工厂工作日的分页查询
    * 参数供应商的编码
    **/
    @PostMapping("/zy/page")
    @ApiOperation(value = "分页查询列表", notes = "")
    public R<PageImpl<FactoryWorkdayVo>> pageForSupplier(Pageable pageable, @RequestBody QueryParam<FactoryWorkdayParam> queryParam){
        IPage<FactoryWorkdayVo> page= factoryWorkdayService.selectListPage(PageUtils.toPage(pageable),queryParam, WorkBench.SUPPLIER.getCode());
        return R.data(PageUtils.result(page));
    }

    /**
     * 采购-工作日新增或更新(支持批量)
     * 参数:肯定要编码 belongType code FactoryWorkday的参数  如果是编辑，还要传id
     *
     */
    @PostMapping("/zc/BatchSaveOrUpdate")
    @ApiOperation(value = "采购-工作日新增(支持批量)", notes = "传入FactoryWorkdayBatchParam")
    public R saveBatch(@Valid @RequestBody FactoryWorkdayBatchParam param){
        factoryWorkdayService.batchSetWorkday(param,WorkBench.PURCHASE.getCode());
        return R.success();
    }

    /**
     * 销售-工作日新增(支持批量)
     * 供应的编码
     */
    @PostMapping("/zy/BatchSaveOrUpdate")
    @ApiOperation(value = "销售-工作日新增(支持批量)", notes = "传入FactoryWorkdayBatchParam")
    public R saveBatchForSupplier(@RequestBody FactoryWorkdayBatchParam param){
        factoryWorkdayService.batchSetWorkday(param,WorkBench.SUPPLIER.getCode());
        return R.success();
    }

    /**
     * 采购-工作日删除(支持批量)
     *
     * 需要参数: workday的id(用来删除) 每个factoryworkday的factoryid(用于构建修改的假日)
     *
     * belongType(弃用了)  belongCode  factoryId
     *
     */
    @DeleteMapping("/zc/BatchDelete")
    @ApiOperation(value = "采购/销售-工作日删除(支持批量)", notes = "传入ids")
    public R deleteBatch(@RequestBody BatchDeleteWorkDayParam param){
        factoryWorkdayService.deleteBatchWorkday(param);
        return R.success();
    }

    /**
     * 销售-工作日删除(支持批量)
     *
     * 需要参数: workday的id(用来删除) 每个factoryworkday的factoryid(用于构建修改的假日)
     *
     * belongType(弃用了)  belongCode  factoryId
     *
     */
    @DeleteMapping("/zy/BatchDelete")
    @ApiOperation(value = "采购/销售-工作日删除(支持批量)", notes = "传入ids")
    public R deleteBatchForSupplier(@RequestBody BatchDeleteWorkDayParam param){
        factoryWorkdayService.deleteBatchWorkdayForSupplier(param);
        return R.success();
    }

    /**
     * 供应方查询自己的工作日的接口
     */
    @PostMapping("/zy/supplierPage")
    @ApiOperation(value = "购方全局默认工作日配置调整接口", notes = "传入ids")
    public R<PageImpl<FactoryWorkdayVo>> supplierPage(Pageable pageable, @RequestBody QueryParam<FactoryWorkdayParam> queryParam){
        IPage<FactoryWorkdayVo> page=factoryWorkdayService.supplierPage(PageUtils.toPage(pageable),queryParam);
        return R.data(PageUtils.result(page));
    }


}
