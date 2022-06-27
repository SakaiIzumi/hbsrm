package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.enums.QuotationStatusEnum;
import net.bncloud.quotation.enums.QuotationSupplierResponseStatus;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.service.QuotationBaseCommonService;
import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.service.QuotationSupplierService;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import net.bncloud.quotation.vo.SupplierVo;
import net.bncloud.quotation.wrapper.QuotationSupplierWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 询价供应商信息 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/quotation-supplier")
public class QuotationSupplierController {

    @Autowired
    private QuotationSupplierService quotationSupplierService;

    @Autowired
    private QuotationBaseService quotationBaseService;

    @Autowired
    private QuotationBaseCommonService quotationBaseCommonService;

    
    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationSupplier")
    public R<QuotationSupplier> getById(@PathVariable(value = "id") Long id){
        return R.data(quotationSupplierService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationSupplier")
    public R save(@RequestBody QuotationSupplier quotationSupplier){
        quotationSupplierService.save(quotationSupplier);
        return R.success();
    }

    /**
     * 清空并保存供应商信息
     */
    @PostMapping("/{quotationBaseId}/cleanAndSave")
    @ApiOperation(value = "新增", notes = "传入quotationBaseId")
    public R cleanAndSave(@PathVariable("quotationBaseId") String quotationBaseId , @RequestBody List<QuotationSupplier> quotationSupplierList){
        if (CollectionUtil.isNotEmpty(quotationSupplierList)){
            long count = quotationSupplierList.stream().map(QuotationSupplier::getSupplierCode).count();
            long disCount = quotationSupplierList.stream().map(QuotationSupplier::getSupplierCode).distinct().count();
            if (count!=disCount){
                throw new ApiException(ResultCode.FAILURE.getCode(),"不允许重复供应商，请重新选择！");
            }
        }
        quotationBaseCommonService.cleanAndSaveSuppliers(Long.parseLong(quotationBaseId),quotationSupplierList);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        quotationSupplierService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationSupplier")
    public R updateById(@RequestBody QuotationSupplier quotationSupplier){
        quotationSupplierService.updateById(quotationSupplier);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationSupplier")
    public R<List<SupplierVo>> list(@RequestBody QuotationSupplier quotationSupplier ){
        List<QuotationSupplier> list = quotationSupplierService.list(Condition.getQueryWrapper(quotationSupplier));
        //TODO
        List<SupplierVo> supplierVos = BeanUtil.copy(list, SupplierVo.class);
        supplierVos.forEach(vo->vo.setCode(vo.getSupplierCode()));
        return R.data(supplierVos);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationSupplierParam")
    public R<PageImpl<QuotationSupplierVo>> page(Pageable pageable, @RequestBody QueryParam<QuotationSupplierParam> pageParam){
        final QuotationSupplierParam param = pageParam.getParam();
        QuotationBase base = quotationBaseService.getById(param.getQuotationBaseId());
        String quotationStatus = base.getQuotationStatus();


        final IPage<QuotationSupplier> page = quotationSupplierService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<QuotationSupplierVo> pageVO = QuotationSupplierWrapper.build().pageVO(page);

		//添加询价单每个供应商是否可以通知应标状态
        pageVO.getRecords().forEach(item->{
            String responseStatus = item.getResponseStatus();
            boolean equals = !(responseStatus.equals(QuotationSupplierResponseStatus.refused.name()));

            if( (quotationStatus.equals(QuotationStatusEnum.QUOTATION.getCode())||quotationStatus.equals(QuotationStatusEnum.FRESH.getCode()) )
                     && equals  ){
                item.setNoticeBidStatus(false);
            }else {
                item.setNoticeBidStatus(true);
            }

        });


		return R.data(PageUtils.result(pageVO));
    }





}
