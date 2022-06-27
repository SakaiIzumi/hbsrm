package net.bncloud.contract.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.param.ContractHistoryRelParam;
import net.bncloud.contract.service.ContractHistoryRelService;
import net.bncloud.contract.vo.ContractHistoryRelVo;
import net.bncloud.contract.wrapper.ContractHistoryRelWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
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
 * <p>
 * 合同与历史合同关联关系表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/zy/contract/contract-history-rel")
public class ContractHistoryRelController {

    
    @Autowired
    private ContractHistoryRelService contractHistoryRelService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入ContractHistoryRel")
    public R<ContractHistoryRel> getById(@PathVariable(value = "id") Long id){
        return R.data(contractHistoryRelService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入ContractHistoryRel")
    public R save(@RequestBody ContractHistoryRel contractHistoryRel){
        contractHistoryRelService.save(contractHistoryRel);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        contractHistoryRelService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入contractHistoryRel")
    public R updateById(@RequestBody ContractHistoryRel contractHistoryRel){
        contractHistoryRelService.updateById(contractHistoryRel);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入contractHistoryRel")
    public R list(@RequestBody ContractHistoryRel contractHistoryRel ){
        List<ContractHistoryRel> list = contractHistoryRelService.list(Condition.getQueryWrapper(contractHistoryRel));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ContractHistoryRelParam")
    public R page(Pageable pageable, @RequestBody QueryParam<ContractHistoryRelParam> queryParam){
        final ContractHistoryRelParam param = queryParam.getParam();
        final IPage<ContractHistoryRel> page = contractHistoryRelService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<ContractHistoryRelVo> contractHistoryRelVoIPage = ContractHistoryRelWrapper.build().pageVO(page);
		return R.data(PageUtils.result(contractHistoryRelVoIPage));
    }





}
