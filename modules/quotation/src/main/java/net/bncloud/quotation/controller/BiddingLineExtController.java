package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.util.NumberUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.QuotationStatusEnum;
import net.bncloud.quotation.service.PricingRecordService;
import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.service.QuotationMarkService;
import net.bncloud.quotation.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.BiddingLineExtService;
import net.bncloud.quotation.param.BiddingLineExtParam;
import net.bncloud.quotation.wrapper.BiddingLineExtWrapper;

import java.util.*;


/**
 * 招标行信息 liyh6
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/bidding-line-ext")
public class BiddingLineExtController {

    @Autowired
    private QuotationMarkService iQuotationMarkService;


    @Autowired
    private BiddingLineExtService biddingLineExtService;

    @Autowired
    private PricingRecordService pricingRecordService;

    @Autowired
    private QuotationBaseService quotationBaseService;


    /**
     * 通过id查询
     */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入BiddingLineExt")
    public R<BiddingLineExt> getById(@PathVariable(value = "id") Long id) {
        return R.data(biddingLineExtService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入BiddingLineExt")
    public R save(@RequestBody BiddingLineExt biddingLineExt) {
        biddingLineExtService.save(biddingLineExt);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        biddingLineExtService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入biddingLineExt")
    public R updateById(@RequestBody BiddingLineExt biddingLineExt) {
        biddingLineExtService.updateById(biddingLineExt);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入biddingLineExt")
    public R<List<BiddingLineExtVo>> list(@RequestBody BiddingLineExt biddingLineExt) {
        return R.data(biddingLineExtService.selectList(biddingLineExt));
    }

    /**
     * 根据报价记录ID ,查询报价信息
     */
    @PostMapping("{quotationRecordId}/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationRecordId")
    public R<List<BiddingLineExt>> list(@PathVariable("quotationRecordId") Long quotationRecordId) {
        List<BiddingLineExt> list = biddingLineExtService.listByQuotationRecordId(quotationRecordId);
        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入BiddingLineExtParam")
    public R page(Pageable pageable, @RequestBody QueryParam<BiddingLineExtParam> pageParam) {
        final BiddingLineExtParam param = pageParam.getParam();

        final IPage<BiddingLineExt> page = biddingLineExtService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<BiddingLineExtVo> biddingLineExtVoIPage = BiddingLineExtWrapper.build().pageVO(page);
        return R.data(PageUtils.result(biddingLineExtVoIPage));
    }


    /**
     * 查询供应商每轮总报价  趋势图
     */
    @GetMapping("/quotationLineExtlist/{id}")
    @ApiOperation(value = "查询供应商每轮总报价", notes = "传入id")
    public R<List<QuotationSupplierVo>> quotationLineExtlist(@PathVariable(value = "id") String id) {
        List<QuotationSupplierVo> list = biddingLineExtService.quotationLineExtlist(Long.parseLong(id), true,null);
        Iterator<QuotationSupplierVo> iterator = list.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getAllBiddingLineExt().isEmpty()){
                iterator.remove();
            }
        }
        return R.data(list);
    }

    /**
     * 定价选择供应商后点击确定跳转页面
     */
    @PostMapping("/quotationLineExtlistBySupplierIds")
    @ApiOperation(value = "定价选择供应商后点击确定跳转页面", notes = "quotationLineExtListBySupplierIdsVo")
    public R<List<QuotationSupplierVo>> quotationLineExtlistBySupplierIds(@RequestBody QuotationLineExtListBySupplierIdsVo quotationLineExtListBySupplierIdsVo) {
        //查询询价单的状态
        Long quotationIdForSelect = NumberUtil.toLong(quotationLineExtListBySupplierIdsVo.getQuotationId());
        QuotationBase base = quotationBaseService.getById(quotationIdForSelect);
        List<String> supplierIdsForValidate = quotationLineExtListBySupplierIdsVo.getSupplierIds();
        //如果是已经定价的状态，直接调用查询定价接口的业务方法
        if( base.getQuotationStatus().equals(QuotationStatusEnum.HAVE_PRICING.getCode()) ){
            List<QuotationSupplierVo> pricingInfo = pricingRecordService.getPricingInfo(NumberUtil.toLong(quotationLineExtListBySupplierIdsVo.getQuotationId()));
            return R.data(pricingInfo);
        }

        if(  StringUtil.isEmpty(supplierIdsForValidate.get(0))  ){
            QuotationMark quotationMark = new QuotationMark();
            quotationMark.setQuotationId(Long.valueOf(quotationLineExtListBySupplierIdsVo.getQuotationId()));
            List<QuotationMarkVo> quotationMarkVos = iQuotationMarkService.markedSupplier(quotationMark);
            if(quotationMarkVos.isEmpty()){
                //为空没有供应商报价
                return R.fail("供应商还没有报价");
            }else{
                return R.fail("请选择供应商");
            }
        }

        String quotationId = quotationLineExtListBySupplierIdsVo.getQuotationId();
        List<String> supplierIds = quotationLineExtListBySupplierIdsVo.getSupplierIds();
        List<QuotationSupplierVo> list = biddingLineExtService.quotationLineExtlist(Long.parseLong(quotationId), true,supplierIds);
        return R.data(list);
    }


    /**
     * 查询供应商比价接口
     */
    @GetMapping("/quotationSupplierCompare/{id}")
    @ApiOperation(value = "查询供应商比价接口", notes = "传入id")
    public R<QuotationSupplierCompareVo> quotationSupplierCompare(@PathVariable(value = "id") String id) {
        QuotationSupplierCompareVo quotationSupplierCompareVo = biddingLineExtService.quotationSupplierCompare(Long.valueOf(id));
        return R.data(quotationSupplierCompareVo);
    }

    /**
     * 采购端-最低报价-郑湘侠
     */
    @GetMapping("/cheapest/{quotationBaseId}")
    @ApiOperation(value = "采购端-最低报价", notes = "传入询价单ID")
    public R<List<BiddingLineExtVo>> cheapest(@PathVariable("quotationBaseId") Long quotationBaseId){
        return R.data(biddingLineExtService.cheapest(quotationBaseId));
    }

}
