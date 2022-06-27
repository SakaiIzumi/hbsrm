package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.QuotationAttBizEnum;
import net.bncloud.quotation.param.PricingRecordParam;
import net.bncloud.quotation.service.BiddingLineExtService;
import net.bncloud.quotation.service.PricingRecordService;
import net.bncloud.quotation.service.PricingRemarkService;
import net.bncloud.quotation.service.QuotationAttachmentService;
import net.bncloud.quotation.vo.*;
import net.bncloud.quotation.wrapper.PricingRecordWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * 定价请示记录信息 liyh2331
 *
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/pricing-record")

public class PricingRecordController {

    

    @Resource
    @Lazy
    private  PricingRecordService pricingRecordService;

    @Autowired
    private BiddingLineExtService biddingLineExtService;

    @Autowired
    private PricingRemarkService pricingRemarkService;

    @Autowired
    private QuotationAttachmentService quotationAttachmentService;



    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入PricingRecord")
    public R<PricingRecord> getById(@PathVariable(value = "id") Long id){
        return R.data(pricingRecordService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入PricingRecord")
    public R save(@RequestBody PricingRecord pricingRecord){
        pricingRecordService.save(pricingRecord);
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
            pricingRecordService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入pricingRecord")
    public R updateById(@RequestBody PricingRecord pricingRecord){
        pricingRecordService.updateById(pricingRecord);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入pricingRecord")
    public R list(@RequestBody PricingRecord pricingRecord ){
        List<PricingRecord> list = pricingRecordService.list(Condition.getQueryWrapper(pricingRecord));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入PricingRecordParam")
    public R page(Pageable pageable, @RequestBody QueryParam<PricingRecordParam> pageParam){
        final PricingRecordParam param = pageParam.getParam();

        final IPage<PricingRecord> page = pricingRecordService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<PricingRecordVo> pricingRecordVoIPage = PricingRecordWrapper.build().pageVO(page);
		return R.data(PageUtils.result(pricingRecordVoIPage));
    }

    /**
     * 定价单上传附件
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFiles")
    public R<List<QuotationAttachment>> uploadFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<QuotationAttachment> quotationAttachments = pricingRecordService.saveFile(files);
        return R.data(quotationAttachments);
    }


    /**
     * 定价信息保存
     */
    @PostMapping("/savePricingInfo")
    @ApiOperation(value = "新增定价信息", notes = "传入PricingRecord")
    public R savePricingInfo(@RequestBody PricingRecordAndRemark PricingRecordAndRemark){
        pricingRecordService.savePricingInfo(PricingRecordAndRemark);
        return R.success();
    }

    /**
     * 定价供应商信息查询
     */
    @PostMapping("{id}/getPricingInfo")
    @ApiOperation(value = "定价供应商信息查询", notes = "传入PricingRecord")
    public R getPricingInfo(@PathVariable(value = "id") Long id){
        PricingRecord pricingRecord = new PricingRecord();
        pricingRecord.setQuotationBaseId(id);
        List<PricingRecord> pricingRecords = pricingRecordService.getBaseMapper().selectList(Condition.getQueryWrapper(pricingRecord));
        HashMap<String, PricingRecord> stringPricingRecordHashMap = new HashMap<>();
        for (PricingRecord record : pricingRecords) {
            stringPricingRecordHashMap.put(record.getSupplierId(),record);
        }

        List<String> supplierIds = pricingRecords.stream().map(item -> {
            return item.getSupplierId();
        }).collect(Collectors.toList());
        List<QuotationSupplierVo> list = biddingLineExtService.quotationLineExtlist(id, true,supplierIds);

        for (QuotationSupplierVo quotationSupplierVo : list) {
            quotationSupplierVo.setPricingRecordVo(stringPricingRecordHashMap.get(quotationSupplierVo.getSupplierId().toString()));
        }

        PricingRemark pricingRemark = pricingRemarkService.getById(id);
        LambdaQueryWrapper<QuotationAttachment> quotationAttachmentLambdaQueryWrapper = Condition.getQueryWrapper(new QuotationAttachment()).lambda()
                .eq(QuotationAttachment::getBusinessFormId, id)
                .eq(QuotationAttachment::getBusinessType, QuotationAttBizEnum.QUOTATION_PRICING.getCode());
        List<QuotationAttachment> quotationAttachments = quotationAttachmentService.getBaseMapper().selectList(quotationAttachmentLambdaQueryWrapper);

        return R.data(list);
    }

    /**
     * 定价信息查询附件和备注
     */
    @PostMapping("/getPricingInfoForRemarkAndFile/{id}")
    @ApiOperation(value = "定价信息查询附件和备注", notes = "quotationLineExtListBySupplierIdsVo")
    public R<PricingInfoForRemarkAndFileVo> getPricingInfoForRemarkAndFile(@PathVariable("id") Long id) {
        LambdaQueryWrapper<PricingRemark> pricingRemarkLambdaQueryWrapper = Wrappers.lambdaQuery();
        PricingRemark pricingRemark = pricingRemarkService.getOne(pricingRemarkLambdaQueryWrapper.eq(PricingRemark::getQuotationBaseId,id));
        LambdaQueryWrapper<QuotationAttachment> quotationAttachmentLambdaQueryWrapper = Condition.getQueryWrapper(new QuotationAttachment()).lambda()
                .eq(QuotationAttachment::getBusinessFormId, id)
                .eq(QuotationAttachment::getBusinessType, QuotationAttBizEnum.QUOTATION_PRICING.getCode());
        List<QuotationAttachment> quotationAttachments = quotationAttachmentService.getBaseMapper().selectList(quotationAttachmentLambdaQueryWrapper);
        PricingInfoForRemarkAndFileVo pricingInfoForRemarkAndFileVo = new PricingInfoForRemarkAndFileVo();
        pricingInfoForRemarkAndFileVo.setRemark(pricingRemark);
        pricingInfoForRemarkAndFileVo.setQuotationAttachments(quotationAttachments);

        //构建附件信息
        quotationAttachmentService.buildAttachment(pricingInfoForRemarkAndFileVo);

        return R.data(pricingInfoForRemarkAndFileVo);
    }


    /**
     * 开标信息确认
     *
     * TRfqVerificationCodeVo tRfqVerificationCodeVo
     * ConfirmVerificationCodeVo tRfqVerificationCodeVo
     */
    @PostMapping("/verifyOpenPricingInfo")
    @ApiOperation(value = "开标信息确认", notes = "传入tRfqVerificationCodeVo")
    public R<Boolean> saveOpenPricingInfo(@RequestBody ConfirmVerificationCodeVo confirmVerificationCodeVo ){
        TRfqVerificationCodeVo tRfqVerificationCodeVo = new TRfqVerificationCodeVo();
        tRfqVerificationCodeVo.setQuotationId(confirmVerificationCodeVo.getQuotationId());
        List<TRfqVerificationCode> tRfqVerificationCodes = new ArrayList<>();

        //因为有开标按钮的原因，验证码可能会没有，所以为空
        if(ObjectUtil.isNotEmpty( confirmVerificationCodeVo.getVerificationCodeVo() )  ){
            for (VerificationCodeVo verificationCodeVo : confirmVerificationCodeVo.getVerificationCodeVo()) {
                TRfqVerificationCode tRfqVerificationCode = new TRfqVerificationCode();
                tRfqVerificationCode.setUid(verificationCodeVo.getUid());
                tRfqVerificationCode.setVerificationCode(verificationCodeVo.getVerificationCode());
                tRfqVerificationCode.setQuotationId(confirmVerificationCodeVo.getQuotationId());
                tRfqVerificationCodes.add(tRfqVerificationCode);
            }
            tRfqVerificationCodeVo.setTRfqVerificationCode(tRfqVerificationCodes);
        }


        Boolean aBoolean = pricingRecordService.verifyOpenPricingInfo(tRfqVerificationCodeVo);

        if(!aBoolean){
            return R.fail("验证码校验失败");
        }
        return R.data(aBoolean);
    }


    /**
     * 开标发送验证码
     */
    @PostMapping("/sendVerifyCode")
    @ApiOperation(value = "开标发送验证码", notes = "传入tRfqVerificationCodeVo")
    public R<Boolean> sendVerifyCode(@RequestBody TRfqVerificationCodeVo tRfqVerificationCodeVo){
        Boolean aBoolean = pricingRecordService.sendVerifyCode(tRfqVerificationCodeVo);
        return R.data(aBoolean);
    }

    /**
     * 通知供应商应标发送短信
     */
    @PostMapping("/sendVerifyCode2Supplier")
    @ApiOperation(value = "通知供应商应标发送短信", notes = "传入tRfqVerificationCodeVo")
    public R<Boolean> sendVerifyCode2Supplier(@RequestBody TRfqVerificationCodeVo tRfqVerificationCodeVo){
        Boolean aBoolean = pricingRecordService.sendVerifyCode2Supplier(tRfqVerificationCodeVo);
        return R.data(aBoolean);
    }

    /**
     * 通知对应供应商应标
     */
    @PostMapping("/bidResponse")
    @ApiOperation(value = "开标发送验证码", notes = "传入tRfqVerificationCodeVo")
    public R<Boolean> bidResponse(@RequestBody TRfqQuotationSupplierVo quotationSupplierVo){
        Boolean aBoolean = pricingRecordService.bidResponse(quotationSupplierVo);
        return R.data(aBoolean);
    }


}
