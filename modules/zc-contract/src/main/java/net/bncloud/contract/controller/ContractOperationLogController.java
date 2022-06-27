package net.bncloud.contract.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.contract.entity.ContractOperationLog;
import net.bncloud.contract.param.ContractOperationLogParam;
import net.bncloud.contract.service.ContractOperationLogService;
import net.bncloud.contract.vo.ContractOperationLogVo;
import net.bncloud.contract.wrapper.ContractOperationLogWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * zc合同日志
 *
 * @author huangtao
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zc/contract/contract-operation-log")
public class ContractOperationLogController {

    
    @Autowired
    private ContractOperationLogService contractOperationLogService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入ContractOperationLog")
    public R<ContractOperationLog> getById(@PathVariable(value = "id") Long id){
        return R.data(contractOperationLogService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入ContractOperationLog")
    public R save(@RequestBody ContractOperationLog contractOperationLog){
        contractOperationLogService.save(contractOperationLog);
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
            contractOperationLogService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入contractOperationLog")
    public R updateById(@RequestBody ContractOperationLog contractOperationLog){
        contractOperationLogService.updateById(contractOperationLog);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入contractOperationLog")
    public R list(@RequestBody ContractOperationLog contractOperationLog ){
        List<ContractOperationLog> list = contractOperationLogService.list(Condition.getQueryWrapper(contractOperationLog));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ContractOperationLogParam")
    public R<PageImpl<ContractOperationLogVo>> page(Pageable pageable, @RequestBody QueryParam<ContractOperationLogParam> queryParam){
        final ContractOperationLogParam param = queryParam.getParam();
        QueryWrapper<ContractOperationLog> queryWrapper = Condition.getQueryWrapper(param);
        queryWrapper.orderByDesc("created_date");
        final IPage<ContractOperationLog> page = contractOperationLogService.page(PageUtils.toPage(pageable), queryWrapper);
		IPage<ContractOperationLogVo> contractOperationLogVoIPage = ContractOperationLogWrapper.build().pageVO(page);
		return R.data(PageUtils.result(contractOperationLogVoIPage));
    }





}
