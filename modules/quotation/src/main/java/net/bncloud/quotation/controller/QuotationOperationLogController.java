package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.StringUtil;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.param.QuotationOperationLogParam;
import net.bncloud.quotation.service.IQuotationOperationLogService;
import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.vo.QuotationOperationLogVo;
import net.bncloud.quotation.wrapper.QuotationOperationLogWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 询价单操作记录日志表 前端控制器
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
@RestController
@RequestMapping("/t-quotation-operation-log")
public class QuotationOperationLogController {


    @Autowired
    private IQuotationOperationLogService iQuotationOperationLogService;

    @Autowired
    private QuotationBaseService quotationBaseService;


    /**
     * 通过id查询
     */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationOperationLog")
    public R<QuotationOperationLog> getById(@PathVariable(value = "id") Long id) {
        return R.data(iQuotationOperationLogService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationOperationLog")
    public R save(@RequestBody QuotationOperationLog quotationOperationLog) {
        iQuotationOperationLogService.save(quotationOperationLog);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        iQuotationOperationLogService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationOperationLog")
    public R updateById(@RequestBody QuotationOperationLog quotationOperationLog) {
        iQuotationOperationLogService.updateById(quotationOperationLog);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationOperationLog")
    public R list(@RequestBody QuotationOperationLog quotationOperationLog) {
        List<QuotationOperationLog> list = iQuotationOperationLogService.list(Condition.getQueryWrapper(quotationOperationLog));

        return R.data(list);
    }

    /**
     * 分页查询
     * 采购方可以查看所有的操作日志
     * 供应商只可以查看自己和采购方的操作日志
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationOperationLogParam")
    public R<PageImpl<QuotationOperationLogVo>> page(Pageable pageable, @RequestBody QueryParam<QuotationOperationLogParam> pageParam) {
        if (null == pageParam.getParam() || null == pageParam.getParam().getBillId()) {
            throw new ApiException(400,"未传询报单ID");
        }
        final QuotationOperationLogParam param = pageParam.getParam();
        LambdaQueryWrapper<QuotationOperationLog> query = Condition.getQueryWrapper(new QuotationOperationLog()).lambda();
        query.eq(QuotationOperationLog::getBillId,param.getBillId());
        if(!StringUtil.isEmpty(pageParam.getSearchValue())){
            query.like(QuotationOperationLog::getOperatorName, pageParam.getSearchValue());
        }
        QuotationBase base = quotationBaseService.getById(pageParam.getParam().getBillId());
        String publisher = base.getPublisher();
        BaseUserEntity user = AuthUtil.getUser();
        //当前供应商只可以查看自己和采购方的操作日志
        if(user.getCurrentSupplier()!=null){
            query.and(item->item.eq(QuotationOperationLog::getOperationNo,user.getUserId())
                    .or()
                    .eq(QuotationOperationLog::getOperatorName,publisher));
        }
        final IPage<QuotationOperationLog> page = iQuotationOperationLogService.page(PageUtils.toPage(pageable), query);
        IPage<QuotationOperationLogVo> quotationOperationLogVoIPage = QuotationOperationLogWrapper.build().pageVO(page);
        return R.data(PageUtils.result(quotationOperationLogVoIPage));
    }


}
