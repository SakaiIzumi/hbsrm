package net.bncloud.contract.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;



import net.bncloud.contract.service.ContractTypeService;
import net.bncloud.contract.entity.ContractType;
import net.bncloud.contract.param.ContractTypeParam;
import net.bncloud.contract.vo.ContractTypeVo;
import net.bncloud.contract.wrapper.ContractTypeWrapper;

import java.util.*;


/**
 * <p>
 * 合同类型信息表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zy/contract/contract-type")
public class ContractTypeController {

    
    @Autowired
    private ContractTypeService contractTypeService;

    @Autowired
    private ContractService contractService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入ContractType")
    public R<ContractType> getById(@PathVariable(value = "id") Long id){
        return R.data(contractTypeService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入ContractType")
    public R save(@RequestBody ContractType contractType){
        contractTypeService.save(contractType);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        long contractTypeId = Long.parseLong(id);
        Contract contract = Contract.builder().contractTypeId(contractTypeId).build();
        QueryWrapper<Contract> queryWrapper = Wrappers.query(contract);
        int count = contractService.count(queryWrapper);
        if(count>0){
            return R.fail("合同类型被引用，不允许删除");
        }
        contractTypeService.removeById(contractTypeId);
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入contractType")
    public R updateById(@RequestBody ContractType contractType){
        contractTypeService.updateById(contractType);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入contractType")
    public R list(@RequestBody ContractType contractType ){
        List<ContractType> list = contractTypeService.list(Condition.getQueryWrapper(contractType));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ContractTypeParam")
    public R page(Pageable pageable, @RequestBody QueryParam<ContractTypeParam> queryParam){
        final ContractTypeParam param = queryParam.getParam();
        final IPage<ContractType> page = contractTypeService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<ContractTypeVo> contractTypeVoIPage = ContractTypeWrapper.build().pageVO(page);
		return R.data(PageUtils.result(contractTypeVoIPage));
    }





}
