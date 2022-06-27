package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.ITRfqQuotationRecordService;
import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.quotation.param.TRfqQuotationRecordParam;
import net.bncloud.quotation.vo.TRfqQuotationRecordVo;
import net.bncloud.quotation.wrapper.TRfqQuotationRecordWrapper;

import java.util.*;


/**
 * 报价记录信息 前端控制器
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/t-rfq-quotation-record")
public class TRfqQuotationRecordController {

    
    @Autowired
    private ITRfqQuotationRecordService iTRfqQuotationRecordService;

    
    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入TRfqQuotationRecord")
    public R<TRfqQuotationRecord> getById(@PathVariable(value = "id") Long id){
        return R.data(iTRfqQuotationRecordService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入TRfqQuotationRecord")
    public R save(@RequestBody TRfqQuotationRecord tRfqQuotationRecord){
        iTRfqQuotationRecordService.save(tRfqQuotationRecord);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        iTRfqQuotationRecordService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入tRfqQuotationRecord")
    public R updateById(@RequestBody TRfqQuotationRecord tRfqQuotationRecord){
        iTRfqQuotationRecordService.updateById(tRfqQuotationRecord);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入tRfqQuotationRecord")
    public R list(@RequestBody TRfqQuotationRecord tRfqQuotationRecord ){
        List<TRfqQuotationRecord> list = iTRfqQuotationRecordService.list(Condition.getQueryWrapper(tRfqQuotationRecord));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入TRfqQuotationRecordParam")
    public R<PageImpl<TRfqQuotationRecordVo>> page(Pageable pageable, @RequestBody(required = false) QueryParam<TRfqQuotationRecordParam> pageParam){

		return R.data(PageUtils.result(iTRfqQuotationRecordService.selectPage(PageUtils.toPage(pageable),pageParam)));
    }



}
