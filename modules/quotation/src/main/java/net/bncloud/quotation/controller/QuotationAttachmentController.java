package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.api.feign.file.FileInfo;
import net.bncloud.quotation.vo.QuotationAttachmentSaveVo;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.QuotationAttachmentService;
import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.quotation.param.QuotationAttachmentParam;
import net.bncloud.quotation.vo.QuotationAttachmentVo;
import net.bncloud.quotation.wrapper.QuotationAttachmentWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Wrapper;
import java.util.*;


/**
 * 询价单详情-上传需求附件清单
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-attachment")
public class QuotationAttachmentController {

    
    private final QuotationAttachmentService quotationAttachmentService;

    public QuotationAttachmentController(QuotationAttachmentService quotationAttachmentService) {
        this.quotationAttachmentService = quotationAttachmentService;
    }


    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationAttachment")
    public R<QuotationAttachment> getById(@PathVariable(value = "id") Long id){
        return R.data(quotationAttachmentService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationAttachment")
    public R save(@RequestBody QuotationAttachment quotationAttachment){
        quotationAttachmentService.saveInfo(quotationAttachment);
        return R.success();
    }

    /**
    * 删除接口
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        quotationAttachmentService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationAttachment")
    public R updateById(@RequestBody QuotationAttachment quotationAttachment){
        quotationAttachmentService.updateById(quotationAttachment);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationAttachment")
    public R<List<QuotationAttachment>> list(@RequestBody QuotationAttachment quotationAttachment ){
        List<QuotationAttachment> list = quotationAttachmentService.list(Condition.getQueryWrapper(quotationAttachment));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationAttachmentParam")
    public R<PageImpl<QuotationAttachmentVo>> page(Pageable pageable, @RequestBody QueryParam<QuotationAttachmentParam> pageParam){
        final QuotationAttachmentParam param = pageParam.getParam();

        final IPage<QuotationAttachment> page = quotationAttachmentService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<QuotationAttachmentVo> pageVO = QuotationAttachmentWrapper.build().pageVO(page);
		return R.data(PageUtils.result(pageVO));
    }


    /**
     * 保存附件
     *
     * quotation_base":"询价单"
     * quotation_pricing":"报价中"
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveFiles")
    public R<String> uploadFiles(@RequestBody QuotationAttachmentSaveVo quotationAttachmentSaveVo) throws Exception {
        quotationAttachmentService.saveFile(quotationAttachmentSaveVo.getBusinessFormId(),quotationAttachmentSaveVo.getBusinessType(),quotationAttachmentSaveVo.getFileInfoDtos());
        return R.success();
    }

    /**
     * 根据业务id 业务类型获取文件列表 此接口查询全部 不分页
     * @param businessFormId 业务ID
     * @param businessType 业务类型
     * @return
     */
    @PostMapping("/listFiles/{businessFormId}/{businessType}")
    public R<List<QuotationAttachmentVo>> listFiles(@PathVariable(value = "businessFormId",required = true)String businessFormId, @PathVariable(value = "businessType",required = true) String businessType){
        return R.data(quotationAttachmentService.listFiles(businessFormId,businessType));
    }

    /**
     * 根据询价单ID查询附件信息
     */
    @PostMapping("/getByBusinessFormId/{businessFormId}")
    public R<PageImpl<QuotationAttachmentVo>> getByBusinessFormId(@PathVariable(value = "businessFormId",required = true) String businessFormId, Pageable pageable){
        final IPage<QuotationAttachment> page = quotationAttachmentService.page(PageUtils.toPage(pageable), Wrappers.<QuotationAttachment>lambdaQuery().eq(QuotationAttachment::getBusinessFormId,businessFormId));
        IPage<QuotationAttachmentVo> pageVO = QuotationAttachmentWrapper.build().pageVO(page);
        return R.data(PageUtils.result(pageVO));
    }
}
